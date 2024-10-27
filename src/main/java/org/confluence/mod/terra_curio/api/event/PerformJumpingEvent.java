package org.confluence.mod.terra_curio.api.event;

import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@OnlyIn(Dist.CLIENT)
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
