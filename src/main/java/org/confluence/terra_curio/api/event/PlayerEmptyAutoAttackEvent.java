package org.confluence.terra_curio.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class PlayerEmptyAutoAttackEvent extends PlayerEvent {
    private final ItemStack itemStack;

    public PlayerEmptyAutoAttackEvent(Player player, ItemStack itemStack) {
        super(player);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
