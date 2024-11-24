package org.confluence.terra_curio.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static org.confluence.terra_curio.common.component.AccessoriesComponent.of;
import static org.confluence.terra_curio.common.init.TCItems.FULL_INFO;
import static org.confluence.terra_curio.common.init.TCItems.INFORMATION;

public class CellPhone extends MagicMirror implements IFunctionCouldEnable.Multi {
    public CellPhone() {
        super(new Properties().fireResistant().stacksTo(1)
                .component(TCDataComponentTypes.MOD_RARITY, ModRarity.LIME)
                .component(TCDataComponentTypes.ACCESSORIES, of(INFORMATION, FULL_INFO)));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.cell_phone.0"));
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.cell_phone.1"));
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        return Optional.ofNullable(getTooltipComponent(stack));
    }
}
