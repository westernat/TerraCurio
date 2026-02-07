package org.confluence.terra_curio.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.bus.api.Event;

public class ArmorPenetrationEvent extends Event {
    private final DamageSource damageSource;
    private final float armorValue;
    private float penetration;

    public ArmorPenetrationEvent(DamageSource damageSource, float armorValue) {
        this.damageSource = damageSource;
        this.armorValue = armorValue;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    public float getArmorValue() {
        return armorValue;
    }

    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }

    public float getPenetration() {
        return penetration;
    }
}
