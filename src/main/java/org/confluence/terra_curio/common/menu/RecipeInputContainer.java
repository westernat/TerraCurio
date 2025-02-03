package org.confluence.terra_curio.common.menu;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeInput;

public class RecipeInputContainer extends SimpleContainer implements RecipeInput {
    private final AbstractContainerMenu menu;

    public RecipeInputContainer(AbstractContainerMenu menu, int size) {
        super(size);
        this.menu = menu;
    }

    @Override
    public int size() {
        return getContainerSize();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        menu.slotsChanged(this);
    }
}
