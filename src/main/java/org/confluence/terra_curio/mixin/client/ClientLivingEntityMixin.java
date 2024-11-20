package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.confluence.terra_curio.client.handler.StepStoolHandler;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.mixed.SelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ClientLivingEntityMixin implements SelfGetter<LivingEntity> {
    @Unique
    private FluidState terra_curio$lastWalkedFluidState = null;

    @Inject(method = "checkFallDamage", at = @At("HEAD"))
    private void fall(double motionY, boolean onGround, BlockState blockState, BlockPos blockPos, CallbackInfo ci) {
        LivingEntity self = self();
        if (motionY > 0.0 && GravitationHandler.isShouldRot() && self instanceof LocalPlayer) {
            self.fallDistance += (float) motionY;
        }
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    private Vec3 confused(Vec3 vec3) {
        if (GravitationHandler.isShouldRot()) {
            return self() instanceof LocalPlayer ? new Vec3(-vec3.x, vec3.y, vec3.z) : vec3;
        } else if (StepStoolHandler.onStool()) {
            return self() instanceof LocalPlayer ? Vec3.ZERO : vec3;
        }
        return vec3;
    }

    @WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 notSlowdownY(Vec3 instance, double factorX, double factorY, double factorZ, Operation<Vec3> original) {
        if (TCClientPacketHandler.floating && TCClientPacketHandler.isCanFloating() && self() instanceof LocalPlayer) {
            return original.call(instance, factorX, 1.0, factorZ);
        }
        return original.call(instance, factorX, factorY, factorZ);
    }

    @WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z"))
    private boolean onFluid(LivingEntity instance, FluidState fluidState, Operation<Boolean> original) {
        if (terra_curio$checkCanWalk(instance, fluidState)) {
            return false;
        }
        return original.call(instance, fluidState);
    }

    @Inject(method = "canStandOnFluid", at = @At("RETURN"), cancellable = true)
    private void standOnFluid(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && terra_curio$checkCanWalk(self(), fluidState)) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    private boolean terra_curio$checkCanWalk(LivingEntity living, FluidState fluidState) {
        if (living.isCrouching() || fluidState.isEmpty() || !(living instanceof LocalPlayer)) return false;
        if (terra_curio$lastWalkedFluidState == fluidState) {
            return true;
        } else if (TCClientPacketHandler.getWalkableFluidStates().contains(fluidState)) {
            this.terra_curio$lastWalkedFluidState = fluidState;
            return true;
        }
        return false;
    }
}
