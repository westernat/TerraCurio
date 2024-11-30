package org.confluence.terra_curio.common.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAmountRecipe implements Recipe<RecipeInput> {
    public final ItemStack result;
    public final NonNullList<Ingredient> ingredients;

    protected AbstractAmountRecipe(ItemStack pResult, NonNullList<Ingredient> pIngredients) {
        this.result = pResult;
        this.ingredients = pIngredients;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@Nullable Provider registries) {
        return result;
    }

    @Override
    public boolean matches(@NotNull RecipeInput pContainer, @NotNull Level pLevel) {
        found:
        for (Ingredient ingredient : ingredients) {
            for (int index = 0; index < pContainer.size(); index++) {
                ItemStack itemStack = pContainer.getItem(index);
                if (!itemStack.isEmpty() && ingredient.test(itemStack)) {
                    continue found;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, HolderLookup.@NotNull Provider registries) {
        extractIngredients(input, ingredients);
        return getResultItem(registries).copy();
    }

    private static void extractIngredients(RecipeInput pContainer, NonNullList<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            for (int index = 0; index < pContainer.size(); index++) {
                ItemStack itemStack = pContainer.getItem(index);
                if (!itemStack.isEmpty() && ingredient.test(itemStack)) {
                    if (ingredient.getCustomIngredient() instanceof AmountIngredient amountIngredient) {
                        itemStack.shrink(amountIngredient.amount());
                    } else {
                        itemStack.shrink(1);
                    }
                    break;
                }
            }
        }
    }

    public static void extractIngredients(CraftingContainer pContainer, NonNullList<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            for (int index = 0; index < pContainer.getContainerSize(); index++) {
                ItemStack itemStack = pContainer.getItem(index);
                if (!itemStack.isEmpty() && ingredient.test(itemStack)) {
                    if (ingredient.getCustomIngredient() instanceof AmountIngredient amountIngredient) {
                        itemStack.shrink(amountIngredient.amount());
                    } else {
                        itemStack.shrink(1);
                    }
                    break;
                }
            }
        }
    }

    public ItemStack assemble(RecipeInput container, Level level) {
        return assemble(container, level.registryAccess());
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

    public static abstract class Serializer<R extends AbstractAmountRecipe> implements RecipeSerializer<R> {
        protected abstract R newInstance(ItemStack pResult, NonNullList<Ingredient> pIngredients);
    }
}
