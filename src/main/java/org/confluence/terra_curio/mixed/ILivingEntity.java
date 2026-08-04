package org.confluence.terra_curio.mixed;

import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ILivingEntity {
    void terra_curio$setLastWalkedFluidState(FluidState fluidState);

    @Nullable FluidState terra_curio$getLastWalkedFluidState();

    void terra_curio$resetLastWalkedFluidState(Set<FluidState> fluidStates);

    boolean terra_curio$isFluidWalkable(FluidState fluidState);

    void terra_curio$setTotemCooldown(int cooldown);

    int terra_curio$getTotemCooldown();

//    @Nullable Map<Identifier, ParticleEmitter> terra_curio$getParticleEmitters();
//
//    @NotNull Map<Identifier, ParticleEmitter> terra_curio$getOrCreateParticleEmitters();
}
