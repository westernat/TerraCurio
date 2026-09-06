package org.confluence.terra_curio.common.item.curio.combat;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.common.item.curio.NightBonusCurioItem;
import org.confluence.terra_curio.util.CuriosUtils;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class MoonShell extends NightBonusCurioItem implements ICosmetic {
    public static final UUID ID = UUID.nameUUIDFromBytes("moon_shell".getBytes());
    private static final Multimap<Attribute, AttributeModifier> UNDER_WATER = ImmutableMultimap.of(
            Attributes.SUBMERGED_MINING_SPEED.value(), new AttributeModifier(ID, "moon_shell", 0.8, AttributeModifier.Operation.ADDITION)
    );

    public MoonShell(Builder builder) {
        super(0.1F, builder);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        return CuriosUtils.noSameCurio(living, this) && CuriosUtils.noSameCurio(living, ICosmetic.class);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID id, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        if (living != null && living.isInWaterOrBubble()) {
            return UNDER_WATER;
        }
        return super.getAttributeModifiers(slotContext, id, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!slotContext.entity().isInWaterOrBubble()) {
            super.curioTick(slotContext, stack);
        }
    }
}
