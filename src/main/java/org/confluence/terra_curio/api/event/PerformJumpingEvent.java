package org.confluence.terra_curio.api.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PerformJumpingEvent extends PlayerEvent implements ICancellableEvent {
    private boolean canPerform = true;

    public PerformJumpingEvent(Player player) {
        super(player);
    }

    public boolean isCanPerform() {
        return canPerform;
    }

    public void setCanPerform(boolean canPerform) {
        this.canPerform = canPerform;
    }
}
