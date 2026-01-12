package org.confluence.terra_curio.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.confluence.terra_curio.client.handler.StepStoolHandler;
import org.confluence.terra_curio.network.NetworkHandler;
import org.confluence.terra_curio.network.c2s.FallDistancePacketC2S;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ClientLivingEntityMixin {
    @Inject(method = "checkFallDamage", at = @At("HEAD"))
    private void fall(double motionY, boolean onGround, BlockState blockState, BlockPos blockPos, CallbackInfo ci) {
        LivingEntity self = confluence$getSelf();
        if (motionY > 0.0 && self instanceof LocalPlayer && GravitationHandler.isShouldRot()) {
            self.fallDistance += (float) motionY;
            NetworkHandler.CHANNEL.sendToServer(new FallDistancePacketC2S(self.fallDistance));
        }
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    private Vec3 confused(Vec3 vec3) {
        if (confluence$getSelf() instanceof LocalPlayer) {
            if (GravitationHandler.isShouldRot()) {
                return new Vec3(-vec3.x, vec3.y, vec3.z);
            } else if (StepStoolHandler.onStool()) {
                return Vec3.ZERO;
            }
        }
        return vec3;
    }

    @Unique
    private LivingEntity confluence$getSelf() {
        return (LivingEntity) (Object) this;
    }
}
