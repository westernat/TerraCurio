package org.confluence.terra_curio.common.item.curio.combat;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes.PortAttributesExtension;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.common.LibAttributes;
import org.confluence.lib.util.LibDateUtils;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.util.CuriosUtils;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class CelestialShell extends BaseCurioItem implements ICosmetic {
    public static final UUID ID = UUID.nameUUIDFromBytes("celestial_shell_night".getBytes());

    private static final Multimap<Attribute, AttributeModifier> NIGHT = Util.make(ImmutableMultimap.<Attribute, AttributeModifier>builder(), builder -> {
        builder.put(LibAttributes.getCriticalChance().value(), new AttributeModifier(ID, "celestial_shell_night", 0.02, AttributeModifier.Operation.ADDITION));
        builder.put(LibAttributes.getAttackDamage().value(), new AttributeModifier(ID, "celestial_shell_night", 0.051, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ID, "celestial_shell_night", 0.051, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ID, "celestial_shell_night", 0.05, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(Attributes.ARMOR, new AttributeModifier(ID, "celestial_shell_night", 3.0, AttributeModifier.Operation.ADDITION));
        builder.put(PortAttributesExtension.jumpStrength().value(), new AttributeModifier(ID, "celestial_shell_night", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }).build();

    public CelestialShell(Builder builder) {
        super(builder);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        return CuriosUtils.noSameCurio(living, this) &&
                CuriosUtils.noSameCurio(living, TCItems.CELESTIAL_STONE.get()) &&
                CuriosUtils.noSameCurio(living, ICosmetic.class);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID id, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        Multimap<Attribute, AttributeModifier> attributeModifiers = super.getAttributeModifiers(slotContext, id, stack);
        if (living != null && !living.isInWaterOrBubble() && LibDateUtils.isNight(living.level())) {
            return ImmutableMultimap.<Attribute, AttributeModifier>builder().putAll(attributeModifiers).putAll(NIGHT).build();
        }
        return attributeModifiers;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        boolean isNight = LibDateUtils.isNight(slotContext.entity().level());
        TCEffects.healPerSecond(slotContext.entity(), isNight ? 0.3F : 0.2F);
    }
}
