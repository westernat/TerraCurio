package org.confluence.terra_curio.api.primitive;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attribute.PortAttributeExtension;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.confluence.lib.util.LibCodecUtils;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.*;

public final class AttributeModifiersValue implements PrimitiveValue<ImmutableListMultimap<Holder<Attribute>, PortAttributeModifier>> {
    public static final AttributeModifiersValue EMPTY = new AttributeModifiersValue(ImmutableListMultimap.of());
    public static final Codec<AttributeModifiersValue> CODEC = LibCodecUtils
            .multimap(PortAttributeExtension.codec(), PortAttributeModifier.CODEC)
            .xmap(AttributeModifiersValue::new, AttributeModifiersValue::get);
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, AttributeModifiersValue> STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public AttributeModifiersValue decode(PortRegistryFriendlyByteBuf buffer) {
            int size = buffer.readInt();
            ImmutableListMultimap.Builder<Holder<Attribute>, PortAttributeModifier> builder = ImmutableListMultimap.builder();
            for (int i = 0; i < size; i++) {
                Holder<Attribute> holder = PortAttributeExtension.streamCodec().decode(buffer);
                int amount = buffer.readInt();
                for (int j = 0; j < amount; j++) {
                    builder.put(holder, PortAttributeModifier.STREAM_CODEC.decode(buffer));
                }
            }
            return new AttributeModifiersValue(builder.build());
        }

        @Override
        public void encode(PortRegistryFriendlyByteBuf buffer, AttributeModifiersValue value) {
            buffer.writeInt(value.value.keySet().size());
            for (Map.Entry<Holder<Attribute>, Collection<PortAttributeModifier>> entry : value.value.asMap().entrySet()) {
                PortAttributeExtension.streamCodec().encode(buffer, entry.getKey());
                buffer.writeInt(entry.getValue().size());
                for (PortAttributeModifier modifier : entry.getValue()) {
                    PortAttributeModifier.STREAM_CODEC.encode(buffer, modifier);
                }
            }
        }
    };
    public static final CombineRule<ImmutableListMultimap<Holder<Attribute>, PortAttributeModifier>, AttributeModifiersValue> MERGE = CombineRule.register((a, b) -> ImmutableListMultimap.<Holder<Attribute>, PortAttributeModifier>builder().putAll(a).putAll(b).build(), "attributes_modifiers_merge");

    private final ImmutableListMultimap<Holder<Attribute>, PortAttributeModifier> value;
    private Multimap<Attribute, AttributeModifier> oldValue;

    public AttributeModifiersValue(ImmutableListMultimap<Holder<Attribute>, PortAttributeModifier> value) {
        this.value = value;
    }

    @Diff
    public Multimap<Attribute, AttributeModifier> getOldValue() {
        if (oldValue == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            for (Map.Entry<Holder<Attribute>, Collection<PortAttributeModifier>> entry : value.asMap().entrySet()) {
                Attribute attribute = entry.getKey().value();
                for (PortAttributeModifier modifier : entry.getValue()) {
                    builder.put(attribute, modifier.unwrap());
                }
            }
            this.oldValue = builder.build();
        }
        return oldValue;
    }

    @Override
    public ImmutableListMultimap<Holder<Attribute>, PortAttributeModifier> get() {
        return value;
    }

    @Override
    public Codec<AttributeModifiersValue> codec() {
        return CODEC;
    }

    @Override
    public List<String> getDescription() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Holder<Attribute>, Collection<PortAttributeModifier>> entry : value.asMap().entrySet()) {
            list.add(entry.getKey().unwrapKey().orElseThrow().location().toString());
            for (PortAttributeModifier modifier : entry.getValue()) {
                list.add("    " + modifier);
            }
        }
        return list;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof AttributeModifiersValue v && value.equals(v.value));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static AttributeModifiersValue simple(Holder<Attribute> attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation) {
        return new AttributeModifiersValue(ImmutableListMultimap.of(attribute, new PortAttributeModifier(id, amount, operation)));
    }

    public ImmutableListMultimap<Holder<Attribute>, PortAttributeModifier> value() {
        return value;
    }

    @Override
    public String toString() {
        return "AttributeModifiersValue[" +
                "value=" + value + ']';
    }

    public static class Builder {
        private final ImmutableListMultimap.Builder<Holder<Attribute>, PortAttributeModifier> builder = ImmutableListMultimap.builder();

        Builder() {}

        public Builder add(Holder<Attribute> attribute, PortAttributeModifier... modifiers) {
            builder.putAll(attribute, modifiers);
            return this;
        }

        public Builder add(Holder<Attribute> attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation) {
            builder.put(attribute, new PortAttributeModifier(id, amount, operation));
            return this;
        }

        public Builder addAll(Multimap<Holder<Attribute>, PortAttributeModifier> multimap) {
            builder.putAll(multimap);
            return this;
        }

        public AttributeModifiersValue build() {
            return new AttributeModifiersValue(builder.build());
        }
    }
}
