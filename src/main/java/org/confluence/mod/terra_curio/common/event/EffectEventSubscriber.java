package org.confluence.mod.terra_curio.common.event;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.util.CuriosUtils;

@EventBusSubscriber(modid = TerraCurio.MOD_ID)
public class EffectEventSubscriber {
    @SubscribeEvent
    public static void effectApplicable(MobEffectEvent.Applicable event) {
        Holder<MobEffect> mobEffect = event.getEffectInstance().getEffect();
        LivingEntity living = event.getEntity();
        //是否有免疫buff的饰品
        if(CuriosUtils.hasEffectImmunity(living, mobEffect)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}
