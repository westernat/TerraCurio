package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.confluence.terra_curio.mixed.IEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public abstract class ClientPlayerMixin {
    @Unique
    private static final float terra_curio$fix = -(Mth.EPSILON + Mth.EPSILON);

    @ModifyVariable(method = "maybeBackOffFromEdge", at = @At("HEAD"), argsOnly = true)
    private Vec3 backOff(Vec3 pVec) {
        return GravitationHandler.isShouldRot() ? new Vec3(pVec.x, -pVec.y, pVec.z) : pVec;
    }

    @Inject(method = "maybeBackOffFromEdge", at = @At(value = "RETURN", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void backOff2(Vec3 vec, MoverType mover, CallbackInfoReturnable<Vec3> cir, float f, double d0, double d1, double d2, double d3, double d4) {
        if (GravitationHandler.isShouldRot()) {
            Vec3 vec3 = cir.getReturnValue();
            cir.setReturnValue(new Vec3(d0, -vec3.y, d1));
        }
    }

    @Inject(method = "maybeBackOffFromEdge", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void backOff3(Vec3 vec, MoverType mover, CallbackInfoReturnable<Vec3> cir) {
        if (GravitationHandler.isShouldRot()) {
            Vec3 vec3 = cir.getReturnValue();
            cir.setReturnValue(new Vec3(vec3.x, -vec3.y, vec3.z));
        }
    }

    @WrapOperation(method = "maybeBackOffFromEdge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;maxUpStep()F"))
    private float backOff4(Player instance, Operation<Float> original) {
        Float maxUpStep = original.call(instance);
        if (GravitationHandler.isShouldRot()) {
            return terra_curio$fix - maxUpStep - ((IEntity) instance).terra_curio$getDimensionHeight();
        }
        return maxUpStep;
    }
}
