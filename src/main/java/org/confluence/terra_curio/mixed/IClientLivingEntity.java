package org.confluence.terra_curio.mixed;

import net.minecraft.world.entity.LivingEntity;

public interface IClientLivingEntity {
    void terra_curio$setShowingCosmetic(boolean showing);

    boolean terra_curio$isShowingCosmetic();

    static IClientLivingEntity of(LivingEntity living) {
        return (IClientLivingEntity) living;
    }
}
