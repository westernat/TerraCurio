package org.confluence.terra_curio.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

/// @see org.confluence.lib.event.ArmorPenetrationEvent
/// @deprecated
public class ArmorPenetrationEvent extends Event {
    private final org.confluence.lib.event.ArmorPenetrationEvent e;

    public ArmorPenetrationEvent(org.confluence.lib.event.ArmorPenetrationEvent e) {
        this.e = e;
    }

    public DamageSource getDamageSource() {
        return e.getDamageSource();
    }

    public float getArmorValue() {
        return e.getArmorValue();
    }

    public void setPenetration(float penetration) {
        e.setPenetration(penetration);
    }

    public float getPenetration() {
        return e.getPenetration();
    }

    static {
        NeoForge.EVENT_BUS.addListener(org.confluence.lib.event.ArmorPenetrationEvent.class, e -> NeoForge.EVENT_BUS.post(new ArmorPenetrationEvent(e)));
    }
}
