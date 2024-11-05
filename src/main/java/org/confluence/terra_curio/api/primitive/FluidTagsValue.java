package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.*;

public record FluidTagsValue(List<TagKey<Fluid>> tags) implements PrimitiveValue<List<TagKey<Fluid>>> {
    public static final Codec<FluidTagsValue> CODEC = TagKey.codec(Registries.FLUID).listOf().xmap(FluidTagsValue::new, FluidTagsValue::get);
    public static final CombineRule<List<TagKey<Fluid>>, FluidTagsValue> EXPANSION = CombineRule.register(new CombineRule<>() {
        @Override
        public List<TagKey<Fluid>> combine(List<TagKey<Fluid>> componentA, List<TagKey<Fluid>> componentB) {
            Set<TagKey<Fluid>> combined = new HashSet<>(componentA);
            combined.addAll(componentB);
            return new ArrayList<>(combined);
        }

        @Override
        public String name() {
            return "fluid_tag_expansion";
        }
    });

    @SafeVarargs
    public FluidTagsValue(TagKey<Fluid>... tags) {
        this(Arrays.stream(tags).toList());
    }

    @Override
    public List<TagKey<Fluid>> get() {
        return tags;
    }

    @Override
    public Codec<FluidTagsValue> codec() {
        return CODEC;
    }
}
