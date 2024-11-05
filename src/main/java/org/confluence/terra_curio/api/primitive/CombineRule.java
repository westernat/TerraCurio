package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;

import java.util.Hashtable;
import java.util.Map;

public abstract class CombineRule<T, V extends PrimitiveValue<T>> {
    public static final Map<String, CombineRule<?, ?>> RULES = new Hashtable<>();

    public static final Codec<CombineRule<?, ?>> CODEC = Codec.STRING.xmap(RULES::get, CombineRule::name); // todo

    public abstract T combine(T componentA, T componentB);

    public T combineValue(V valueA, V valueB) {
        return combine(valueA.get(), valueB.get());
    }

    public T combineFromValue(V valueA, T componentB) {
        return combine(valueA.get(), componentB);
    }

    public T combineWithValue(T componentA, V valueB) {
        return combine(componentA, valueB.get());
    }

    public abstract String name();

    public static <T, V extends PrimitiveValue<T>> CombineRule<T, V> register(CombineRule<T, V> combineRule) {
        RULES.put(combineRule.name(), combineRule);
        return combineRule;
    }
}
