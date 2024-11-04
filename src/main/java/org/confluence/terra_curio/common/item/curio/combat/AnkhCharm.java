package org.confluence.terra_curio.common.item.curio.combat;

import net.minecraft.world.effect.MobEffects;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.util.TCUtils;

public class AnkhCharm extends BaseCurioItem {
    public AnkhCharm() {
        super(getBuilder().initialize());
    }

    private static Builder getBuilder() {
        return TCUtils.forConfluence$ModifyExpression(builder("ankh_charm").effectImmunities(
                MobEffects.POISON, MobEffects.WITHER,
                MobEffects.WEAKNESS, MobEffects.HUNGER,
                MobEffects.BLINDNESS, MobEffects.DARKNESS,
                MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION,
                MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION
        ).rarity(ModRarity.LIGHT_PURPLE));
    }
}
