package org.confluence.terra_curio.common.item.curio.information;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.confluence.lib.util.LibClientUtils;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.client.TCKeyBindings;

import java.util.function.Consumer;

public class MetalDetector extends MultiInfoCurioItem {
    public MetalDetector(Builder builder) {
        super(builder);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (LibUtils.isPhysicalClient()) {
            builder.accept(Component.translatable("tooltip.item.terra_curio.metal_detector.keybinding",
                    LibClientUtils.keyMappingComponent(TCKeyBindings.METAL_DETECTOR.get())
            ));
        }
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }
}
