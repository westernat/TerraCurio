package org.confluence.mod.terra_curio.common.item.curio.compat;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.component.EffectImmunities;
import org.confluence.mod.terra_curio.common.config.ModConfig;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class AnkhShield extends BaseCurioItem {

    public AnkhShield() {
        super(builder -> builder.effectImmunities(MobEffects.POISON, MobEffects.HUNGER, MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS,
            MobEffects.LEVITATION, MobEffects.WITHER, MobEffects.DARKNESS, MobEffects.BLINDNESS,
            MobEffects.CONFUSION, MobEffects.MOVEMENT_SLOWDOWN).build());
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return ImmutableMultimap.of(
            Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(TerraCurio.asResource("ankh_shield_knockback_resistance"), ModConfig.ANKH_SHIELD_RESISTANCE.get(), AttributeModifier.Operation.ADD_VALUE),
            Attributes.ARMOR, new AttributeModifier(TerraCurio.asResource("ankh_shield_armor"), ModConfig.ANKH_SHIELD_ARMOR.get(), AttributeModifier.Operation.ADD_VALUE)
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId()));
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId() + ".1"));
    }

}
