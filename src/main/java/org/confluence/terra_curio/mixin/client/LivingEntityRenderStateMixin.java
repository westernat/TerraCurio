package org.confluence.terra_curio.mixin.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.confluence.terra_curio.mixed.ILivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements ILivingEntityRenderState {
    @Unique
    private boolean terra_curio$showingCosmetic = false;

    @Override
    public void terra_curio$setShowingCosmetic(boolean showing) {
        this.terra_curio$showingCosmetic = showing;
    }

    @Override
    public boolean terra_curio$isShowingCosmetic() {
        return terra_curio$showingCosmetic;
    }
}
