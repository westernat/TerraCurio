package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.confluence.terra_curio.common.init.TCAttributes;
import org.confluence.terra_curio.mixinauxi.SelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements SelfGetter<AbstractArrow> {
    @ModifyExpressionValue(method = "doKnockback", at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 0))
    private float modify1(float original) {
        return TCAttributes.applyArrowKnockback(self().getOwner(), original);
    }

    @ModifyExpressionValue(method = "doKnockback", at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 1))
    private float modify2(float original) {
        return TCAttributes.applyArrowKnockback(self().getOwner(), original);
    }
}
