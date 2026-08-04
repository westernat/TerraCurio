package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.confluence.lib.mixed.SelfGetter;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.mixed.IEntity;
import org.confluence.terra_curio.mixed.ILivingEntity;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntity, SelfGetter<LivingEntity> {
    @Unique
    private int terra_curio$totem_cooldown = -1;
    //    @Unique
//    private Map<Identifier, ParticleEmitter> terra_curio$emitters;
    @Unique
    private FluidState terra_curio$lastWalkedFluidState = null;
    @Unique
    private Set<FluidState> terra_curio$walkableFluidStates;

    @Override
    public void terra_curio$setLastWalkedFluidState(FluidState fluidState) {
        this.terra_curio$lastWalkedFluidState = fluidState;
    }

    @Override
    public @Nullable FluidState terra_curio$getLastWalkedFluidState() {
        return terra_curio$lastWalkedFluidState;
    }

    @Override
    public void terra_curio$resetLastWalkedFluidState(Set<FluidState> fluidStates) {
        this.terra_curio$lastWalkedFluidState = null;
        this.terra_curio$walkableFluidStates = fluidStates;
    }

    @Override
    public boolean terra_curio$isFluidWalkable(FluidState fluidState) {
        return terra_curio$walkableFluidStates != null && terra_curio$walkableFluidStates.contains(fluidState);
    }

    @Override
    public void terra_curio$setTotemCooldown(int cooldown) {
        this.terra_curio$totem_cooldown = cooldown;
    }

    @Override
    public int terra_curio$getTotemCooldown() {
        return terra_curio$totem_cooldown;
    }

//    @Override
//    public @Nullable Map<Identifier, ParticleEmitter> terra_curio$getParticleEmitters() {
//        return terra_curio$emitters;
//    }
//
//    @Override
//    public @NotNull Map<Identifier, ParticleEmitter> terra_curio$getOrCreateParticleEmitters() {
//        if (terra_curio$emitters == null) {
//            this.terra_curio$emitters = new Hashtable<>();
//        }
//        return terra_curio$emitters;
//    }

    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);

    @ModifyArg(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"), index = 2)
    private double modifyParticlePosY(double posY) {
        IEntity self = IEntity.of(confluence$self());
        if (self.terra_curio$isShouldRot()) {
            return posY + self.terra_curio$getDimensionHeight() - 0.15;
        }
        return posY;
    }

    @ModifyReturnValue(method = "canFreeze", at = @At(value = "RETURN", ordinal = 1))
    private boolean checkFreeze(boolean original) {
        return TCUtils.applyFrozenImmune(confluence$self(), original);
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true, name = "input")
    private Vec3 confused(Vec3 input) {
        if (hasEffect(TCEffects.CONFUSED)) {
            input = input.reverse();
        }
        if (IEntity.of(confluence$self()).terra_curio$isShouldRot()) {
            input = new Vec3(-input.x, input.y, input.z);
        }
        return input;
    }

    @Inject(method = "travelInWater", at = @At("HEAD"))
    private void cacheFluidWalkable(CallbackInfo ci, @Share("isFluidWalkable") LocalBooleanRef isFluidWalkable) {
        isFluidWalkable.set(TCUtils.isFluidWalkable(confluence$self(), confluence$self().level().getFluidState(confluence$self().blockPosition())));
    }

    @WrapOperation(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0))
    private double skipEfficiency(LivingEntity instance, Holder<Attribute> attribute, Operation<Double> original, @Share("isFluidWalkable") LocalBooleanRef isFluidWalkable) {
        if (isFluidWalkable.get()) return 0;
        return original.call(instance, attribute);
    }

    @WrapOperation(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
    private Vec3 notSlowdown(Vec3 instance, double xScale, double yScale, double zScale, Operation<Vec3> original, @Share("isFluidWalkable") LocalBooleanRef isFluidWalkable) {
        if (isFluidWalkable.get()) {
            return original.call(instance, 0.94, yScale, 0.94);
        }
        return original.call(instance, xScale, yScale, zScale);
    }

    @WrapOperation(method = "shouldTravelInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z"))
    private boolean onFluid(LivingEntity instance, FluidState fluid, Operation<Boolean> original) {
        if (TCUtils.isFluidWalkable(instance, fluid)) {
            return false;
        }
        return original.call(instance, fluid);
    }

    @ModifyReturnValue(method = "canStandOnFluid", at = @At("RETURN"))
    private boolean standOnFluid(boolean original, @Local(argsOnly = true, name = "fluid") FluidState fluid) {
        if (!original && TCUtils.isFluidWalkable(confluence$self(), fluid)) {
            return true;
        }
        return original;
    }

    @Inject(method = "onChangedBlock", at = @At("TAIL"))
    private void onMoved(CallbackInfo ci, @Local(argsOnly = true, name = "level") ServerLevel level) {
        TCUtils.onChangedBlock(confluence$self(), level);
    }

    @Inject(method = "checkTotemDeathProtection", at = @At(value = "CONSTANT", args = "nullValue=true"), cancellable = true)
    private void useTotemAbility(CallbackInfoReturnable<Boolean> cir) {
        if (TCUtils.applyTotemAbility(confluence$self())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (terra_curio$totem_cooldown > 0) {
            --this.terra_curio$totem_cooldown;
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveData(ValueOutput output, CallbackInfo ci) {
        output.putInt("terra_curio:totem_cooldown", terra_curio$totem_cooldown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readData(ValueInput input, CallbackInfo ci) {
        this.terra_curio$totem_cooldown = input.getIntOr("terra_curio:totem_cooldown", 0);
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void flipJump(CallbackInfo ci) {
        LivingEntity self = confluence$self();
        if (IEntity.of(self).terra_curio$isShouldRot()) {
            Vec3 vec3 = self.getDeltaMovement();
            self.setDeltaMovement(vec3.x, -vec3.y, vec3.z);
        }
    }
}
