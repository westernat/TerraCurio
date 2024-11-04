package org.confluence.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.List;

public record FluidTagsValue(List<TagKey<Fluid>> tags) implements PrimitiveValue<List<TagKey<Fluid>>> {
    public static final Codec<FluidTagsValue> CODEC = TagKey.codec(Registries.FLUID).listOf().xmap(FluidTagsValue::new, FluidTagsValue::get);

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
