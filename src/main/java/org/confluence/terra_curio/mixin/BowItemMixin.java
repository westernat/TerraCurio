package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import org.confluence.terra_curio.util.TCUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BowItem.class)
public abstract class BowItemMixin {
    @ModifyExpressionValue(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ArrowItem;isInfinite(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Z", remap = false))
    private boolean isInfinity(boolean original, @Local(argsOnly = true) LivingEntity shooter, @Local(argsOnly = true) int timeLeft) {
        if (shooter.level().isClientSide) return original;
        return original || TCUtils.magicQuiver$shouldSkip(shooter, timeLeft);
    }
}
