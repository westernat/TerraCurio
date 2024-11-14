package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;

import java.util.Hashtable;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class CombineRule<T, V extends PrimitiveValue<T>> {
    public static final Map<String, CombineRule<?, ?>> RULES = new Hashtable<>();

    public static final Codec<CombineRule<?, ?>> CODEC = Codec.STRING.xmap(RULES::get, CombineRule::name); // 暂未使用

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

    public static <T, V extends PrimitiveValue<T>> CombineRule<T, V> register(BiFunction<T, T, T> combineFunc, String name) {
        CombineRule<T, V> combineRule = new CombineRule<>() {
            @Override
            public T combine(T componentA, T componentB) {
                return combineFunc.apply(componentA, componentB);
            }

            @Override
            public String name() {
                return name;
            }
        };
        RULES.put(name, combineRule);
        return combineRule;
    }
}
