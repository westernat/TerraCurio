package org.confluence.mod.terra_curio.common.item.curio.combat;

import net.minecraft.world.effect.MobEffects;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;

public class AnkhCharm extends BaseCurioItem {
    public AnkhCharm() {
        super(getBuilder());
    }

    /**
     * Injected by Confluence
     */
    private static Builder getBuilder() {
        return builder("ankh_charm").effectImmunities(
                MobEffects.POISON, MobEffects.WITHER,
                MobEffects.WEAKNESS, MobEffects.HUNGER,
                MobEffects.BLINDNESS, MobEffects.DARKNESS,
                MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION,
                MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION
        ).rarity(ModRarity.LIGHT_PURPLE).initialize();
    }
}
