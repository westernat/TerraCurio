package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TargetingConditions.class)
public abstract class TargetingConditionsMixin {
    @ModifyReturnValue(method = "test", at = @At(value = "RETURN", ordinal = 7))
    private boolean unTest(
            boolean original,
            @Local(argsOnly = true, name = "targeter") @Nullable LivingEntity targeter,
            @Local(argsOnly = true, name = "target") LivingEntity target
    ) {
        if (original && targeter != null && targeter.getType().builtInRegistryHolder().is(TCUtils.getValue(target, TCItems.MOB$IGNORE))) {
            return false;
        }
        return original;
    }
}
