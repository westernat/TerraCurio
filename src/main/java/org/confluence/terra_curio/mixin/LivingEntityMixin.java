package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.mixed.IEntity;
import org.confluence.terra_curio.mixed.ILivingEntity;
import org.confluence.terra_curio.mixed.SelfGetter;
import org.confluence.terra_curio.util.TCUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntity, SelfGetter<LivingEntity> {
    @Unique
    private int terra_curio$totem_cooldown = -1;

    @Override
    public void terra_curio$setTotemCooldown(int cooldown) {
        this.terra_curio$totem_cooldown = cooldown;
    }

    @Override
    public int terra_curio$getTotemCooldown() {
        return terra_curio$totem_cooldown;
    }

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
        if (cir.getReturnValue() && TCUtils.hasAccessoriesType(self(), ValueType.FROZEN$IMMUNE)) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"))
    private float passArmor(LivingEntity entity, float damage, DamageSource damageSource, float armorValue, float armorToughness, Operation<Float> original) {
        return original.call(entity, damage, damageSource, TCUtils.applyArmorPass(damageSource, armorValue), armorToughness);
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    private Vec3 confused(Vec3 vec3) {
        return hasEffect(TCEffects.CONFUSED) ? vec3.reverse() : vec3;
    }

    @Inject(method = "onChangedBlock", at = @At("TAIL"))
    private void onMoved(ServerLevel level, BlockPos pos, CallbackInfo ci) {
        TCUtils.onChangedBlock(self(), level);
    }

    @Inject(method = "checkTotemDeathProtection", at = @At(value = "CONSTANT", args = "nullValue=true"), cancellable = true)
    private void useTotemAbility(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (TCUtils.applyTotemAbility(self())) cir.setReturnValue(true);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (terra_curio$totem_cooldown > 0) this.terra_curio$totem_cooldown--;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("terra_curio:totem_cooldown", terra_curio$totem_cooldown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readData(CompoundTag compound, CallbackInfo ci) {
        this.terra_curio$totem_cooldown = compound.getInt("terra_curio:totem_cooldown");
    }
}
