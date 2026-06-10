package org.confluence.terra_curio.common.item.curio.expert;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.lib.util.LibClientUtils;
import org.confluence.terra_curio.client.TCKeyBindings;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GravityGlobe extends BaseCurioItem {
    public GravityGlobe(Builder builder) {
        super(builder);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable(
                "tooltip.item.terra_curio.gravity_globe.1",
                LibClientUtils.keyMappingComponent(TCKeyBindings.FLIP_GRAVITATION.get(), ChatFormatting.WHITE)
        ).withStyle(ChatFormatting.GRAY));
    }
}
