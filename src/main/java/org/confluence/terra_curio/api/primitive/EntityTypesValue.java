package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.*;
import java.util.function.Supplier;

public class EntityTypesValue implements PrimitiveValue<List<EntityType<?>>> {
    public static final Codec<EntityTypesValue> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf()
            .xmap(EntityTypesValue::new, EntityTypesValue::get);
    public static final CombineRule<List<EntityType<?>>, EntityTypesValue> EXPANSION = CombineRule.register(new CombineRule<>() {
        @Override
        public List<EntityType<?>> combine(List<EntityType<?>> componentA, List<EntityType<?>> componentB) {
            Set<EntityType<?>> combined = new HashSet<>(componentA);
            combined.addAll(componentB);
            return new ArrayList<>(combined);
        }

        @Override
        public String name() {
            return "entity_types_expansion";
        }
    });
    private final Supplier<List<EntityType<?>>> supplier;
    private List<EntityType<?>> values;

    public EntityTypesValue(Supplier<List<EntityType<?>>> supplier) {
        this.supplier = supplier;
        this.values = null;
    }

    public EntityTypesValue(List<EntityType<?>> values) {
        this.supplier = null;
        this.values = values;
    }

    public EntityTypesValue(EntityType<?>... values) {
        this(Arrays.stream(values).toList());
    }

    @Override
    public List<EntityType<?>> get() {
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
