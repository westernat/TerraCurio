package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.mixed.IEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// todo 反转AI
@Mixin(MoveControl.class)
public abstract class MoveControlMixin {
    @Shadow
    @Final
    protected Mob mob;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getY()D", ordinal = 0))
    private double modifyY(double y) {
        IEntity entity = IEntity.of(mob);
        if (entity.terra_curio$isShouldRot()) {
            return y + entity.terra_curio$getDimensionHeight() - 1;
        }
        return y;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/control/MoveControl;rotlerp(FFF)F"))
    private float modifyRot(float original) {
        if (IEntity.of(mob).terra_curio$isShouldRot()) {
            return TerraCurio.hotswap(original);
        }
        return original;
    }
}
