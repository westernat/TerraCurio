package org.confluence.terra_curio.common.effect.beneficial;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

@javax.annotation.ParametersAreNonnullByDefault
@net.minecraft.MethodsReturnNonnullByDefault
public class HoneyEffect extends MobEffect {
    public HoneyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        living.heal(0.5F);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration % 10 == 0;
    }
}
