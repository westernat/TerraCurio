package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EntityTypesValue implements PrimitiveValue<Set<EntityType<?>>> {
    public static final Codec<EntityTypesValue> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().xmap(
            values -> new EntityTypesValue(new HashSet<>(values)),
            value -> new ArrayList<>(value.values)
    );
    public static final CombineRule<Set<EntityType<?>>, EntityTypesValue> EXPANSION = CombineRule.register((a, b) -> {
        Set<EntityType<?>> combined = new HashSet<>(a);
        combined.addAll(b);
        return combined;
    }, "entity_types_expansion");
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

    @Override
    public List<String> getDescription() {
        List<String> list = new ArrayList<>();
        for (EntityType<?> entityType : get()) {
            list.add(BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString());
        }
        return list;
    }
}
