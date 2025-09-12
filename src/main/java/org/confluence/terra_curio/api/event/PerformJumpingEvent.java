package org.confluence.terra_curio.api.event;

import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PerformJumpingEvent extends PlayerEvent implements ICancellableEvent {
    private boolean canPerform = true;

    public PerformJumpingEvent(LocalPlayer player) {
        super(player);
    }

    public boolean isCanPerform() {
        return canPerform;
    }

    public void setCanPerform(boolean canPerform) {
        this.canPerform = canPerform;
    }
}
