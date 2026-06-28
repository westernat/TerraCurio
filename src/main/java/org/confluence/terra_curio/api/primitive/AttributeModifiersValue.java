package org.confluence.terra_curio.api.primitive;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attribute.PortAttributeExtension;
import PortLib.extensions.net.minecraft.world.entity.ai.attributes.AttributeModifier.PortAttributeModifierExtension;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.confluence.lib.util.LibCodecUtils;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.*;

public record AttributeModifiersValue(
        ImmutableListMultimap<Attribute, AttributeModifier> value
) implements PrimitiveValue<ImmutableListMultimap<Attribute, AttributeModifier>> {
    public static final AttributeModifiersValue EMPTY = new AttributeModifiersValue(ImmutableListMultimap.of());
    public static final Codec<AttributeModifiersValue> CODEC = LibCodecUtils
            .multimap(PortAttributeExtension.directCodec(), PortAttributeModifierExtension.codec())
            .xmap(AttributeModifiersValue::new, AttributeModifiersValue::get);
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, AttributeModifiersValue> STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public AttributeModifiersValue decode(PortRegistryFriendlyByteBuf buffer) {
            int size = buffer.readInt();
            ImmutableListMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableListMultimap.builder();
            for (int i = 0; i < size; i++) {
                Attribute holder = PortAttributeExtension.directStreamCodec().decode(buffer);
                int amount = buffer.readInt();
                for (int j = 0; j < amount; j++) {
                    builder.put(holder, PortAttributeModifierExtension.streamCodec().decode(buffer));
                }
            }
            return new AttributeModifiersValue(builder.build());
        }

        @Override
        public void encode(PortRegistryFriendlyByteBuf buffer, AttributeModifiersValue value) {
            buffer.writeInt(value.value.keySet().size());
            for (Map.Entry<Attribute, Collection<AttributeModifier>> entry : value.value.asMap().entrySet()) {
                PortAttributeExtension.directStreamCodec().encode(buffer, entry.getKey());
                buffer.writeInt(entry.getValue().size());
                for (AttributeModifier modifier : entry.getValue()) {
                    PortAttributeModifierExtension.streamCodec().encode(buffer, modifier);
                }
            }
        }
    };
    public static final CombineRule<ImmutableListMultimap<Attribute, AttributeModifier>, AttributeModifiersValue> MERGE = CombineRule.register((a, b) -> ImmutableListMultimap.<Attribute, AttributeModifier>builder().putAll(a).putAll(b).build(), "attributes_modifiers_merge");

    @Override
    public ImmutableListMultimap<Attribute, AttributeModifier> get() {
        return value;
    }

    @Override
    public Codec<AttributeModifiersValue> codec() {
        return CODEC;
    }

    @Override
    public List<String> getDescription() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Attribute, Collection<AttributeModifier>> entry : value.asMap().entrySet()) {
            list.add(entry.getKey().getDescriptionId());
            for (AttributeModifier modifier : entry.getValue()) {
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

    public static AttributeModifiersValue simple(Attribute attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation) {
        return new AttributeModifiersValue(ImmutableListMultimap.of(attribute, new AttributeModifier(PortAttributeModifier.rl2uuid(id), id.getPath(), amount, operation.unwrap())));
    }

    public static AttributeModifiersValue simple(Holder<Attribute> attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation) {
        return simple(attribute.value(), id, amount, operation);
    }

    public ImmutableListMultimap<Attribute, AttributeModifier> value() {
        return value;
    }

    @Override
    public String toString() {
        return "AttributeModifiersValue[" +
                "value=" + value + ']';
    }

    public static class Builder {
        private final ImmutableListMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableListMultimap.builder();

        Builder() {}

        public Builder add(Attribute attribute, AttributeModifier... modifiers) {
            builder.putAll(attribute, modifiers);
            return this;
        }

        public Builder add(Attribute attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation) {
            builder.put(attribute, new AttributeModifier(PortAttributeModifier.rl2uuid(id), id.getPath(), amount, operation.unwrap()));
            return this;
        }

        public Builder add(Holder<Attribute> attribute, AttributeModifier... modifiers) {
            return add(attribute.value(), modifiers);
        }

        public Builder add(Holder<Attribute> attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation) {
            return add(attribute.value(), id, amount, operation);
        }

        public Builder addAll(Multimap<Attribute, AttributeModifier> multimap) {
            builder.putAll(multimap);
            return this;
        }

        public AttributeModifiersValue build() {
            return new AttributeModifiersValue(builder.build());
        }
    }
}
