package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.confluence.terra_curio.common.component.primitive.ValueType;
import org.confluence.terra_curio.common.init.TCAttachments;
import org.confluence.terra_curio.common.init.TCAttributes;
import org.confluence.terra_curio.common.init.TCDamageTypes;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.mixinauxi.IEntity;
import org.confluence.terra_curio.mixinauxi.SelfGetter;
import org.confluence.terra_curio.util.TCUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements SelfGetter<LivingEntity> {
    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);

    @ModifyArg(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"), index = 2)
    private double modifyParticlePosY(double pPosY) {
        IEntity self = (IEntity) self();
        if (self.terra_curio$isShouldRot()) {
            return pPosY + self.terra_curio$getDimensionHeight();
        }
        return pPosY;
    }

    @Inject(method = "canFreeze", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void checkFreeze(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && self().getData(TCAttachments.ACCESSORIES).contains(ValueType.FROZEN$IMMUNE)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canStandOnFluid", at = @At("RETURN"), cancellable = true)
    private void standOnFluid(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if (fluidState.isEmpty()) return;
        LivingEntity self = self();
        if (self.isCrouching()) {
            cir.setReturnValue(false);
        } else if (self.getData(TCAttachments.ACCESSORIES).getValue(ValueType.FLUID$WALK).stream().anyMatch(fluidState::is)) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z"))
    private boolean onFluid(LivingEntity instance, FluidState fluidState) {
        return false;
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0))
    private Vec3 waterWalk(Vec3 par1) {
        return c$getWalkVec(par1);
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 2))
    private Vec3 lavaJump(Vec3 par1) {
        return c$getWalkVec(par1);
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 4))
    private Vec3 lavaWalk(Vec3 par1) {
        return c$getWalkVec(par1);
    }

    @Unique
    private Vec3 c$getWalkVec(Vec3 par1) {
        LivingEntity self = self();
        if (self instanceof Player && self.getEyeInFluidType() == NeoForgeMod.EMPTY_TYPE.value()) {
            if (self.canStandOnFluid(self.level().getFluidState(self.blockPosition()))) {
                AttributeInstance instance = self.getAttribute(Attributes.MOVEMENT_SPEED);
                if (instance == null) return par1;
                double horizon = Math.min(0.91 * self.getSpeed() / instance.getBaseValue(), 0.93);
                return self.getDeltaMovement().multiply(horizon, 1.0, horizon);
            }
        }
        return par1;
    }

    @WrapOperation(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"))
    private float passArmor(LivingEntity entity, float damage, DamageSource damageSource, float armorValue, float armorToughness, Operation<Float> original) {
        if (!TCAttributes.hasCustomAttribute(TCAttributes.ARMOR_PASS) && damageSource.getEntity() instanceof LivingEntity attacker) {
            AttributeInstance attributeInstance = attacker.getAttribute(TCAttributes.ARMOR_PASS);
            if (attributeInstance != null) armorValue -= (float) attributeInstance.getValue();
            if (damageSource.is(TCDamageTypes.STAR_CLOAK)) armorValue -= 3.0F;
            armorValue = Math.max(armorValue, 0.0F);
        }
        return original.call(entity, damage, damageSource, armorValue, armorToughness);
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    private Vec3 confused(Vec3 vec3) {
        return hasEffect(TCEffects.CONFUSED) ? vec3.reverse() : vec3;
    }

    @Inject(method = "onChangedBlock", at = @At("TAIL"))
    private void onMoved(ServerLevel level, BlockPos pos, CallbackInfo ci) {
        TCUtils.onChangedBlock(self(), level, pos);
    }
}
