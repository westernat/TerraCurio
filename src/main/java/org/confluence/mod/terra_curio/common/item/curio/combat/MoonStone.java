package org.confluence.mod.terra_curio.common.item.curio.combat;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.effect.ModEffects;
import org.confluence.mod.terra_curio.common.init.ModAttributes;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import top.theillusivec4.curios.api.SlotContext;

public class MoonStone extends BaseCurioItem {
    public MoonStone() {
        super(getBuilder());
    }

    private static Builder getBuilder() {
        return builder("moon_stone").rarity(ModRarity.PINK)
                .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ARMOR, "armor", 4.0, AttributeModifier.Operation.ADD_VALUE)
                .attribute(Attributes.BLOCK_BREAK_SPEED, "block_break_speed", 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(ModAttributes.getCriticalChance(), "critical_chance", 0.02, AttributeModifier.Operation.ADD_VALUE)
                .attribute(ModAttributes.getRangedDamage(), "ranged_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(ModAttributes.getMagicDamage(), "magic_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        return living != null && living.level().getDayTime() % 24000 > 12000 ? super.getAttributeModifiers(slotContext, id, stack) : EMPTY_ATTRIBUTE;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        if (living.level().getDayTime() % 24000 > 12000) {
            ModEffects.healPerSecond(living, 2.0F);
        }
    }
}
