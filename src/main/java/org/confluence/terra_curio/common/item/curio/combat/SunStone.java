package org.confluence.terra_curio.common.item.curio.combat;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCAttributes;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.util.TCUtils;
import top.theillusivec4.curios.api.SlotContext;

public class SunStone extends BaseCurioItem {
    public SunStone() {
        super(getBuilder());
    }

    private static Builder getBuilder() {
        return TCUtils.forConfluence$ModifyExpression(builder("sun_stone").rarity(ModRarity.LIME) // todo mixin here
                .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ARMOR, "armor", 4.0, AttributeModifier.Operation.ADD_VALUE)
                .attribute(Attributes.BLOCK_BREAK_SPEED, "block_break_speed", 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(TCAttributes.getCriticalChance(), "critical_chance", 0.02, AttributeModifier.Operation.ADD_VALUE)
                .attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .attribute(TCAttributes.getMagicDamage(), "magic_damage", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        return living != null && living.level().getDayTime() % 24000 < 12000 ? super.getAttributeModifiers(slotContext, id, stack) : EMPTY_ATTRIBUTE;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        if (living.level().getDayTime() % 24000 < 12000) {
            TCEffects.healPerSecond(living, 2.0F);
        }
    }
}
