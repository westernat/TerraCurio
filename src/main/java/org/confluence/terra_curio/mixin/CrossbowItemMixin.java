package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.util.TCUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    @WrapWithCondition(method = "onCrossbowShot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;clearChargedProjectiles(Lnet/minecraft/world/item/ItemStack;)V"))
    private static boolean setProjectilesBack(ItemStack crossbowStack, @Local(argsOnly = true, ordinal = 0) LivingEntity shooter) {
        return !TCUtils.magicQuiver$shouldSkip(shooter, 0);
    }
}
