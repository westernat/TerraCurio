package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EntityTypesValue implements PrimitiveValue<Set<EntityType<?>>> {
    public static final Codec<EntityTypesValue> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().xmap(
            values -> new EntityTypesValue(new HashSet<>(values)),
            value -> new ArrayList<>(value.values)
    );
    public static final CombineRule<Set<EntityType<?>>, EntityTypesValue> EXPANSION = CombineRule.register(new CombineRule<>() {
        @Override
        public Set<EntityType<?>> combine(Set<EntityType<?>> componentA, Set<EntityType<?>> componentB) {
            Set<EntityType<?>> combined = new HashSet<>(componentA);
            combined.addAll(componentB);
            return combined;
        }

        @Override
        public String name() {
            return "entity_types_expansion";
        }
    });
    private final Supplier<Set<EntityType<?>>> supplier;
    private Set<EntityType<?>> values;

    public EntityTypesValue(Supplier<Set<EntityType<?>>> supplier) {
        this.supplier = supplier;
        this.values = null;
    }

    public EntityTypesValue(Set<EntityType<?>> values) {
        this.supplier = null;
        this.values = values;
    }

    public EntityTypesValue(EntityType<?>... values) {
        this(Arrays.stream(values).collect(Collectors.toSet()));
    }

    @Override
    public Set<EntityType<?>> get() {
        if (supplier != null && values == null) {
            this.values = supplier.get();
        }
        return values;
    }

    @Override
    public Codec<EntityTypesValue> codec() {
        return CODEC;
    }
}
