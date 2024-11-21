package org.confluence.terra_curio.mixin.sodiumdynamiclights;

import net.minecraft.world.entity.Entity;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "dev.lambdaurora.lambdynlights.api.DynamicLightHandlers", remap = false)
public abstract class DynamicLightHandlersMixin {
    @Inject(method = "getLuminanceFrom(Lnet/minecraft/world/entity/Entity;)I", at = @At("RETURN"), cancellable = true)
    private static <T extends Entity> void getAccessoryLuminance(T entity, CallbackInfoReturnable<Integer> cir) {
        int luminance = TCClientPacketHandler.getWaterLuminance(entity);
        if (luminance > cir.getReturnValue()) {
            cir.setReturnValue(luminance);
        }
    }
}
