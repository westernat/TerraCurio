package org.confluence.terra_curio.common.item.curio.combat;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCAttributes;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import top.theillusivec4.curios.api.SlotContext;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;

public class CelestialShell extends BaseCurioItem {
    public static final ResourceLocation ID = TerraCurio.asResource("celestial_shell_night");
    private static final ImmutableMultimap<Holder<Attribute>, AttributeModifier> NIGHT = Util.make(ImmutableMultimap.<Holder<Attribute>, AttributeModifier>builder(), builder -> {
        builder.put(TCAttributes.getCriticalChance(), new AttributeModifier(ID, 0.02, ADD_VALUE));
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ID, 0.051, ADD_MULTIPLIED_TOTAL));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ID, 0.051, ADD_MULTIPLIED_TOTAL));
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ID, 0.05, ADD_MULTIPLIED_TOTAL));
        builder.put(Attributes.ARMOR, new AttributeModifier(ID, 3.0, ADD_VALUE));
        builder.put(Attributes.JUMP_STRENGTH, new AttributeModifier(ID, 0.1, ADD_MULTIPLIED_TOTAL));
    }).build();

    public CelestialShell(Builder builder) {
        super(builder);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = super.getAttributeModifiers(slotContext, id, stack);
        if (living != null && !living.isInWaterOrBubble() && living.level().getDayTime() % 24000L > 12000L) {
            return ImmutableMultimap.<Holder<Attribute>, AttributeModifier>builder().putAll(attributeModifiers).putAll(NIGHT).build();
        }
        return attributeModifiers;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        boolean isNight = slotContext.entity().level().getGameTime() % 24000L > 12000L;
        TCEffects.healPerSecond(slotContext.entity(), isNight ? 1.5F : 1.0F);
    }
}
