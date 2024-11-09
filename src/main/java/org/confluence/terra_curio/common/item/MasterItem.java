package org.confluence.terra_curio.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.client.animate.MasterColorAnimation;
import org.jetbrains.annotations.NotNull;

public class MasterItem extends Item {
    public MasterItem() {
        super(new Properties());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(getDescriptionId()).withStyle(style -> style.withColor(MasterColorAnimation.INSTANCE.getColor()));
    }
}
