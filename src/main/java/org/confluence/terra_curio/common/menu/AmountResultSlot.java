package org.confluence.terra_curio.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.common.recipe.AbstractAmountRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AmountResultSlot extends Slot {
    protected final RecipeInputContainer input;
    protected @Nullable AbstractAmountRecipe recipe;

    public AmountResultSlot(RecipeInputContainer input, Container result, int pSlot, int pX, int pY) {
        super(result, pSlot, pX, pY);
        this.input = input;
    }

    public void setCurrentRecipe(@Nullable AbstractAmountRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public void onTake(@NotNull Player pPlayer, @NotNull ItemStack pStack) {
        if (recipe != null) {
            AbstractAmountRecipe.extractIngredients(input, recipe.getIngredients());
            input.setChanged();
            updateMenu();
        }
    }

    protected void updateMenu() {}
}
