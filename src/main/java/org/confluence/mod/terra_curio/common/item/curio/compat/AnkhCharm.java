package org.confluence.mod.terra_curio.common.item.curio.compat;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.mod.terra_curio.client.component.EffectImmunities;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;

import java.util.List;

public class AnkhCharm extends BaseCurioItem {

    public AnkhCharm() {
        super(EffectImmunities.of(List.of(MobEffects.POISON, MobEffects.HUNGER, MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS,
            MobEffects.LEVITATION, MobEffects.WITHER, MobEffects.DARKNESS, MobEffects.BLINDNESS,
            MobEffects.CONFUSION, MobEffects.MOVEMENT_SLOWDOWN)), 2);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId()));
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId() + ".1"));
    }
}
