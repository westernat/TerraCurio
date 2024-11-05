package org.confluence.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;

public record ByteValue(byte value) implements PrimitiveValue<Byte> {
    public static final Codec<ByteValue> CODEC = Codec.BYTE.xmap(ByteValue::new, ByteValue::get);

    @Override
    public Byte get() {
        return value;
    }

    @Override
    public Codec<ByteValue> codec() {
        return CODEC;
    }
}
