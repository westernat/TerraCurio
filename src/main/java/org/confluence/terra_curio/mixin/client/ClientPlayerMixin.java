package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class ClientPlayerMixin {
    @Shadow
    public abstract boolean isLocalPlayer();

    @ModifyVariable(method = "maybeBackOffFromEdge", at = @At("HEAD"), argsOnly = true)
    private Vec3 backOff(Vec3 pVec) {
        return isLocalPlayer() && GravitationHandler.isShouldRot() ? new Vec3(pVec.x, -pVec.y, pVec.z) : pVec;
    }

    @Inject(method = "maybeBackOffFromEdge", at = @At("RETURN"), cancellable = true)
    private void backOff2(Vec3 pVec, MoverType pMover, CallbackInfoReturnable<Vec3> cir) {
        if (isLocalPlayer() && GravitationHandler.isShouldRot()) {
            Vec3 vec3 = cir.getReturnValue();
            cir.setReturnValue(new Vec3(vec3.x, -vec3.y, vec3.z));
        }
    }

    @WrapOperation(method = "maybeBackOffFromEdge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;maxUpStep()F"))
    private float backOff3(Player instance, Operation<Float> original) {
        Float v = original.call(instance);
        return isLocalPlayer() && GravitationHandler.isShouldRot() ? -v : v;
    }
}
