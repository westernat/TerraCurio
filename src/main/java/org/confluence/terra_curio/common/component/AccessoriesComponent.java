package org.confluence.terra_curio.common.component;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger;
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover;
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.UnitValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("unchecked")
public record AccessoriesComponent(Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> types) implements DataComponentType<AccessoriesComponent> {
    public static final Codec<AccessoriesComponent> CODEC = Codec
            .<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>>dispatchedMap(
                    ResourceLocation.CODEC.xmap(ValueType.TYPES::get, ValueType::key), ValueType.VALUE_CODECS::get)
            .xmap(AccessoriesComponent::new, AccessoriesComponent::types);

    public static final StreamCodec<RegistryFriendlyByteBuf, AccessoriesComponent> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public static <T, V extends PrimitiveValue<T>> AccessoriesComponent entry(ValueType<T, V> type, V value) {
        Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> map = new Hashtable<>();
        map.put(type, value);
        return new AccessoriesComponent(map);
    }

    public static <T, V extends PrimitiveValue<T>> AccessoriesComponent of(ValueType<T, V> type, T value) {
        Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> map = new Hashtable<>();
        map.put(type, type.newInstance(value));
        return new AccessoriesComponent(map);
    }

    public static AccessoriesComponent units(ValueType<Unit, ? extends UnitValue> type, ValueType<Unit, ? extends UnitValue>... types) {
        Hashtable<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> table = new Hashtable<>(Map.of(type, UnitValue.INSTANCE));
        for (ValueType<Unit, ? extends UnitValue> type1 : types) {
            table.put(type1, UnitValue.INSTANCE);
        }
        return new AccessoriesComponent(table);
    }

    public <T, V extends PrimitiveValue<T>> boolean contains(ValueType<T, V> type) {
        return types.containsKey(type);
    }

    public <T, V extends PrimitiveValue<T>> @Nullable V get(ValueType<T, V> type) {
        return (V) types.get(type);
    }

    public <T, V extends PrimitiveValue<T>> void put(ValueType<T, V> type, V value) {
        if (value instanceof PrimitiveValue<?> value1) {
            types.put(type, value1);
        }
    }

    @Override
    public @Nullable Codec<AccessoriesComponent> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AccessoriesComponent> streamCodec() {
        return STREAM_CODEC;
    }

    public record Remover(List<ValueType<?, ? extends PrimitiveValue<?>>> types) implements DataMapValueRemover<Item, AccessoriesComponent> {
        public static final Codec<Remover> CODEC = ValueType.CODEC.listOf().xmap(Remover::new, Remover::types);

        @Override
        public Optional<AccessoriesComponent> remove(AccessoriesComponent component, Registry<Item> registry, Either<TagKey<Item>, ResourceKey<Item>> source, Item item) {
            HashMap<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> map = new HashMap<>(component.types());
            for (ValueType<?, ? extends PrimitiveValue<?>> type : types) {
                map.remove(type);
            }
            return Optional.of(new AccessoriesComponent(map));
        }
    }

    public static class Merger implements DataMapValueMerger<Item, AccessoriesComponent> {
        @Override
        public AccessoriesComponent merge(Registry<Item> registry, Either<TagKey<Item>, ResourceKey<Item>> either, AccessoriesComponent component, Either<TagKey<Item>, ResourceKey<Item>> either1, AccessoriesComponent component1) {
            Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> map = new Hashtable<>(component1.types());
            map.putAll(component.types());
            return new AccessoriesComponent(map);
        }
    }
}
