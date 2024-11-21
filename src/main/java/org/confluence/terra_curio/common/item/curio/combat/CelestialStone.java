package org.confluence.terra_curio.common.item.curio.combat;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCAttributes;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import top.theillusivec4.curios.api.SlotContext;

public class CelestialStone extends BaseCurioItem {
    public CelestialStone() {
        super(builder("celestial_stone").rarity(ModRarity.LIME)
                .jeiInfos(0)
                .attribute(Attributes.ATTACK_SPEED, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ATTACK_DAMAGE, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ARMOR, 4.0, AttributeModifier.Operation.ADD_VALUE)
                .attribute(Attributes.BLOCK_BREAK_SPEED, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(TCAttributes.getCriticalChance(), 0.02, AttributeModifier.Operation.ADD_VALUE)
                .attribute(TCAttributes.getRangedDamage(), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(TCAttributes.getMagicDamage(), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        TCEffects.healPerSecond(slotContext.entity(), 2.0F);
    }
}
