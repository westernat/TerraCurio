package org.confluence.mod.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;

public interface PrimitiveValue<T> {
    T get();

    Codec<? extends PrimitiveValue<T>> codec();

    default <V extends PrimitiveValue<T>> T combine(PrimitiveValue<T> other, CombineRule<T, V> combineRule) {
        return combineRule.combine(this, other);
    }
}
