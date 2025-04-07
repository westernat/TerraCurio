package org.confluence.terra_curio.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.recipe.AbstractAmountRecipe;
import org.jetbrains.annotations.Nullable;

@javax.annotation.ParametersAreNonnullByDefault
@net.minecraft.MethodsReturnNonnullByDefault
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
    public boolean mayPlace(ItemStack pStack) {
        return false;
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        if (recipe != null) {
            AbstractAmountRecipe.consumeShapeless(input, recipe.getIngredients());
            input.setChanged();
            updateMenu();
        }
    }

    protected void updateMenu() {}
}
