package org.confluence.terra_curio.common.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;

import java.util.Optional;
import java.util.function.Consumer;

import static org.confluence.terra_curio.common.component.PrimitiveValueComponent.of;
import static org.confluence.terra_curio.common.init.TCItems.FULL_INFO;
import static org.confluence.terra_curio.common.init.TCItems.INFORMATION;

public class CellPhone extends MagicMirror implements IMultiFunctionCouldEnable {
    public CellPhone(Identifier id) {
        super(new Properties().fireResistant().stacksTo(1)
                .component(TCDataComponentTypes.ACCESSORIES, of(INFORMATION, FULL_INFO))
                .setId(ResourceKey.create(Registries.ITEM, id)), ModRarity.LIME);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("tooltip.item.terra_curio.cell_phone.0"));
        builder.accept(Component.translatable("tooltip.item.terra_curio.cell_phone.1"));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.ofNullable(getTooltipComponent(stack));
    }
}
