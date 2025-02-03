package org.confluence.terra_curio.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.CommonHooks;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.util.TCUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CommonHooks.class, remap = false)
public abstract class CommonHooksMixin {
    @Inject(method = "canMobEffectBeApplied(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/effect/MobEffectInstance;)Z", at = @At("RETURN"), cancellable = true)
    private static void deny(LivingEntity entity, MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if (effect != null && TCUtils.getAccessoriesValue(entity, TCItems.EFFECT$IMMUNITIES).contains(effect.getEffect())) {
                cir.setReturnValue(false);
            }
        }
    }
}
