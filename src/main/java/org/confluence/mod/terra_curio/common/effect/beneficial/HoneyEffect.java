package org.confluence.mod.terra_curio.common.effect.beneficial;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class HoneyEffect extends MobEffect {
    public HoneyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity living, int amplifier) {
        living.heal(0.5F);
        return true;
    }


    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pAmplifier % 10 == 0;
    }

}
