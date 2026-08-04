package org.confluence.terra_curio.common.item.curio.information;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.confluence.terra_curio.common.item.IMultiFunctionCouldEnable;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;

import java.util.Optional;
import java.util.function.Consumer;

public class MultiInfoCurioItem extends BaseCurioItem implements IMultiFunctionCouldEnable {
    public MultiInfoCurioItem(Builder builder) {
        super(builder);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.ofNullable(getTooltipComponent(stack));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (!tooltipFlag.hasShiftDown()) {
            super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        }
    }
}
