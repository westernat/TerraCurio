package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.confluence.terra_curio.util.TCUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    @Inject(method = "performShooting", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/ChargedProjectiles;isEmpty()Z"))
    private void setProjectilesBack(CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) LivingEntity shooter, @Local(argsOnly = true) ItemStack weapon, @Local ChargedProjectiles chargedprojectiles) {
        if (!TCUtils.magicQuiver$shouldConsume(shooter)) {
            weapon.set(DataComponents.CHARGED_PROJECTILES, chargedprojectiles);
        }
    }
}
