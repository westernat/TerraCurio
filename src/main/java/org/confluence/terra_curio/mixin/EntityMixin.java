package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.attachment.AccessoriesAttachment;
import org.confluence.terra_curio.common.init.TCAttachments;
import org.confluence.terra_curio.mixinauxi.IEntity;
import org.confluence.terra_curio.mixinauxi.SelfGetter;
import org.confluence.terra_curio.util.TCUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity, SelfGetter<Entity> {
    @Shadow
    public abstract DamageSources damageSources();

    @Shadow
    protected abstract BlockPos getOnPos(float yOffset);

    @Shadow
    public abstract EntityDimensions getDimensions(Pose pose);

    @Shadow
    public abstract Pose getPose();

    @Shadow
    public float fallDistance;

    @Shadow
    private Level level;
    @Unique
    private int terra_curio$cthulhuSprintingTime = 0;
    @Unique
    private boolean terra_curio$isShouldRot = false;
    @Unique
    private float terra_curio$dimensionHeight = 0.0F;

    @Override
    public int terra_curio$getCthulhuSprintingTime() {
        return terra_curio$cthulhuSprintingTime;
    }

    @Override
    public void terra_curio$setCthulhuSprintingTime(int amount) {
        this.terra_curio$cthulhuSprintingTime = amount;
    }

    @Override
    public void terra_curio$setShouldRot(boolean bool) {
        this.terra_curio$isShouldRot = bool;
    }

    @Override
    public boolean terra_curio$isShouldRot() {
        return terra_curio$isShouldRot;
    }

    @Override
    public float terra_curio$getDimensionHeight() {
        return terra_curio$dimensionHeight;
    }

    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInLava()Z", ordinal = 1))
    private boolean resetLavaImmune(boolean original) {
        if (self() instanceof LivingEntity living) {
            BlockPos onPos = living.getOnPos();
            if (living.level().getFluidState(onPos).is(FluidTags.LAVA) && !living.level().getFluidState(onPos.above()).is(FluidTags.LAVA)) return false;
            AccessoriesAttachment attachment = living.getData(TCAttachments.ACCESSORIES);
            if (original) {
                if (attachment.decreaseLavaImmuneTicks()) {
                    original = false;
                }
            } else {
                attachment.increaseLavaImmuneTicks();
            }
            this.terra_curio$dimensionHeight = terra_curio$isShouldRot ? getDimensions(getPose()).height() : 0.0F;
        }
        return original;
    }

    @Inject(method = "setSprinting", at = @At("TAIL"))
    private void sprinting(boolean bool, CallbackInfo ci) {
        if (bool && !level.isClientSide && self() instanceof LivingEntity living) {
            if (terra_curio$cthulhuSprintingTime == 0 && TCUtils.hasAccessoriesType(living, ValueType.SHIELD$OF$CTHULHU)) {
                float f = living.getYRot() * Mth.DEG_TO_RAD;
                double factor = living.onGround() ? 1.6 : 1.2;
                living.setDeltaMovement(living.getDeltaMovement().add(-Mth.sin(f) * factor, 0.0D, Mth.cos(f) * factor));
                this.terra_curio$cthulhuSprintingTime = 32;
            }
        }
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"))
    private void tickProfiler(CallbackInfo ci) {
        if (terra_curio$cthulhuSprintingTime > 0) this.terra_curio$cthulhuSprintingTime--;
    }

    @Inject(method = "playerTouch", at = @At("TAIL"))
    private void collidingCheck(Player player, CallbackInfo ci) {
        if (((IEntity) player).terra_curio$isOnCthulhuSprinting()) {
            Entity self = self();
            Vec3 vector = player.getDeltaMovement();
            self.addDeltaMovement(new Vec3(vector.x * 1.6, 0.6, vector.z * 1.6));
            self.hurt(damageSources().playerAttack(player), 7.8F);
            player.setDeltaMovement(vector.scale(-0.9));
            ((IEntity) player).terra_curio$setCthulhuSprintingTime(20);
        }
    }

    @Inject(method = "getOnPosLegacy", at = @At("RETURN"), cancellable = true)
    private void getOnPosAbove(CallbackInfoReturnable<BlockPos> cir) {
        if (terra_curio$isShouldRot) {
            cir.setReturnValue(getOnPos(-(terra_curio$dimensionHeight + 0.2F)));
        }
    }

    @WrapOperation(method = "checkSupportingBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;"))
    private AABB getBoundingBox(Entity instance, Operation<AABB> original) {
        AABB aabb = original.call(instance);
        if (terra_curio$isShouldRot) {
            return new AABB(aabb.minX, aabb.maxY + Mth.EPSILON, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
        }
        return aabb;
    }

    @Inject(method = "checkFallDamage", at = @At("TAIL"))
    private void updateFallDIstance(double y, boolean onGround, BlockState state, BlockPos pos, CallbackInfo ci) {
        if (terra_curio$isShouldRot && !level.isClientSide && y > 0.0) {
            this.fallDistance += (float) y;
        }
    }

    @ModifyArg(method = "spawnSprintParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"), index = 2)
    private double modifyParticlePosY(double y) {
        if (terra_curio$isShouldRot) {
            return y - 0.2 + terra_curio$dimensionHeight;
        }
        return y;
    }

    @ModifyArg(method = "spawnSprintParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"), index = 5)
    private double modifyParticleSpeedY(double y) {
        return terra_curio$isShouldRot ? -y : y;
    }
}
