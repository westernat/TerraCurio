package org.confluence.terra_curio.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class FishingPowerModificationEvent extends LivingEvent {
    private final float original;
    private float neoValue;

    public FishingPowerModificationEvent(LivingEntity livingEntity, float original) {
        super(livingEntity);
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
