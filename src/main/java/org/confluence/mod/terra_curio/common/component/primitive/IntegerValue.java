package org.confluence.mod.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;

public class IntegerValue implements PrimitiveValue<Integer> {
    public static final Codec<IntegerValue> CODEC = Codec.INT.xmap(IntegerValue::new, IntegerValue::get);
    private final int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public Codec<IntegerValue> codec() {
        return CODEC;
    }
}
