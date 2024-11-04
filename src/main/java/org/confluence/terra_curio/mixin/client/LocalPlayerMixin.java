package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import net.neoforged.neoforge.fluids.FluidType;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin implements ILivingEntityExtension {
    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z", ordinal = 0))
    private boolean notCheckOnGround(boolean original) {
        return original || TCClientPacketHandler.isHasCthulhu();
    }

    @WrapWithCondition(method = "aiStep", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;sinkInFluid(Lnet/neoforged/neoforge/fluids/FluidType;)V"), remap = false)
    private boolean sinkUpFluid(LocalPlayer instance, FluidType fluidType) {
        if (GravitationHandler.isShouldRot()) {
            jumpInFluid(fluidType);
            return false;
        }
        return true;
    }
}
