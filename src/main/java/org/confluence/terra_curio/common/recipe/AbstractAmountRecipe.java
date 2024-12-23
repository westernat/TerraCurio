package org.confluence.terra_curio.common.recipe;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public abstract class AbstractAmountRecipe implements Recipe<RecipeInput> {
    public final ItemStack result;
    public final NonNullList<Ingredient> ingredients;

    protected AbstractAmountRecipe(ItemStack pResult, NonNullList<Ingredient> pIngredients) {
        this.result = pResult;
        this.ingredients = pIngredients;
        if (ingredients.size() > maxIngredientSize()) {
            throw new RuntimeException("Too many ingredients for '" + getGroup() + "' recipe. The maximum is: " + maxIngredientSize());
        }
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@Nullable Provider registries) {
        return result;
    }

    @Override
    public boolean matches(@NotNull RecipeInput input, @NotNull Level pLevel) {
        HashSet<Ingredient> matches = new HashSet<>();
        Object2IntOpenHashMap<Integer> requires2Count = new Object2IntOpenHashMap<>();
        outer:
        for (int j = 0; j < ingredients.size(); j++) {
            Ingredient ingredient = ingredients.get(j);
            for (int i = 0; i < input.size(); i++) {
                ItemStack itemStack = input.getItem(i);
                if (itemStack.isEmpty()) continue;
                if (ingredient.getCustomIngredient() instanceof AmountIngredient amountIngredient) {
                    if (amountIngredient.ingredient().test(itemStack)) {
                        requires2Count.addTo(j, itemStack.getCount());
                        matches.add(ingredient);
                    }
                } else if (ingredient.test(itemStack)) {
                    matches.add(ingredient);
                    continue outer;
                }
            }
        }
        if (matches.size() != ingredients.size()) return false;
        for (Object2IntMap.Entry<Integer> entry : requires2Count.object2IntEntrySet()) {
            if (((AmountIngredient) ingredients.get(entry.getKey()).getCustomIngredient()).amount() > entry.getIntValue()) {
                return false;
            }
        }
        return true;
    }

    private static void consumeIngredients(int pContainerSize, Int2ObjectFunction<ItemStack> getItemStackCallback, NonNullList<Ingredient> ingredients) {
        Object2ObjectOpenHashMap<Integer, IntArrayList> requires2Slots = new Object2ObjectOpenHashMap<>();
        outer:
        for (int j = 0; j < ingredients.size(); j++) {
            Ingredient ingredient = ingredients.get(j);
            for (int i = 0; i < pContainerSize; i++) {
                ItemStack itemStack = getItemStackCallback.apply(i);
                if (itemStack.isEmpty()) continue;
                if (ingredient.getCustomIngredient() instanceof AmountIngredient amountIngredient) {
                    if (amountIngredient.ingredient().test(itemStack)) {
                        requires2Slots.computeIfAbsent(j, ai -> new IntArrayList()).add(i);
                    }
                } else if (ingredient.test(itemStack)) {
                    itemStack.shrink(1);
                    continue outer;
                }
            }
        }
        for (Object2ObjectMap.Entry<Integer, IntArrayList> entry : requires2Slots.object2ObjectEntrySet()) {
            int requires = ((AmountIngredient) ingredients.get(entry.getKey()).getCustomIngredient()).amount();
            int[] slots = entry.getValue().toIntArray();
            int avg = requires / slots.length;
            int rem = requires % slots.length;
            boolean shouldConsumeRem = false;
            if (rem > 0) {
                shouldConsumeRem = true;
                rem += avg;
            }
            for (int slot : slots) {
                ItemStack itemStack = getItemStackCallback.apply(slot);
                if (shouldConsumeRem && itemStack.getCount() >= rem) {
                    itemStack.shrink(rem);
                    shouldConsumeRem = false;
                } else {
                    itemStack.shrink(avg);
                }
            }
        }
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, HolderLookup.@NotNull Provider registries) {
        return getResultItem(registries).copy();
    }

    public ItemStack assembleAndExtract(RecipeInput input, HolderLookup.Provider registries) {
        extractInput(input, ingredients);
        return assemble(input, registries);
    }

    public static void extractInput(RecipeInput input, NonNullList<Ingredient> ingredients) {
        consumeIngredients(input.size(), input::getItem, ingredients);
    }

    public static void extractContainer(Container container, NonNullList<Ingredient> ingredients) {
        consumeIngredients(container.getContainerSize(), container::getItem, ingredients);
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    protected abstract int maxIngredientSize();

    public abstract @NotNull String getGroup();

    public abstract @NotNull ItemStack getToastSymbol();
}
