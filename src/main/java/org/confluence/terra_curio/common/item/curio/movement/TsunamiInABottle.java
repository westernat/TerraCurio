package org.confluence.terra_curio.common.item.curio.movement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.mesdag.particlestorm.particle.ParticleEmitter;

public class TsunamiInABottle extends BaseCurioItem {
    public TsunamiInABottle(Builder builder) {
        super(builder);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void particleTick(LivingEntity living, ParticleEmitter emitter, ResourceLocation particle) {
        super.particleTick(living, emitter, particle);
        if (PlayerJumpHandler.isOnTsunamiJump) {
            emitter.active = true;
            PlayerJumpHandler.isOnTsunamiJump = false;
        } else {
            emitter.active = false;
        }
    }
}
