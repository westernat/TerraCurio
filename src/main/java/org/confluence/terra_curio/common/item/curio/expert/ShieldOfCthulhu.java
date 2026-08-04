package org.confluence.terra_curio.common.item.curio.expert;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.confluence.lib.util.LibClientUtils;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.client.TCKeyBindings;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;

import java.util.function.Consumer;

public class ShieldOfCthulhu extends BaseCurioItem {
    public ShieldOfCthulhu(Builder builder) {
        super(builder);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (LibUtils.isPhysicalClient()) {
            builder.accept(Component.translatable("tooltip.item.terra_curio.shield_of_cthulhu.0",
                    LibClientUtils.keyMappingComponent(TCKeyBindings.CTHULHU_SPRINTING.get())
            ));
        }
    }
}
