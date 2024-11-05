package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;

public record ByteValue(byte value) implements PrimitiveValue<Byte> {
    public static final Codec<ByteValue> CODEC = Codec.BYTE.xmap(ByteValue::new, ByteValue::get);
    public static final CombineRule<Byte, ByteValue> ADDITION_WITHIN_0_TO_4 = CombineRule.register(new CombineRule<>() {
        @Override
        public Byte combine(Byte componentA, Byte componentB) {
            return (byte) Mth.clamp(componentA + componentB, 0, 4);
        }

        @Override
        public String name() {
            return "byte_addition_within_1_to_4";
        }
    });

    @Override
    public Byte get() {
        return value;
    }

    @Override
    public Codec<ByteValue> codec() {
        return CODEC;
    }
}
