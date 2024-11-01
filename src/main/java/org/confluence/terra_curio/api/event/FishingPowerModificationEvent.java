package org.confluence.terra_curio.api.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class FishingPowerModificationEvent extends PlayerEvent {
    private final float original;
    private float neoValue;

    public FishingPowerModificationEvent(Player player, float original) {
        super(player);
        this.original = original;
        this.neoValue = original;
    }

    public float getOriginal() {
        return original;
    }

    public void setNeoValue(float neoValue) {
        this.neoValue = neoValue;
    }

    public float getNeoValue() {
        return neoValue;
    }
}
