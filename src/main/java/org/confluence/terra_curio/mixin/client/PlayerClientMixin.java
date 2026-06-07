package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.lib.mixed.SelfGetter;
import org.confluence.terra_curio.mixed.IEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerClientMixin implements SelfGetter<Player> {
    @Unique
    private static final float terra_curio$fix = -(Mth.EPSILON + Mth.EPSILON);

    @ModifyExpressionValue(method = "maybeBackOffFromEdge", at = @At(value = "FIELD", target = "Lnet/minecraft/world/phys/Vec3;y:D", opcode = Opcodes.GETFIELD, ordinal = 0))
    private double invert(double original) {
        return IEntity.of(confluence$self()).terra_curio$isShouldRot() ? -original : original;
    }

    @WrapOperation(method = "maybeBackOffFromEdge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;maxUpStep()F"))
    private float maxDownStep(Player instance, Operation<Float> original) {
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
