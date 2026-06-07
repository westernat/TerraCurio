package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import org.confluence.terra_curio.network.s2c.InfoCurioCheckPacketS2C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
public abstract class InventoryChangeTriggerMixin {
    @Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    private void onInventoryChanged(CallbackInfo ci, @Local(argsOnly = true) ServerPlayer player, @Local(argsOnly = true) Inventory inventory) {
        InfoCurioCheckPacketS2C.sendToClient(player, inventory);
    }
}
