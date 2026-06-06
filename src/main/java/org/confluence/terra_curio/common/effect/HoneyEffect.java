package org.confluence.terra_curio.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.mixed.IEntity;

public class HoneyEffect extends MobEffect {
    public HoneyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        living.heal(0.1F);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    public static void applyHoneyEffect(LivingEntity living) {
        if (LibUtils.isAnimal(living) || IEntity.of(living).terra_curio$isPlayer()) {
            MobEffectInstance effect = living.getEffect(TCEffects.HONEY.get());
            if (effect == null || effect.getDuration() < 220) {
                living.addEffect(new MobEffectInstance(TCEffects.HONEY.get(), 600));
            }
        } else if (living instanceof AbstractPiglin piglin) {
            piglin.setImmuneToZombification(true);
        }
    }
}
