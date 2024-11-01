package org.confluence.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class EntityTypeValue implements PrimitiveValue<EntityType<?>> {
    public static final Codec<EntityTypeValue> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec()
            .xmap(EntityTypeValue::new, EntityTypeValue::get);
    private final Supplier<EntityType<?>> supplier;
    private final EntityType<?> value;

    public EntityTypeValue(Supplier<EntityType<?>> supplier) {
        this.supplier = supplier;
        this.value = null;
    }

    public EntityTypeValue(EntityType<?> value) {
        this.supplier = null;
        this.value = value;
    }

    @Override
    public EntityType<?> get() {
        return supplier == null ? value : supplier.get();
    }

    @Override
    public Codec<EntityTypeValue> codec() {
        return CODEC;
    }
}
