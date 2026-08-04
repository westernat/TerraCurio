package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerClientMixin implements SelfGetter<Player> {
    @Unique
    private static final float terra_curio$fix = -(Mth.EPSILON + Mth.EPSILON);

    @ModifyVariable(method = "maybeBackOffFromEdge", at = @At("HEAD"), argsOnly = true, name = "delta")
    private Vec3 backOff(Vec3 delta) {
        return IEntity.of(confluence$self()).terra_curio$isShouldRot() ? new Vec3(delta.x, -delta.y, delta.z) : delta;
    }

    @ModifyReturnValue(method = "maybeBackOffFromEdge", at = @At(value = "RETURN", ordinal = 0))
    private Vec3 backOff2(Vec3 original, @Local(name = "deltaX") double deltaX, @Local(name = "deltaZ") double deltaZ) {
        if (IEntity.of(confluence$self()).terra_curio$isShouldRot()) {
            return new Vec3(deltaX, -original.y, deltaZ);
        }
        return original;
    }

    @Inject(method = "maybeBackOffFromEdge", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void backOff3(CallbackInfoReturnable<Vec3> cir) {
        if (IEntity.of(confluence$self()).terra_curio$isShouldRot()) {
            Vec3 vec3 = cir.getReturnValue();
            cir.setReturnValue(new Vec3(vec3.x, -vec3.y, vec3.z));
        }
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
}
