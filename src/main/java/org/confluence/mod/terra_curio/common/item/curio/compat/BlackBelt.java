package org.confluence.mod.terra_curio.common.item.curio.compat;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.mod.terra_curio.common.misc.ModAttributes;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class BlackBelt extends BaseCurioItem {

    public BlackBelt() {
        super("test1",builder -> builder.rarity(ModRarity.LIME).build());
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return ImmutableMultimap.of(
            ModAttributes.DODGE_CHANCE, new AttributeModifier(TerraCurio.asResource("blackbelt_dodge_chance"), 0.1, AttributeModifier.Operation.ADD_VALUE)
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId()));
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId() + ".1"));
    }
}
