package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.common.attachment.AccessoriesAttachment;
import org.confluence.terra_curio.common.component.primitive.ValueType;
import org.confluence.terra_curio.common.init.TCAttachments;
import org.confluence.terra_curio.mixinauxi.IEntity;
import org.confluence.terra_curio.mixinauxi.SelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity, SelfGetter<Entity> {
    @Shadow
    public abstract DamageSources damageSources();

    @Shadow
    protected abstract BlockPos getOnPos(float yOffset);

    @Unique
    private int confluence$cthulhuSprintingTime = 0;
    @Unique
    private boolean confluence$isShouldRot = false;

    @Override
    public int terra_curio$getCthulhuSprintingTime() {
        return confluence$cthulhuSprintingTime;
    }

    @Override
    public void terra_curio$setCthulhuSprintingTime(int amount) {
        this.confluence$cthulhuSprintingTime = amount;
    }

    @Override
    public void terra_curio$setShouldRot(boolean bool) {
        this.confluence$isShouldRot = bool;
    }

    @Override
    public boolean terra_curio$isShouldRot() {
        return confluence$isShouldRot;
    }

    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInLava()Z", ordinal = 1))
    private boolean resetLavaImmune(boolean original) {
        if (self() instanceof Player living) {
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
        }
        return original;
    }

    @Inject(method = "setSprinting", at = @At("TAIL"))
    private void sprinting(boolean bool, CallbackInfo ci) {
        if (bool && self() instanceof Player living) {
            if (confluence$cthulhuSprintingTime == 0 && living.getData(TCAttachments.ACCESSORIES).contains(ValueType.SHIELD$OF$CTHULHU)) {
                float f = living.getYRot() * Mth.DEG_TO_RAD;
                double factor = living.onGround() ? 1.6 : 1.2;
                living.setDeltaMovement(living.getDeltaMovement().add(-Mth.sin(f) * factor, 0.0D, Mth.cos(f) * factor));
                this.confluence$cthulhuSprintingTime = 32;
            }
        }
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"))
    private void tickProfiler(CallbackInfo ci) {
        if (confluence$cthulhuSprintingTime > 0) this.confluence$cthulhuSprintingTime--;
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
        if (self() instanceof ServerPlayer && confluence$isShouldRot) {
            cir.setReturnValue(getOnPos(-2.2F));
        }
    }
}
