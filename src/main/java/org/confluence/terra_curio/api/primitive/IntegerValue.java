package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;

public class IntegerValue implements PrimitiveValue<Integer> {
    public static final Codec<IntegerValue> CODEC = Codec.INT.xmap(IntegerValue::new, IntegerValue::get);
    public static final CombineRule<Integer, IntegerValue> GET_MAX = CombineRule.register(new CombineRule<>() {
        @Override
        public Integer combine(Integer componentA, Integer componentB) {
            return Math.max(componentA, componentB);
        }

        @Override
        public String name() {
            return "integer_get_max";
        }
    });
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
