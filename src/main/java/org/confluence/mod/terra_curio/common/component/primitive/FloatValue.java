package org.confluence.mod.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;

public class FloatValue implements PrimitiveValue<Float> {
    public static final Codec<FloatValue> CODEC = Codec.FLOAT.xmap(FloatValue::new, FloatValue::get);
    private final float value;

    public FloatValue(float value) {
        this.value = value;
    }

    @Override
    public Float get() {
        return value;
    }

    @Override
    public Codec<FloatValue> codec() {
        return CODEC;
    }
}
