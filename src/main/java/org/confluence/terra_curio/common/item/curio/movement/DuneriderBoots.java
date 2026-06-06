package org.confluence.terra_curio.common.item.curio.movement;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes.PortAttributesExtension;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class DuneriderBoots extends BaseSpeedBoots {
    public DuneriderBoots(String name) {
        super(1, 40, builder(name)
                .attribute(PortAttributesExtension.stepHeight().value(), 0.5, PortAttributeModifier.PortOperation.ADD_VALUE));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        if (living.getBlockStateOn().is(BlockTags.SAND)) {
            speedUp(slotContext, stack, 2, 70);
        } else {
            speedUp(slotContext, stack, 1, 40);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.dunerider_boots.0"));
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.dunerider_boots.1"));
    }
}
