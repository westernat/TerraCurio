package org.confluence.terra_curio.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.util.TCUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    @Inject(method = "performShooting", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/ChargedProjectiles;isEmpty()Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void setProjectilesBack(Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, float velocity, float inaccuracy, LivingEntity target, CallbackInfo ci, ServerLevel serverlevel, ChargedProjectiles chargedprojectiles) {
        if (!TCUtils.magicQuiver$shouldConsume(shooter)) {
            weapon.set(DataComponents.CHARGED_PROJECTILES, chargedprojectiles);
        }
    }
}
