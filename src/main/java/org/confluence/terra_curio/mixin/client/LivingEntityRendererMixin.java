package org.confluence.terra_curio.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.confluence.terra_curio.mixed.IEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "isEntityUpsideDown", at = @At("RETURN"), cancellable = true)
    private static void upsideDown(LivingEntity living, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        if ((living.getClass() == LocalPlayer.class && GravitationHandler.isShouldRot()) ||
                (living.getClass() == RemotePlayer.class && ((IEntity) living).terra_curio$isShouldRot())
        ) {
            cir.setReturnValue(true);
        }
    }
}
