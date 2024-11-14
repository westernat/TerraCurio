package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;

import java.util.function.BiFunction;

public interface PrimitiveValue<T> {

    T get();

    Codec<? extends PrimitiveValue<T>> codec();

    default <V extends PrimitiveValue<T>> T combine(V other, CombineRule<T, V> combineRule) {
        return combineRule.combine(get(), other.get());
    }

    default <V extends PrimitiveValue<T>> T combine(T other, CombineRule<T, V> combineRule) {
        return combineRule.combine(get(), other);
    }

    static <T> BiFunction<T, T, T> getSelfFunction() {
        return (a, b) -> a;
    }
}
