package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.lib.mixed.SelfGetter;
import org.confluence.terra_curio.client.handler.StepStoolHandler;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.mixed.IEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ClientLivingEntityMixin implements SelfGetter<LivingEntity> {
    @Inject(method = "checkFallDamage", at = @At("HEAD"))
    private void fall(CallbackInfo ci, @Local(argsOnly = true, name = "ya") double ya) {
        if (ya > 0.0 && IEntity.of(confluence$self()).terra_curio$isShouldRot()) {
            confluence$self().fallDistance += (float) ya;
        }
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true, name = "input")
    private Vec3 onStool(Vec3 input) {
        LivingEntity living = confluence$self();
        if (StepStoolHandler.onStool() && IEntity.of(living).terra_curio$isPlayer()) {
            return ((Player) living).isLocalPlayer() ? Vec3.ZERO : input;
        }
        return input;
    }

    @WrapOperation(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 notSlowdown(Vec3 instance, double xScale, double yScale, double zScale, Operation<Vec3> original) {
        if (TCClientPacketHandler.floating && TCClientPacketHandler.isCanFloating()) {
            if (IEntity.of(confluence$self()).terra_curio$isPlayer()) {
                return original.call(instance, xScale, 1.0, zScale);
            }
        }
        return original.call(instance, xScale, yScale, zScale);
    }

    @ModifyExpressionValue(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    private boolean neptunesShell(boolean original) {
        return original || (TCClientPacketHandler.isHasNeptunesShell() && IEntity.of(confluence$self()).terra_curio$isPlayer());
    }
}
