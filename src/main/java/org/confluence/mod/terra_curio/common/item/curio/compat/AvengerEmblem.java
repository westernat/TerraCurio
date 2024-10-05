package org.confluence.mod.terra_curio.common.item.curio.compat;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;

import java.util.List;

public class AvengerEmblem extends BaseCurioItem {

    private static ImmutableMultimap<Attribute, AttributeModifier> ATTRIBUTES;

    public AvengerEmblem() {
        super("test",builder -> builder.rarity(ModRarity.PINK).build());
    }

    //todo ModConfigs
/*
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        if (ATTRIBUTES == null) {
            ATTRIBUTES = ImmutableMultimap.of(
                    Attributes.ATTACK_DAMAGE, new AttributeModifier(TerraCurio.SPACE("avenger_emblem") , ModConfigs.AVENGER_EMBLEM_DAMAGE.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                    ModAttributes.RANGED_DAMAGE, new AttributeModifier(TerraCurio.SPACE("avenger_emblem") , ModConfigs.AVENGER_EMBLEM_RANGED.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                    ModAttributes.MAGIC_DAMAGE, new AttributeModifier(TerraCurio.SPACE("avenger_emblem") , ModConfigs.AVENGER_EMBLEM_MAGIC.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            );
        }
        return ATTRIBUTES;
    }
    */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
