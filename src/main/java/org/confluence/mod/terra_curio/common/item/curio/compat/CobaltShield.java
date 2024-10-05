package org.confluence.mod.terra_curio.common.item.curio.compat;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.config.ModConfig;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CobaltShield extends BaseCurioItem {

    public CobaltShield() {
        super("test3",builder -> builder.rarity(ModRarity.GREEN).build());
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return ImmutableMultimap.of(
            Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(TerraCurio.asResource("cobalt_shield_resistance"), ModConfig.COBALT_SHIELD_RESISTANCE.get(), AttributeModifier.Operation.ADD_VALUE),
            Attributes.ARMOR, new AttributeModifier(TerraCurio.asResource("cobalt_shield_armor"), ModConfig.COBALT_SHIELD_ARMOR.get(), AttributeModifier.Operation.ADD_VALUE)
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId()));
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId() + ".1"));
    }

}
