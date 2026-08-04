package org.confluence.terra_curio.common.item.curio.expert;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.confluence.terra_curio.client.TCKeyBindings;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;

import java.util.function.Consumer;

public class GravityGlobe extends BaseCurioItem {
    public GravityGlobe(Builder builder) {
        super(builder);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("tooltip.item.terra_curio.gravity_globe.1",
                MutableComponent.create(TCKeyBindings.FLIP_GRAVITATION.get().getTranslatedKeyMessage().getContents()).withStyle(ChatFormatting.GRAY))
        );
    }
}
