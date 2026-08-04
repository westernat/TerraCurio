package org.confluence.terra_curio.common.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.UnitValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.component.PrimitiveValueComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class PrimitiveValueHolder implements ValueIOSerializable {
    public static final Codec<Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>>> VALUE_MAP_CODEC = Codec.dispatchedMap(ValueType.CODEC, ValueType.VALUE_CODECS::get);

    protected final Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> valueMap = new HashMap<>();

    public PrimitiveValueHolder() {
        setToDefaultValue();
    }

    public void setToDefaultValue() {
        valueMap.clear();
    }

    public <T, V extends PrimitiveValue<T>> boolean contains(ValueType<T, V> type) {
        return valueMap.containsKey(type);
    }

    public <T, V extends PrimitiveValue<T>> T getValue(ValueType<T, V> type) {
        V value = getPrimitiveValue(type);
        return value == null ? type.defaultValue() : value.get();
    }

    public <T, V extends PrimitiveValue<T>> @Nullable V getPrimitiveValue(ValueType<T, V> type) {
        return (V) valueMap.get(type);
    }

    protected void flushAbility(LivingEntity living) {
        setToDefaultValue();
    }

    public void compute(PrimitiveValueComponent component) {
        for (Map.Entry<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> entry : component.types().entrySet()) {
            ValueType<?, ? extends PrimitiveValue<?>> type = entry.getKey();
            if (type.defaultValue() == Unit.INSTANCE) {
                putUnit(type);
            } else {
                combineValue(type, tryCast(entry.getValue()));
            }
        }
    }

    public <T, V extends PrimitiveValue<T>> void putUnit(ValueType<T, V> type) {
        valueMap.put(type, UnitValue.INSTANCE);
    }

    public <T, V extends PrimitiveValue<T>> void combineValue(ValueType<T, V> type, V value) {
        V other = (V) valueMap.get(type);
        if (other == null) {
            valueMap.put(type, value);
        } else {
            T t = value.combine(other, type.combineRule());
            valueMap.put(type, type.newInstance(t));
        }
    }

    @Override
    public void serialize(ValueOutput output) {
        output.store("ValueMap", VALUE_MAP_CODEC, valueMap);
    }

    @Override
    public void deserialize(ValueInput input) {
        valueMap.clear();
        input.read("ValueMap", VALUE_MAP_CODEC).ifPresent(valueMap::putAll);
    }

    @ApiStatus.Internal
    public static <T, V extends PrimitiveValue<T>> V tryCast(PrimitiveValue<?> primitiveValue) {
        return (V) primitiveValue;
    }
}
