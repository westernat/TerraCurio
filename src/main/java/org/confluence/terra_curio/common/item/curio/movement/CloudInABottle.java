package org.confluence.terra_curio.common.item.curio.movement;

import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.mesdag.particlestorm.particle.ParticleEmitter;

public class CloudInABottle extends BaseCurioItem {
    public CloudInABottle(Builder builder) {
        super(builder);
    }

    @Override
    protected void particleTick(LivingEntity living, ParticleEmitter emitter) {
        super.particleTick(living, emitter);
        if (PlayerJumpHandler.isOnCloudJump) {
            emitter.active = true;
            PlayerJumpHandler.isOnCloudJump = false;
        } else {
            emitter.active = false;
        }
    }
}
