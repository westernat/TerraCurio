package org.confluence.terra_curio.common.item.curio.combat;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.util.TCUtils;

public class AnkhShield extends BaseCurioItem {
    public AnkhShield() {
        super(getBuilder().initialize());
    }

    private static Builder getBuilder() {
        return TCUtils.forConfluence$ModifyExpression(builder("ankh_shield").effectImmunities(
                MobEffects.POISON, MobEffects.WITHER,
                MobEffects.WEAKNESS, MobEffects.HUNGER,
                MobEffects.BLINDNESS, MobEffects.DARKNESS,
                MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION,
                MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION
        ).attribute(
                Attributes.KNOCKBACK_RESISTANCE, "knockback_resistance", 1.0, AttributeModifier.Operation.ADD_VALUE
        ).attribute(
                Attributes.ARMOR, "armor", 4.0, AttributeModifier.Operation.ADD_VALUE
        ).rarity(ModRarity.LIME));
    }
}
