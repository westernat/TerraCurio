package org.confluence.terra_curio.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.lib.common.component.ModRarity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CellPhone extends MagicMirror implements IMultiFunctionCouldEnable {
    public CellPhone() {
        super(ModRarity.LIME);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.cell_phone.0"));
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.cell_phone.1"));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.ofNullable(getTooltipComponent(stack));
    }
}
