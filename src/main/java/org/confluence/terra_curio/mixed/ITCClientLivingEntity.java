package org.confluence.terra_curio.mixed;

import net.minecraft.world.entity.LivingEntity;

public interface ITCClientLivingEntity {
    void terra_curio$setShowingCosmetic(boolean showing);

    boolean terra_curio$isShowingCosmetic();

    static ITCClientLivingEntity of(LivingEntity living) {
        return (ITCClientLivingEntity) living;
    }
}
