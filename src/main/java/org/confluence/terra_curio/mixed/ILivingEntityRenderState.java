package org.confluence.terra_curio.mixed;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public interface ILivingEntityRenderState {
    void terra_curio$setShowingCosmetic(boolean showing);

    boolean terra_curio$isShowingCosmetic();

    static ILivingEntityRenderState of(LivingEntityRenderState state) {
        return (ILivingEntityRenderState) state;
    }
}
