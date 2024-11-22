package org.confluence.terra_curio.common.item.curio.combat;

import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.mesdag.particlestorm.particle.ParticleEmitter;

public class FrozenTurtleShell extends BaseCurioItem {
    public FrozenTurtleShell(Builder builder) {
        super(builder);
    }

    @Override
    protected void particleTick(LivingEntity living, ParticleEmitter emitter) {
        emitter.active = living.getHealth() / living.getMaxHealth() < 0.5F;
    }
}
