package org.confluence.terra_curio.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.mixed.IEntity;
import org.confluence.terra_curio.mixed.SelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ClientEntityMixin implements SelfGetter<Entity> {
    @Shadow
    protected abstract BlockPos getOnPos(float pYOffset);

    @Shadow
    public boolean verticalCollision;

    @Inject(method = "getEyeHeight()F", at = @At("RETURN"), cancellable = true)
    private void eyeHeight(CallbackInfoReturnable<Float> cir) {
        if (self() instanceof LocalPlayer localPlayer && GravitationHandler.isShouldRot()) {
            cir.setReturnValue(((IEntity) localPlayer).terra_curio$getDimensionHeight() * 0.15F);
        }
    }

    @ModifyVariable(method = "setOnGroundWithMovement", at = @At("HEAD"), argsOnly = true)
    private boolean checkVertical(boolean bool) {
        if (!bool) return verticalCollision && self() instanceof LocalPlayer && GravitationHandler.isShouldRot();
        return true;
    }

    @Inject(method = "getOnPosLegacy", at = @At("RETURN"), cancellable = true)
    private void getOnPosAbove(CallbackInfoReturnable<BlockPos> cir) {
        if (self() instanceof Player player) {
            if (player.isLocalPlayer() ? GravitationHandler.isShouldRot() : ((IEntity) player).terra_curio$isShouldRot()) {
                cir.setReturnValue(getOnPos(-2.2F));
            }
        }
    }

    @Inject(method = "setSprinting", at = @At("TAIL"))
    private void sprinting(boolean bool, CallbackInfo ci) {
        if (bool && self() instanceof LivingEntity living) {
            IEntity entity = (IEntity) living;
            if (entity.terra_curio$getCthulhuSprintingTime() == 0 && TCClientPacketHandler.isHasCthulhu()) {
                float f = living.getYRot() * Mth.DEG_TO_RAD;
                double factor = living.onGround() ? 1.6 : 1.2;
                living.setDeltaMovement(living.getDeltaMovement().add(-Mth.sin(f) * factor, 0.0D, Mth.cos(f) * factor));
                entity.terra_curio$setCthulhuSprintingTime(32);
            }
        }
    }
}
