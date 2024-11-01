package org.confluence.terra_curio.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SpeedBootsComponent(int speed) implements DataComponentType<SpeedBootsComponent> {
    public static final SpeedBootsComponent ZERO = new SpeedBootsComponent(0);
    public static final Codec<SpeedBootsComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("speed").orElse(0).forGetter(SpeedBootsComponent::speed)
    ).apply(instance, SpeedBootsComponent::new));
    public static final StreamCodec<FriendlyByteBuf, SpeedBootsComponent> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull SpeedBootsComponent decode(FriendlyByteBuf buffer) {
            return buffer.readJsonWithCodec(CODEC);
        }

        @Override
        public void encode(FriendlyByteBuf buffer, @NotNull SpeedBootsComponent value) {
            buffer.writeJsonWithCodec(CODEC, value);
        }
    };

    @Override
    public @Nullable Codec<SpeedBootsComponent> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<FriendlyByteBuf, SpeedBootsComponent> streamCodec() {
        return STREAM_CODEC;
    }
}
