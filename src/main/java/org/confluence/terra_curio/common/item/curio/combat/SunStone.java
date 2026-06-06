package org.confluence.terra_curio.common.item.curio.combat;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes.PortAttributesExtension;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.common.LibAttributes;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.lib.util.LibDateUtils;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class SunStone extends BaseCurioItem {
    public SunStone() {
        super(builder("sun_stone").rarity(ModRarity.LIME)
                .attribute(Attributes.ATTACK_SPEED, 0.1, PortAttributeModifier.PortOperation.ADD_MULTIPLIED_TOTAL)
                .attribute(LibAttributes.getAttackDamage().value(), 0.1, PortAttributeModifier.PortOperation.ADD_MULTIPLIED_TOTAL)
                .attribute(Attributes.ARMOR, 2.0, PortAttributeModifier.PortOperation.ADD_VALUE)
                .attribute(PortAttributesExtension.blockBreakSpeed().value(), 0.15, PortAttributeModifier.PortOperation.ADD_MULTIPLIED_TOTAL)
                .attribute(LibAttributes.getCriticalChance().value(), 0.02, PortAttributeModifier.PortOperation.ADD_VALUE)
                .attribute(LibAttributes.getRangedDamage().value(), 0.1, PortAttributeModifier.PortOperation.ADD_MULTIPLIED_TOTAL)
                .attribute(LibAttributes.getMagicDamage().value(), 0.1, PortAttributeModifier.PortOperation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID id, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        return living != null && LibDateUtils.isDay(living.level()) ? super.getAttributeModifiers(slotContext, id, stack) : EMPTY_ATTRIBUTE;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        if (LibDateUtils.isDay(living.level())) {
            TCEffects.healPerSecond(living, 0.4F);
        }
    }
}
