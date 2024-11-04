package org.confluence.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;

public interface PrimitiveValue<T> {
    T get();

    Codec<? extends PrimitiveValue<T>> codec();

    default <V extends PrimitiveValue<T>> T combine(V other, CombineRule<T, V> combineRule) {
        return combineRule.combine(get(), other.get());
    }

    default <V extends PrimitiveValue<T>> T combine(T other, CombineRule<T, V> combineRule) {
        return combineRule.combine(get(), other);
    }
}
