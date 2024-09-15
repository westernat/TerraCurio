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
import org.confluence.mod.terra_curio.common.misc.ModAttributes;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class DestroyerEmblem extends BaseCurioItem {

    public DestroyerEmblem() {
        super(builder -> builder.rarity(ModRarity.LIME).build());
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return ImmutableMultimap.of(
            Attributes.ATTACK_DAMAGE, new AttributeModifier(TerraCurio.asResource("destroyer_emblem_damage"), ModConfig.DESTROYER_EMBLEM_DAMAGE.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
            ModAttributes.CRIT_CHANCE, new AttributeModifier(TerraCurio.asResource("destroyer_emblem_critical"), ModConfig.DESTROYER_EMBLEM_CRITICAL_CHANCE.get(), AttributeModifier.Operation.ADD_VALUE),
            ModAttributes.RANGED_DAMAGE, new AttributeModifier(TerraCurio.asResource("destroyer_emblem_ranged"), ModConfig.DESTROYER_EMBLEM_RANGED.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
            ModAttributes.MAGIC_DAMAGE, new AttributeModifier(TerraCurio.asResource("destroyer_emblem_magic"), ModConfig.DESTROYER_EMBLEM_MAGIC.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId()));
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId() + ".1"));
    }
}
