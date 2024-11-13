package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.confluence.terra_curio.common.init.TCDataMaps;
import org.confluence.terra_curio.util.CuriosUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @WrapOperation(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;findTotem(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack replaceTotem(Player player, Operation<ItemStack> original) {
        Optional<ItemStack> curio = CuriosUtils.findCurio(player, (Predicate<ItemStack>) itemStack -> {
            AccessoriesComponent component = itemStack.get(TCDataComponentTypes.ACCESSORIES);
            if (component != null || (component = itemStack.getItemHolder().getData(TCDataMaps.ACCESSORIES)) != null){
                return component.contains(ValueType.TOTEM$WITH$COOLDOWN);
            }
            return false;
        });
        return curio.orElseGet(() -> original.call(player));
    }
}
