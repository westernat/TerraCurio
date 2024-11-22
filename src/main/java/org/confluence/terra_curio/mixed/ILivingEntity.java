package org.confluence.terra_curio.mixed;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesdag.particlestorm.particle.ParticleEmitter;

import java.util.Map;

public interface ILivingEntity {
    void terra_curio$setTotemCooldown(int cooldown);

    int terra_curio$getTotemCooldown();

    @Nullable Map<ResourceLocation, ParticleEmitter> terra_curio$getParticleEmitters();

    @NotNull Map<ResourceLocation, ParticleEmitter> terra_curio$getOrCreateParticleEmitters();
}
