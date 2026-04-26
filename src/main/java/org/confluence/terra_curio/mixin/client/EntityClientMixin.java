package org.confluence.terra_curio.mixin.client;

import net.minecraft.world.entity.Entity;
import org.confluence.lib.mixed.SelfGetter;
import org.confluence.terra_curio.mixed.IEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityClientMixin implements SelfGetter<Entity> {
    @Inject(method = "getEyeHeight()F", at = @At("RETURN"), cancellable = true)
    private void eyeHeight(CallbackInfoReturnable<Float> cir) {
        if (IEntity.of(confluence$self()).terra_curio$isShouldRot()) {
            cir.setReturnValue(IEntity.of(confluence$self()).terra_curio$getDimensionHeight() * 0.15F);
        }
    }
}
