package org.confluence.terra_curio.common.item.curio;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.AttributeModifiersValue;
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.confluence.terra_curio.common.init.TCDataMaps;
import org.confluence.terra_curio.util.CuriosUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BaseCurioItem extends Item implements ICurioItem {
    protected static final List<Component> EMPTY_TOOLTIP = List.of();
    protected static final ImmutableMultimap<Holder<Attribute>, AttributeModifier> EMPTY_ATTRIBUTE = ImmutableMultimap.of();
    protected static final Consumer<Builder> NO_BUILDER = builder -> {};
    protected Builder builder;

    protected BaseCurioItem(Builder builder) {
        super(builder.initialize().properties);
        this.builder = builder;
    }

    protected BaseCurioItem(Properties properties) {
        super(properties);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        if (slotContext.entity() != null) {
            AccessoriesComponent component;
            AttributeModifiersValue value;
            if ((component = stack.getItemHolder().getData(TCDataMaps.ACCESSORIES)) != null && (value = component.get(ValueType.ATTRIBUTES)) != null) {
                return value.get();
            }
        }
        return builder == null ? EMPTY_ATTRIBUTE : builder.attributes;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        boolean b = builder == null;
        if (b || builder.hasToolTip) {
            tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId()));
            if (!b) tooltipComponents.addAll(builder.additionTip);
        }
    }

    public int getJeiInformationCount() {
        return builder.jeiInformationCount;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return canEquip(slotContext, stack);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosUtils.noSameCurio(slotContext.entity(), this);
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return builder.makePiglinsNeutral;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(getDescriptionId()).withStyle(style -> style.withColor(stack.get(TCDataComponentTypes.MOD_RARITY).getColor()));
    }

    public static Builder builder(String name, Properties properties) {
        return new Builder(name, properties);
    }

    public static Builder builder(String name) {
        return new Builder(name, new Properties());
    }

    public static class Builder {
        private final String name;
        private final Properties properties;
        private final ResourceLocation defaultId;

        private final List<Component> additionTip = new ArrayList<>();
        private boolean hasToolTip = true;
        private transient ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> attributesBuilder = ImmutableMultimap.builder();
        private ImmutableMultimap<Holder<Attribute>, AttributeModifier> attributes;
        private ModRarity rarity = ModRarity.BLUE;
        private int jeiInformationCount = 0;
        private boolean makePiglinsNeutral;

        Builder(String name, Properties properties) {
            this.name = name;
            this.properties = properties;
            this.defaultId = TerraCurio.asResource(name);
        }

        public Builder makesPiglinsNeutral() {
            this.makePiglinsNeutral = true;
            return this;
        }

        public <T> Builder component(Supplier<DataComponentType<T>> type, T value) {
            properties.component(type, value);
            return this;
        }

        public Builder attribute(Holder<Attribute> attribute, String path, double amount, AttributeModifier.Operation operation) {
            attributesBuilder.put(attribute, new AttributeModifier(TerraCurio.asResource(name + "_" + path), amount, operation));
            return this;
        }

        public Builder attribute(Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation) {
            attributesBuilder.put(attribute, new AttributeModifier(defaultId, amount, operation));
            return this;
        }

        public Builder rarity(ModRarity rarity) {
            this.rarity = rarity;
            if (rarity != ModRarity.GRAY && rarity != ModRarity.WHITE) {
                properties.fireResistant();
            }
            return this;
        }

        public Builder accessories(AccessoriesComponent component, AccessoriesComponent... components) {
            if (components.length == 0) {
                properties.component(TCDataComponentTypes.ACCESSORIES, component);
            } else {
                Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> map = new Hashtable<>(component.types());
                for (AccessoriesComponent component1 : components) {
                    map.putAll(component1.types());
                }
                properties.component(TCDataComponentTypes.ACCESSORIES, new AccessoriesComponent(map));
            }
            return this;
        }

        public Builder tooltip(String str) {
            if (!hasToolTip) throw new IllegalArgumentException("Can not add tooltip when noTooltip() invoked!");
            additionTip.add(Component.translatable("tooltip." + str));
            return this;
        }

        public Builder jeiInformationCount(int count) {
            this.jeiInformationCount = count;
            return this;
        }

        public Builder noTooltip() {
            additionTip.clear();
            this.hasToolTip = false;
            return this;
        }

        @ApiStatus.Internal
        public Builder initialize() {
            properties.stacksTo(1).component(TCDataComponentTypes.MOD_RARITY, rarity);
            this.attributes = attributesBuilder.build();
            this.attributesBuilder = null;
            return this;
        }

        @ApiStatus.Internal
        public ImmutableMultimap<Holder<Attribute>, AttributeModifier> getAttributes() {
            return attributes;
        }

        public BaseCurioItem build() {
            return new BaseCurioItem(this);
        }
    }
}
