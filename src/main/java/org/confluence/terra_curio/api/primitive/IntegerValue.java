package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;

public class IntegerValue implements PrimitiveValue<Integer> {
    public static final Codec<IntegerValue> CODEC = Codec.INT.xmap(IntegerValue::new, IntegerValue::get);
    public static final CombineRule<Integer, IntegerValue> GET_MAX = CombineRule.register(Math::max, "integer_get_max");
    public static final CombineRule<Integer, IntegerValue> ADDITION = CombineRule.register(Integer::sum, "integer_addition");
    public static final CombineRule<Integer, IntegerValue> GET_MIN_GREAT_EQUAL_THAN_0 = CombineRule.register((a, b) -> Math.max(0, Math.min(a, b)), "integer_get_min_great_equal_than_0");
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
