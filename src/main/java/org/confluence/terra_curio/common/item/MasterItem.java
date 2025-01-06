package org.confluence.terra_curio.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.client.animate.MasterColorAnimation;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;

public class MasterItem extends Item {
    public MasterItem() {
        super(new Properties().component(TCDataComponentTypes.MOD_RARITY, ModRarity.MASTER));
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(getDescriptionId()).withStyle(style -> style.withColor(MasterColorAnimation.INSTANCE.getColor()));
    }
}
