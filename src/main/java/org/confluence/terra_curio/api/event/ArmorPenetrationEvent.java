package org.confluence.terra_curio.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.ApiStatus;

@Deprecated(since = "1.2.4", forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "1.4.0")
public class ArmorPenetrationEvent extends Event {
    private final org.confluence.lib.api.event.ArmorPenetrationEvent e;

    public ArmorPenetrationEvent(org.confluence.lib.api.event.ArmorPenetrationEvent e) {
        this.e = e;
    }

    public LivingEntity getEntity() {
        return e.getEntity();
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
        NeoForge.EVENT_BUS.addListener(org.confluence.lib.api.event.ArmorPenetrationEvent.class, e -> NeoForge.EVENT_BUS.post(new ArmorPenetrationEvent(e)));
    }
}
