package org.confluence.terra_curio.api.primitive;

import com.google.common.collect.ImmutableListMultimap;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public record AttributeModifiersValue(ImmutableListMultimap<Holder<Attribute>, AttributeModifier> value) implements PrimitiveValue<ImmutableListMultimap<Holder<Attribute>, AttributeModifier>> {
    public static final Codec<AttributeModifiersValue> CODEC = Codec.unboundedMap(RegistryFixedCodec.create(Registries.ATTRIBUTE), AttributeModifier.MAP_CODEC.codec().listOf()).xmap(
            map -> {
                ImmutableListMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableListMultimap.builder();
                for (Map.Entry<Holder<Attribute>, List<AttributeModifier>> entry : map.entrySet()) {
                    builder.putAll(entry.getKey(), entry.getValue());
                }
                return new AttributeModifiersValue(builder.build());
            },
            pv -> {
                Map<Holder<Attribute>, List<AttributeModifier>> map = new Hashtable<>();
                for (Holder<Attribute> holder : pv.value.keySet()) {
                    map.put(holder, pv.value.get(holder));
                }
                return map;
            }
    );
    public static final CombineRule<ImmutableListMultimap<Holder<Attribute>, AttributeModifier>, AttributeModifiersValue> GET_SELF = new CombineRule<>() {
        @Override
        public ImmutableListMultimap<Holder<Attribute>, AttributeModifier> combine(ImmutableListMultimap<Holder<Attribute>, AttributeModifier> componentA, ImmutableListMultimap<Holder<Attribute>, AttributeModifier> componentB) {
            return componentA;
        }

        @Override
        public String name() {
            return "attributes_modifiers_get_self";
        }
    };

    @Override
    public ImmutableListMultimap<Holder<Attribute>, AttributeModifier> get() {
        return value;
    }

    @Override
    public Codec<AttributeModifiersValue> codec() {
        return CODEC;
    }
}
