package org.confluence.terra_curio.item.curio.combat;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.util.CuriosUtils;

public interface IFireAttack {
    static void apply(LivingEntity victim, LivingEntity attacker) {
        if (CuriosUtils.hasCurio(attacker, IFireAttack.class)) {
            float f = attacker.getRandom().nextFloat();
            int time;
            if (f < 0.25F) {
                time = 6;
            } else if (f < 0.375F) {
                time = 4;
            } else {
                time = 2;
            }
            victim.setSecondsOnFire(time);
        }
    }

    Component TOOLTIP = Component.translatable("curios.tooltip.fire_attack");
}
