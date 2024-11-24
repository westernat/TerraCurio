package org.confluence.terra_curio.common.item.curio.information;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.terra_curio.common.item.IFunctionCouldEnable;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PDA extends BaseCurioItem implements IFunctionCouldEnable.Multi {
    public PDA(Builder builder) {
        super(builder);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        return Optional.ofNullable(getTooltipComponent(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (!tooltipFlag.hasShiftDown()) {
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    }
}
