package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;

public record ByteValue(byte value) implements PrimitiveValue<Byte> {
    public static final Codec<ByteValue> CODEC = Codec.BYTE.xmap(ByteValue::new, ByteValue::get);
    public static final CombineRule<Byte, ByteValue> ADDITION_WITHIN_0_TO_2 = CombineRule.register((a, b) -> (byte) Mth.clamp(a + b, 0, 2), "byte_addition_within_1_to_2");

    @Override
    public Byte get() {
        return value;
    }

    @Override
    public Codec<ByteValue> codec() {
        return CODEC;
    }
}
