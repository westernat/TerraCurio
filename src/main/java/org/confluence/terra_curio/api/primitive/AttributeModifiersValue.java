package org.confluence.terra_curio.api.primitive;

import com.google.common.collect.ImmutableListMultimap;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.*;

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
    public static final CombineRule<ImmutableListMultimap<Holder<Attribute>, AttributeModifier>, AttributeModifiersValue> GET_SELF = CombineRule.register(PrimitiveValue.identity(), "attributes_modifiers_get_self");

    @Override
    public ImmutableListMultimap<Holder<Attribute>, AttributeModifier> get() {
        return value;
    }

    @Override
    public Codec<AttributeModifiersValue> codec() {
        return CODEC;
    }

    @Override
    public List<String> getDescription() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Holder<Attribute>, Collection<AttributeModifier>> entry : value.asMap().entrySet()) {
            list.add(BuiltInRegistries.ATTRIBUTE.getKey(entry.getKey().value()).toString());
            for (AttributeModifier modifier : entry.getValue()) {
                list.add("    " + modifier);
            }
        }
        return list;
    }
}
