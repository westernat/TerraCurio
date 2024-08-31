package org.confluence.mod.terra_curio.common.item.curio;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.config.ModConfig;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AnkhShield extends BaseCurioItem{
    public static final UUID RESISTANCE_UUID = UUID.fromString("B9B53B92-98E3-05EF-9D6C-85D8E110B2DC");
    public static final UUID ARMOR_UUID = UUID.fromString("147A80CE-4C69-8073-9340-F851F5BFC622");
    public AnkhShield() {
        super(List.of( MobEffects.POISON,MobEffects.HUNGER,MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS,
                MobEffects.LEVITATION,MobEffects.WITHER,MobEffects.DARKNESS,MobEffects.BLINDNESS,
                MobEffects.CONFUSION,MobEffects.MOVEMENT_SLOWDOWN),2);
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
