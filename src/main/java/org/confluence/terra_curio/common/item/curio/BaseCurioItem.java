package org.confluence.terra_curio.common.item.curio;

import PortLib.extensions.net.minecraft.core.Holder.PortHolderExtension;
import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes.PortAttributesExtension;
import PortLib.extensions.net.minecraft.world.item.Item.PortItemExtension;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_curio.TCStartupConfigs;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.AttributeModifiersValue;
import org.confluence.terra_curio.api.primitive.ComponentsValue;
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.component.PrimitiveValueComponent;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.confluence.terra_curio.common.init.TCDataMaps;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.mixed.ILivingEntity;
import org.confluence.terra_curio.util.CuriosUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.mesdag.particlestorm.particle.MolangParticleEngine;
import org.mesdag.particlestorm.particle.ParticleEmitter;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.*;

public class BaseCurioItem extends Item implements ICurioItem {
    protected static final Multimap<Attribute, AttributeModifier> EMPTY_ATTRIBUTE = ImmutableMultimap.of();

    protected Builder builder;

    public BaseCurioItem(Builder builder) {
        super(builder.initialize().properties);
        this.builder = builder;
    }

    public BaseCurioItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (builder == null || builder.particle == null) return;
        LivingEntity living = slotContext.entity();
        if (living.level().isClientSide) {
            ILivingEntity iLiving = (ILivingEntity) living;
            ParticleEmitter emitter = iLiving.terra_curio$getOrCreateParticleEmitters().get(builder.particle);
            if (emitter == null || emitter.isRemoved()) {
                Map<ResourceLocation, ParticleEmitter> emitters = iLiving.terra_curio$getOrCreateParticleEmitters();
                emitter = new ParticleEmitter(living.level(), living.position(), builder.particle);
                emitter.attachEntity(living);
                emitter.hideOutline = true;
                MolangParticleEngine.INSTANCE.addEmitter(emitter);
                emitters.put(builder.particle, emitter);
            }
            particleTick(living, emitter, builder.particle);
            emitter.active &= slotContext.visible();
        }
    }

    protected void particleTick(LivingEntity living, ParticleEmitter emitter, ResourceLocation particle) {
        if (emitter.isRemoved()) {
            ILivingEntity.of(living).terra_curio$getOrCreateParticleEmitters().remove(particle);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return getAttributeModifiers(stack);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        PrimitiveValueComponent component = PortHolderExtension.getData(stack.getItemHolder(), TCDataMaps.ACCESSORIES);
        AttributeModifiersValue value;
        if (component != null && (value = component.get(TCItems.ATTRIBUTES)) != null) {
            return value.getOldValue();
        }
        return builder == null ? EMPTY_ATTRIBUTE : builder.attributes;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        PrimitiveValueComponent component = PortHolderExtension.getData(stack.getItemHolder(), TCDataMaps.ACCESSORIES);
        ComponentsValue value;
        if (component != null && (value = component.get(TCItems.COMPONENTS)) != null) {
            tooltipComponents.addAll(value.components());
            return;
        }
        boolean b = builder == null;
        if (b || builder.hasToolTip) {
            tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId() + ".0"));
            if (!b) tooltipComponents.addAll(builder.additionTip);
        }
    }

    public int getJeiInformationCount() {
        return builder == null ? 0 : builder.jeiInformationCount;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return canEquip(slotContext, stack);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.cosmetic() || CuriosUtils.noSameCurio(slotContext.entity(), this);
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return builder != null && builder.makePiglinsNeutral;
    }

    public static Builder builder(String name, Properties properties) {
        return new Builder(name, properties);
    }

    public static Builder builder(String name) {
        return new Builder(name, new Properties());
    }

//    @Override
//    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
//        return enchantment.is(EnchantmentTags.CURSE) || stack.is(enchantment.value().definition().supportedItems());
//    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.isCurse() || enchantment.category.canEnchant(stack.getItem());
    }

    public static class Builder {
        private final String name;
        private final Properties properties;
        private final ResourceLocation defaultId;

        private final List<Component> additionTip = new ArrayList<>();
        private boolean hasToolTip = true;
        private transient ImmutableMultimap.Builder<Attribute, AttributeModifier> attributesBuilder = ImmutableMultimap.builder();
        private Multimap<Attribute, AttributeModifier> attributes;
        private ModRarity rarity = ModRarity.BLUE;
        private int jeiInformationCount = 1;
        private boolean makePiglinsNeutral = false;
        private EquipmentSlot equipmentSlot = null;

        private ResourceLocation particle = null;

        Builder(String name, Properties properties) {
            this.name = name;
            this.properties = properties;
            this.defaultId = TerraCurio.asResource(name);
        }

        public Builder particle(ResourceLocation particle) {
            this.particle = particle;
            return this;
        }

        public Builder equipable(EquipmentSlot slot) {
            this.equipmentSlot = slot;
            return this;
        }

        public Builder makesPiglinsNeutral() {
            this.makePiglinsNeutral = true;
            return this;
        }

        public <T> Builder component(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
            PortItemExtension.Properties.component(properties, type, value);
            return this;
        }

        @Diff
        public Builder attribute(Attribute attribute, String path, double amount, PortAttributeModifier.PortOperation operation) {
            attributesBuilder.put(attribute, new AttributeModifier(UUID.fromString(defaultId.getPath()), name + "_" + path, amount, operation.unwrap()));
            return this;
        }

        @Diff
        public Builder attribute(Attribute attribute, double amount, PortAttributeModifier.PortOperation operation) {
            attributesBuilder.put(attribute, new AttributeModifier(UUID.fromString(defaultId.getPath()), defaultId.getPath(), amount, operation.unwrap()));
            return this;
        }

        public Builder attribute(Holder<Attribute> attribute, String path, double amount, PortAttributeModifier.PortOperation operation) {
            attributesBuilder.put(attribute.value(), new AttributeModifier(UUID.fromString(defaultId.getPath()), name + "_" + path, amount, operation.unwrap()));
            return this;
        }

        public Builder attribute(Holder<Attribute> attribute, double amount, PortAttributeModifier.PortOperation operation) {
            attributesBuilder.put(attribute.value(), new AttributeModifier(UUID.fromString(defaultId.getPath()), defaultId.getPath(), amount, operation.unwrap()));
            return this;
        }

        public Builder stepHeight() {
            if (TCStartupConfigs.shoesExtraStepHeight()) {
                return attribute(PortAttributesExtension.stepHeight().value(), 0.5, PortAttributeModifier.PortOperation.ADD_VALUE);
            }
            return this;
        }

        public Builder rarity(ModRarity rarity) {
            this.rarity = rarity;
            if (rarity != ModRarity.GRAY && rarity != ModRarity.WHITE) {
                properties.fireResistant();
            }
            return this;
        }

        public Builder accessories(PrimitiveValueComponent component, PrimitiveValueComponent... components) {
            if (components.length == 0) {
                PortItemExtension.Properties.component(properties, TCDataComponentTypes.ACCESSORIES, component);
            } else {
                Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> map = new Hashtable<>(component.types());
                for (PrimitiveValueComponent component1 : components) {
                    map.putAll(component1.types());
                }
                PortItemExtension.Properties.component(properties, TCDataComponentTypes.ACCESSORIES, new PrimitiveValueComponent(map));
            }
            return this;
        }

        /**
         * 额外的工具提示
         */
        public Builder tooltip(String str) {
            if (!hasToolTip)
                throw new IllegalArgumentException("Can not add tooltip when noTooltip() invoked!");
            additionTip.add(Component.translatable(str));
            return this;
        }

        /**
         * 额外的工具提示
         *
         * @param extra 额外的数量
         */
        public Builder tooltips(int extra) {
            if (!hasToolTip)
                throw new IllegalArgumentException("Can not add tooltip when noTooltip() invoked!");
            extra += 1;
            for (int i = 1; i < extra; i++) {
                additionTip.add(Component.translatable("tooltip.item.terra_curio." + name + "." + i));
            }
            return this;
        }

        public Builder jeiInfos(int count) {
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
            PortItemExtension.Properties.component(properties.stacksTo(1), ConfluenceMagicLib.MOD_RARITY, rarity);
            this.attributes = attributesBuilder.build();
            this.attributesBuilder = null;
            return this;
        }

        @ApiStatus.Internal
        public Multimap<Attribute, AttributeModifier> getAttributes() {
            return attributes;
        }

        @ApiStatus.Internal
        public @Nullable ResourceLocation getParticle() {
            return particle;
        }

        public BaseCurioItem build() {
            if (equipmentSlot != null) {
                return new Equipable(this);
            }
            return new BaseCurioItem(this);
        }
    }

    public static class Equipable extends BaseCurioItem implements net.minecraft.world.item.Equipable {
        public Equipable(Builder builder) {
            super(builder);
        }

        @Override
        public EquipmentSlot getEquipmentSlot() {
            return builder.equipmentSlot;
        }
    }
}
