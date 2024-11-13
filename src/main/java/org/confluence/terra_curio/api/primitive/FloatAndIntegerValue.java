package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Tuple;

public record FloatAndIntegerValue(Tuple<Float, Integer> value) implements PrimitiveValue<Tuple<Float, Integer>> {
    public static final Codec<FloatAndIntegerValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("float_value").forGetter(v -> v.value.getA()),
            ExtraCodecs.POSITIVE_INT.fieldOf("integer_value").forGetter(v -> v.value.getB())
    ).apply(instance, (speed, ticks) -> new FloatAndIntegerValue(new Tuple<>(speed, ticks))));
    public static final CombineRule<Tuple<Float, Integer>, FloatAndIntegerValue> GET_SELF = CombineRule.register(new CombineRule<>() {
        @Override
        public Tuple<Float, Integer> combine(Tuple<Float, Integer> componentA, Tuple<Float, Integer> componentB) {
            return componentA;
        }

        @Override
        public String name() {
            return "float_and_integer_get_self";
        }
    });
    public static final CombineRule<Tuple<Float, Integer>, FloatAndIntegerValue> GET_EACH_MAX = CombineRule.register(new CombineRule<>() {
        @Override
        public Tuple<Float, Integer> combine(Tuple<Float, Integer> componentA, Tuple<Float, Integer> componentB) {
            return new Tuple<>(Math.max(componentA.getA(), componentB.getA()), Math.max(componentA.getB(), componentB.getB()));
        }

        @Override
        public String name() {
            return "float_and_integer_get_each_max";
        }
    });

    @Override
    public Tuple<Float, Integer> get() {
        return value;
    }

    @Override
    public Codec<FloatAndIntegerValue> codec() {
        return CODEC;
    }
}
