package org.confluence.mod.terra_curio.common.item.curio;

import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.compress.utils.Lists;
import org.confluence.mod.terra_curio.common.component.EffectImmunities;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.init.ModDataComponentTypes;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BaseCurioItem extends Item implements ICurioItem {
    private int JeiInformationCount;

    public BaseCurioItem(Function<Builder,Properties> properties) {
        super(properties.apply(new Builder()));
    }

    public BaseCurioItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId()));
    }

    public int getJeiInformationCount() {
        return this.JeiInformationCount;
    }

    public static class Builder {
        private ItemAttributeModifiers attributeModifiers;
        private EffectImmunities effectImmunities;
        private ModRarity rarity;

        public Builder attributeModifier(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
            this.attributeModifiers.withModifierAdded(attribute, modifier, slot);
            return this;
        }

        public Builder effectImmunities(Holder<MobEffect>... effectImmunities) {
            this.effectImmunities = EffectImmunities.of(List.of(effectImmunities));
            return this;
        }


        public Builder rarity(ModRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Properties build() {
            return new Properties()
                    .component(ModDataComponentTypes.MOD_RARITY,rarity)
                    .component(ModDataComponentTypes.EFFECT_IMMUNITIES, effectImmunities)
                    .component(DataComponents.ATTRIBUTE_MODIFIERS, attributeModifiers)
                    .stacksTo(1
            );
        }

    }
}
