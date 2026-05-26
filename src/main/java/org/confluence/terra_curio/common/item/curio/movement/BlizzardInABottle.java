package org.confluence.terra_curio.common.item.curio.movement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.mixed.IEntity;
import org.mesdag.particlestorm.particle.ParticleEmitter;

public class BlizzardInABottle extends BaseCurioItem {
    public BlizzardInABottle(Builder builder) {
        super(builder);
    }

    @Override
    protected void particleTick(LivingEntity living, ParticleEmitter emitter, ResourceLocation particle) {
        super.particleTick(living, emitter, particle);
        emitter.active = PlayerJumpHandler.isOnBlizzardJump;

        if (emitter.active) {
            if (IEntity.of(living).terra_curio$isShouldRot()) {
                emitter.parentSpace.setTranslation(0, living.getBbHeight(), 0);
            } else {
                emitter.parentSpace.setTranslation(0, 0, 0);
            }
        }
    }
}
