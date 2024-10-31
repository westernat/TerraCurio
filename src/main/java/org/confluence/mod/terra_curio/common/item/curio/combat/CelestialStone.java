package org.confluence.mod.terra_curio.common.item.curio.combat;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.effect.ModEffects;
import org.confluence.mod.terra_curio.common.init.ModAttributes;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import top.theillusivec4.curios.api.SlotContext;

public class CelestialStone extends BaseCurioItem {
    public CelestialStone() {
        super(getBuilder().initialize());
    }

    private static Builder getBuilder() {
        return builder("celestial_stone").rarity(ModRarity.LIME)
                .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ARMOR, "armor", 4.0, AttributeModifier.Operation.ADD_VALUE)
                .attribute(Attributes.BLOCK_BREAK_SPEED, "block_break_speed", 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(ModAttributes.getCriticalChance(), "critical_chance", 0.02, AttributeModifier.Operation.ADD_VALUE)
                .attribute(ModAttributes.getRangedDamage(), "ranged_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(ModAttributes.getMagicDamage(), "magic_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ModEffects.healPerSecond(slotContext.entity(), 2.0F);
    }
}
