package org.confluence.terra_curio.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.confluence.terra_curio.common.init.TCItems;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CellPhone extends MagicMirror {
    public CellPhone() {
        super(new Properties().fireResistant().stacksTo(1)
                .component(TCDataComponentTypes.MOD_RARITY, ModRarity.LIME)
                .component(TCDataComponentTypes.ACCESSORIES, AccessoriesComponent.units(TCItems.FULL$INFORMATION)));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.cell_phone.0"));
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.cell_phone.1"));
    }
}
