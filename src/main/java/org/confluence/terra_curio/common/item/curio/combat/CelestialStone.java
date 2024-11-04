package org.confluence.terra_curio.common.item.curio.combat;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCAttributes;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.util.TCUtils;
import top.theillusivec4.curios.api.SlotContext;

public class CelestialStone extends BaseCurioItem {
    public CelestialStone() {
        super(getBuilder().initialize());
    }

    private static Builder getBuilder() {
        return TCUtils.forConfluence$ModifyExpression(builder("celestial_stone").rarity(ModRarity.LIME) // todo mixin here
                .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ARMOR, "armor", 4.0, AttributeModifier.Operation.ADD_VALUE)
                .attribute(Attributes.BLOCK_BREAK_SPEED, "block_break_speed", 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(TCAttributes.getCriticalChance(), "critical_chance", 0.02, AttributeModifier.Operation.ADD_VALUE)
                .attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(TCAttributes.getMagicDamage(), "magic_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        TCEffects.healPerSecond(slotContext.entity(), 2.0F);
    }
}
