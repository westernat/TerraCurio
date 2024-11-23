package org.confluence.terra_curio.common.item.curio.information;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.common.item.IFunctionCouldEnable;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PDA extends BaseCurioItem implements IFunctionCouldEnable.Multi {
    public PDA(Builder builder) {
        super(builder);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        return Optional.ofNullable(getTooltipComponent(stack));
    }
}
