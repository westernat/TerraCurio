package org.confluence.terra_curio.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import org.confluence.terra_curio.common.init.TCEffects;

@javax.annotation.ParametersAreNonnullByDefault
@net.minecraft.MethodsReturnNonnullByDefault
public class HoneyEffect extends MobEffect {
    public HoneyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        living.heal(0.1F);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration % 10 == 0;
    }

    public static void applyHoneyEffect(LivingEntity living) {
        if (living instanceof Animal || living instanceof Player) {
            MobEffectInstance effect = living.getEffect(TCEffects.HONEY);
            if (effect == null || effect.getDuration() < 220) {
                living.addEffect(new MobEffectInstance(TCEffects.HONEY, 600));
            }
        } else if (living instanceof AbstractPiglin piglin) {
            piglin.setImmuneToZombification(true);
        }
    }
}
