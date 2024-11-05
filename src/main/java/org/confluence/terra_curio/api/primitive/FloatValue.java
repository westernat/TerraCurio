package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;

public class FloatValue implements PrimitiveValue<Float> {
    public static final Codec<FloatValue> CODEC = Codec.FLOAT.xmap(FloatValue::new, FloatValue::get);
    public static final CombineRule<Float, FloatValue> GET_SELF = CombineRule.register(new CombineRule<>() {
        @Override
        public Float combine(Float componentA, Float componentB) {
            return componentA;
        }

        @Override
        public String name() {
            return "float_get_self";
        }
    });
    public static final CombineRule<Float, FloatValue> GET_MAX = CombineRule.register(new CombineRule<>() {
        @Override
        public Float combine(Float componentA, Float componentB) {
            return Math.max(componentA, componentB);
        }

        @Override
        public String name() {
            return "float_get_max";
        }
    });
    public static final CombineRule<Float, FloatValue> GET_MAX_WITHIN_0_TO_1 = CombineRule.register(new CombineRule<>() {
        @Override
        public Float combine(Float componentA, Float componentB) {
            return Math.max(componentA, componentB);
        }

        @Override
        public String name() {
            return "float_get_max_within_0_to_1";
        }
    });
    public static final CombineRule<Float, FloatValue> ADDITION = CombineRule.register(new CombineRule<>() {
        @Override
        public Float combine(Float componentA, Float componentB) {
            return componentA + componentB;
        }

        @Override
        public String name() {
            return "float_addition";
        }
    });
    public static final CombineRule<Float, FloatValue> ADDITION_WITHIN_0_TO_1 = CombineRule.register(new CombineRule<>() {
        @Override
        public Float combine(Float componentA, Float componentB) {
            return Mth.clamp(componentA + componentB, 0.0F, 1.0F);
        }

        @Override
        public String name() {
            return "float_addition_within_0_to_1";
        }
    });
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
