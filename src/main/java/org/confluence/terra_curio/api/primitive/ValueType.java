package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import org.confluence.terra_curio.TerraCurio;

import java.util.Hashtable;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ValueType<T, V extends PrimitiveValue<T>> {
    public static final Map<ResourceLocation, Codec<PrimitiveValue<?>>> VALUE_CODECS = new Hashtable<>();
    public static final Map<ResourceLocation, ValueType<?, ? extends PrimitiveValue<?>>> TYPES = new Hashtable<>();
    public static final Codec<ValueType<?, ? extends PrimitiveValue<?>>> CODEC = ResourceLocation.CODEC.xmap(TYPES::get, ValueType::key);

    private final ResourceLocation key;
    private final CombineRule<T, V> combineRule;
    private final T defaultValue;
    private final Function<T, V> factory;

    public ValueType(ResourceLocation key, CombineRule<T, V> combineRule, T defaultValue, Function<T, V> factory) {
        this.key = key;
        this.combineRule = combineRule;
        this.defaultValue = defaultValue;
        this.factory = factory;
    }

    private static <T, V extends PrimitiveValue<T>> void registerCodec(ResourceLocation id, Codec<V> codec) {
        if (codec instanceof Codec<? extends PrimitiveValue<?>> codec1) {
            VALUE_CODECS.put(id, (Codec<PrimitiveValue<?>>) codec1);
        }
    }

    public static <T, V extends PrimitiveValue<T>> ValueType<T, V> create(String path, CombineRule<T, V> combineRule, Codec<V> codec, T defaultValue, Function<T, V> factory) {
        ResourceLocation id = TerraCurio.asResource(path);
        registerCodec(id, codec);
        ValueType<T, V> type = new ValueType<>(id, combineRule, defaultValue, factory);
        TYPES.put(id, type);
        return type;
    }

    public static ValueType<Unit, UnitValue> ofUnit(String path) {
        return create(path, UnitValue.GET_SELF, UnitValue.CODEC, Unit.INSTANCE, UnitValue.UNIT_2_VALUE);
    }

    public static ValueType<Integer, IntegerValue> ofInteger(String path, CombineRule<Integer, IntegerValue> combineRule, int defaultValue) {
        return create(path, combineRule, IntegerValue.CODEC, defaultValue, IntegerValue::new);
    }

    public static ValueType<Float, FloatValue> ofFloat(String path, CombineRule<Float, FloatValue> combineRule, float defaultValue) {
        return create(path, combineRule, FloatValue.CODEC, defaultValue, FloatValue::new);
    }

    public ResourceLocation key() {
        return key;
    }

    public CombineRule<T, V> combineRule() {
        return combineRule;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public V newInstance(T t) {
        return factory.apply(t);
    }

    @Override
    public String toString() {
        return "Type{" +
                "key=" + key +
                ", combineRule=" + combineRule +
                ", defaultValue=" + defaultValue +
                ", factory=" + factory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        return o instanceof ValueType<?, ?> valueType && valueType.key.equals(key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
