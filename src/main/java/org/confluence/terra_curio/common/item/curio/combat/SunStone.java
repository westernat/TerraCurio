package org.confluence.terra_curio.common.item.curio.combat;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.common.LibEffects;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.lib.util.LibDateUtils;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class SunStone extends BaseCurioItem {
    public SunStone() {
        super(builder("sun_stone").rarity(ModRarity.LIME));
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
            LibEffects.healPerSecond(living, 0.4F);
        }
    }
}
