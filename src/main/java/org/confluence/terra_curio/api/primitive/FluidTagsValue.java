package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record FluidTagsValue(Set<TagKey<Fluid>> tags) implements PrimitiveValue<Set<TagKey<Fluid>>> {
    public static final Codec<FluidTagsValue> CODEC = TagKey.codec(Registries.FLUID).listOf().xmap(
            values -> new FluidTagsValue(new HashSet<>(values)),
            value -> new ArrayList<>(value.tags)
    );
    public static final CombineRule<Set<TagKey<Fluid>>, FluidTagsValue> EXPANSION = CombineRule.register(new CombineRule<>() {
        @Override
        public Set<TagKey<Fluid>> combine(Set<TagKey<Fluid>> componentA, Set<TagKey<Fluid>> componentB) {
            Set<TagKey<Fluid>> combined = new HashSet<>(componentA);
            combined.addAll(componentB);
            return combined;
        }

        @Override
        public String name() {
            return "fluid_tags_expansion";
        }
    });

    @SafeVarargs
    public FluidTagsValue(TagKey<Fluid>... tags) {
        this(Arrays.stream(tags).collect(Collectors.toSet()));
    }

    @Override
    public Set<TagKey<Fluid>> get() {
        return tags;
    }

    @Override
    public Codec<FluidTagsValue> codec() {
        return CODEC;
    }
}
