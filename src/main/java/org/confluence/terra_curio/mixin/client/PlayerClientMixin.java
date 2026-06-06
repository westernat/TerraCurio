package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.lib.mixed.SelfGetter;
import org.confluence.terra_curio.mixed.IEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerClientMixin implements SelfGetter<Player> {
    @Unique
    private static final float terra_curio$fix = -(Mth.EPSILON + Mth.EPSILON);

    @ModifyVariable(method = "maybeBackOffFromEdge", at = @At("HEAD"), argsOnly = true)
    private Vec3 backOff(Vec3 pVec) {
        return IEntity.of(confluence$self()).terra_curio$isShouldRot() ? new Vec3(pVec.x, -pVec.y, pVec.z) : pVec;
    }

    @ModifyExpressionValue(method = "maybeBackOffFromEdge", at = @At(value = "NEW", target = "(DDD)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 backOff2(Vec3 original, @Local(name = "d0") double d0, @Local(name = "d1") double d1) {
        if (IEntity.of(confluence$self()).terra_curio$isShouldRot()) {
            return new Vec3(d0, -original.y, d1);
        }
        return original;
    }

    @ModifyExpressionValue(method = "maybeBackOffFromEdge", at = @At(value = "NEW", target = "(DDD)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 backOff3(Vec3 original) {
        if (IEntity.of(confluence$self()).terra_curio$isShouldRot()) {
            return new Vec3(original.x, -original.y, original.z);
        }
        return original;
    }

    @WrapOperation(method = "maybeBackOffFromEdge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;maxUpStep()F"))
    private float backOff4(Player instance, Operation<Float> original) {
        float maxUpStep = original.call(instance);
        IEntity iEntity = IEntity.of(instance);
        if (iEntity.terra_curio$isShouldRot()) {
            return terra_curio$fix - maxUpStep - iEntity.terra_curio$getDimensionHeight();
        }
        return maxUpStep;
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void flipJump(CallbackInfo ci) {
        Player self = confluence$self();
        if (IEntity.of(self).terra_curio$isShouldRot()) {
            Vec3 vec3 = self.getDeltaMovement();
            self.setDeltaMovement(vec3.x, -vec3.y, vec3.z);
        }
    }
}
