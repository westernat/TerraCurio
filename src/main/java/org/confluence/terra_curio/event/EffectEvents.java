package org.confluence.terra_curio.event;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.item.curio.combat.EffectInvul;

@Mod.EventBusSubscriber(modid = TerraCurio.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EffectEvents {
    @SubscribeEvent
    public static void effectApplicable(MobEffectEvent.Applicable event) {
        MobEffect mobEffect = event.getEffectInstance().getEffect();
        LivingEntity living = event.getEntity();

        if (EffectInvul.isInvul(mobEffect, living)) {
            event.setResult(Event.Result.DENY);
        }
    }
}
