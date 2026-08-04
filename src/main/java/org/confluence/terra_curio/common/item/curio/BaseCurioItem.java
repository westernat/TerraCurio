package org.confluence.terra_curio.common.item.curio;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
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
import org.confluence.terra_curio.util.CuriosUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BaseCurioItem extends Item implements ICurioItem {
    protected static final ImmutableMultimap<Holder<Attribute>, AttributeModifier> EMPTY_ATTRIBUTE = ImmutableMultimap.of();
    protected Builder builder;

    public BaseCurioItem(Builder builder) {
        this(builder.initialize());
        this.builder = builder;
    }

    public BaseCurioItem(Properties properties) {
        super(properties.stacksTo(1));
    }

//    @Override
//    public void curioTick(SlotContext slotContext, ItemStack stack) {
//        if (builder == null || builder.particle == null) return;
//        LivingEntity living = slotContext.entity();
//        if (living.level().isClientSide) {
//            ILivingEntity iLiving = (ILivingEntity) living;
//            ParticleEmitter emitter = iLiving.terra_curio$getOrCreateParticleEmitters().get(builder.particle);
//            if (emitter == null || emitter.isRemoved()) {
//                Map<Identifier, ParticleEmitter> emitters = iLiving.terra_curio$getOrCreateParticleEmitters();
//                emitter = new ParticleEmitter(living.level(), living.position(), builder.particle);
//                emitter.attachEntity(living);
//                emitter.hideOutline = true;
//                MolangParticleEngine.INSTANCE.addEmitter(emitter);
//                emitters.put(builder.particle, emitter);
//            }
//            particleTick(living, emitter, builder.particle);
//            emitter.active &= slotContext.visible();
//        }
//    }

//    protected void particleTick(LivingEntity living, ParticleEmitter emitter, Identifier particle) {
//        if (emitter.isRemoved()) {
//            ((ILivingEntity) living).terra_curio$getOrCreateParticleEmitters().remove(particle);
//        }
//    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, Identifier id, ItemStack stack) {
        return getAttributeModifiers(stack);
    }

    public ImmutableMultimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        PrimitiveValueComponent component = stack.typeHolder().getData(TCDataMaps.ACCESSORIES);
        AttributeModifiersValue value;
        if (component != null && (value = component.get(TCItems.ATTRIBUTES)) != null) {
            return value.get();
        }
        return builder == null ? EMPTY_ATTRIBUTE : builder.attributes;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        PrimitiveValueComponent component = itemStack.typeHolder().getData(TCDataMaps.ACCESSORIES);
        ComponentsValue value;
        if (component != null && (value = component.get(TCItems.COMPONENTS)) != null) {
            value.components().forEach(builder);
            return;
        }
        boolean b = this.builder == null;
        if (b || this.builder.hasToolTip) {
            builder.accept(Component.translatable("tooltip." + itemStack.getItem().getDescriptionId() + ".0"));
            if (!b) this.builder.additionTip.forEach(builder);
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

    public static Builder builder(Identifier id, Properties properties) {
        return new Builder(id, properties);
    }

    public static Builder builder(Identifier id) {
        return new Builder(id, new Properties());
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(EnchantmentTags.CURSE) || stack.is(enchantment.value().definition().supportedItems());
    }

    public static class Builder {
        private final Identifier id;
        private final Properties properties;

        private final List<Component> additionTip = new ArrayList<>();
        private boolean hasToolTip = true;
        private transient ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> attributesBuilder = ImmutableMultimap.builder();
        private ImmutableMultimap<Holder<Attribute>, AttributeModifier> attributes;
        private ModRarity rarity = ModRarity.BLUE;
        private int jeiInformationCount = 1;
        private boolean makePiglinsNeutral = false;
        private EquipmentSlot equipmentSlot = null;

        private Identifier particle = null;

        Builder(Identifier id, Properties properties) {
            this.id = id;
            this.properties = properties;
        }

        public Builder particle(Identifier particle) {
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

        public <T> Builder component(Supplier<DataComponentType<T>> type, T value) {
            properties.component(type, value);
            return this;
        }

        public Builder attribute(Holder<Attribute> attribute, String path, double amount, AttributeModifier.Operation operation) {
            attributesBuilder.put(attribute, new AttributeModifier(TerraCurio.asResource(id.getPath() + "_" + path), amount, operation));
            return this;
        }

        public Builder attribute(Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation) {
            attributesBuilder.put(attribute, new AttributeModifier(id, amount, operation));
            return this;
        }

        public Builder stepHeight() {
            if (TCStartupConfigs.shoesExtraStepHeight()) {
                return attribute(Attributes.STEP_HEIGHT, 0.5, AttributeModifier.Operation.ADD_VALUE);
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
                properties.component(TCDataComponentTypes.ACCESSORIES, component);
            } else {
                Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> map = new Hashtable<>(component.types());
                for (PrimitiveValueComponent component1 : components) {
                    map.putAll(component1.types());
                }
                properties.component(TCDataComponentTypes.ACCESSORIES, new PrimitiveValueComponent(map));
            }
            return this;
        }

        /// 额外的工具提示
        public Builder tooltip(String str) {
            if (!hasToolTip)
                throw new IllegalArgumentException("Can not add tooltip when noTooltip() invoked!");
            additionTip.add(Component.translatable(str));
            return this;
        }

        /// 额外的工具提示
        ///
        /// @param extra 额外的数量
        public Builder tooltips(int extra) {
            if (!hasToolTip)
                throw new IllegalArgumentException("Can not add tooltip when noTooltip() invoked!");
            extra += 1;
            for (int i = 1; i < extra; i++) {
                additionTip.add(Component.translatable("tooltip.item.terra_curio." + id.getPath() + "." + i));
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
        public Properties initialize() {
            properties.setId(ResourceKey.create(Registries.ITEM, id));
            properties.component(ConfluenceMagicLib.MOD_RARITY, rarity);
            this.attributes = attributesBuilder.build();
            this.attributesBuilder = null;
            return properties;
        }

        @ApiStatus.Internal
        public ImmutableMultimap<Holder<Attribute>, AttributeModifier> getAttributes() {
            return attributes;
        }

        @ApiStatus.Internal
        public @Nullable Identifier getParticle() {
            return particle;
        }

        public BaseCurioItem build() {
            if (equipmentSlot != null) {
                properties.equippable(equipmentSlot);
            }
            return new BaseCurioItem(this);
        }
    }
}
