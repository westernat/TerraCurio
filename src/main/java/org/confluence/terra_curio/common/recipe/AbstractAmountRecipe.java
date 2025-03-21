package org.confluence.terra_curio.common.recipe;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import org.confluence.terra_curio.util.PatternMatcher;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.HashSet;

public abstract class AbstractAmountRecipe implements Recipe<RecipeInput> {
    private static final Object2ObjectFunction<Ingredient, Tuple<Integer, IntArraySet>> FUNCTION = I -> new Tuple<>(((Ingredient) I).getCustomIngredient() instanceof AmountIngredient ai ? ai.amount() : 1, new IntArraySet());
    public final ItemStack result;
    public final NonNullList<Ingredient> ingredients;

    protected AbstractAmountRecipe(ItemStack result, NonNullList<Ingredient> ingredients) {
        if (ingredients.size() > maxIngredientSize()) {
            throw new RuntimeException("Too many ingredients for '" + getGroup() + "' recipe. The maximum is: " + maxIngredientSize());
        }
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.@Nullable Provider registries) {
        return result;
    }

    @Override
    public boolean matches(RecipeInput input, Level pLevel) {
        return matches(input.size(), input::getItem, ingredients);
    }

    public static boolean matches(int size, Int2ObjectFunction<ItemStack> getItemStackCallback, NonNullList<Ingredient> ingredients) {
        HashSet<Ingredient> matches = new HashSet<>();
        Object2IntOpenHashMap<Integer> requires2Count = new Object2IntOpenHashMap<>();
        outer:
        for (int j = 0; j < ingredients.size(); j++) {
            Ingredient ingredient = ingredients.get(j);
            for (int i = 0; i < size; i++) {
                ItemStack itemStack = getItemStackCallback.apply(i);
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
            ICustomIngredient customIngredient = ingredients.get(entry.getKey()).getCustomIngredient();
            if (customIngredient == null) return false;
            if (((AmountIngredient) customIngredient).amount() > entry.getIntValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return getResultItem(registries).copy();
    }

    public ItemStack assembleAndExtract(RecipeInput input, HolderLookup.Provider registries) {
        consumeShapeless(input, ingredients);
        return assemble(input, registries);
    }

    public static void consumeShapeless(RecipeInput input, NonNullList<Ingredient> ingredients) {
        consumeShapeless(input.size(), input::getItem, ingredients);
    }

    public static void consumeShaped(RecipeInput input, int recipeWidth, int recipeHeight, ShapedRecipePattern pattern) {
        if (pattern.data.isPresent()) {
            ShapedRecipePattern.Data data = pattern.data.get();
            // 计算顶格
            int patternWidth = data.pattern().getFirst().length();
            int patternHeight = data.pattern().size();
            Vector2i patternTopLeft = PatternMatcher.findPatternTopLeft(data.pattern(), patternWidth, patternHeight);
            Vector2i containerTopLeft = PatternMatcher.findContainerTopLeft(input, recipeWidth, recipeHeight);
            // 抽取物品
            for (int i = 0; i < patternHeight; i++) {
                if (i >= patternHeight - patternTopLeft.y) continue;
                String row = data.pattern().get(i + patternTopLeft.y);
                int dy = (i + containerTopLeft.y) * recipeWidth;
                for (int j = 0; j < patternWidth; j++) {
                    if (j >= patternWidth - patternTopLeft.x) continue;
                    char c = row.charAt(j + patternTopLeft.x);
                    if (c == ' ') continue;
                    Ingredient ingredient = data.key().get(c);
                    ItemStack itemStack = input.getItem(j + containerTopLeft.x + dy);
                    if (ingredient.getCustomIngredient() instanceof AmountIngredient ai) {
                        itemStack.shrink(ai.amount());
                    } else {
                        itemStack.shrink(1);
                    }
                }
            }
        }
    }

    public static void consumeShapeless(int pContainerSize, Int2ObjectFunction<ItemStack> getItemStackCallback, NonNullList<Ingredient> ingredients) {
        Object2ObjectOpenHashMap<Ingredient, Tuple<Integer, IntArraySet>> requires2Slots = new Object2ObjectOpenHashMap<>();
        outer:
        for (Ingredient ingredient : ingredients) {
            for (int i = 0; i < pContainerSize; i++) {
                ItemStack itemStack = getItemStackCallback.apply(i);
                if (itemStack.isEmpty()) continue;
                if (ingredient.getCustomIngredient() instanceof AmountIngredient(Ingredient ingredient1, int amount)) {
                    if (amount == requires2Slots.computeIfAbsent(ingredient, FUNCTION).getB().size()) {
                        continue outer;
                    }
                    if (ingredient1.test(itemStack)) {
                        requires2Slots.computeIfAbsent(ingredient, FUNCTION).getB().add(i);
                    }
                } else if (ingredient.test(itemStack)) {
                    requires2Slots.computeIfAbsent(ingredient, FUNCTION).getB().add(i);
                }
            }
        }
        for (Tuple<Integer, IntArraySet> tuple : requires2Slots.values()) {
            int requires = tuple.getA();
            int[] slots = tuple.getB().toIntArray();
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
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    protected abstract int maxIngredientSize();

    public abstract String getGroup();

    public abstract ItemStack getToastSymbol();
}
