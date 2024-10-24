package org.confluence.mod.terra_curio.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public record PrimitiveComponent<T>(Optional<Integer> aInteger, Optional<Float> aFloat, Optional<Boolean> aBoolean, CombineRule<T> combineRule) implements DataComponentType<PrimitiveComponent<?>> {
    public static final Codec<PrimitiveComponent<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("integer").forGetter(PrimitiveComponent::aInteger),
            Codec.FLOAT.optionalFieldOf("float").forGetter(PrimitiveComponent::aFloat),
            Codec.BOOL.optionalFieldOf("boolean").forGetter(PrimitiveComponent::aBoolean),
            CombineRule.CODEC.fieldOf("combine_rule").forGetter(PrimitiveComponent::combineRule)
    ).apply(instance, PrimitiveComponent::new));
    public static final StreamCodec<FriendlyByteBuf, PrimitiveComponent<?>> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(FriendlyByteBuf buffer, @NotNull PrimitiveComponent<?> value) {
            buffer.writeJsonWithCodec(CODEC, value);
        }

        @Override
        @NotNull
        public PrimitiveComponent<?> decode(FriendlyByteBuf buffer) {
            return buffer.readJsonWithCodec(CODEC);
        }
    };

    public static PrimitiveComponent<Integer> ofInteger(int aInteger, CombineRule<Integer> combineRule) {
        return new PrimitiveComponent<>(Optional.of(aInteger), Optional.empty(), Optional.empty(), combineRule);
    }

    public static PrimitiveComponent<Float> ofFloat(float aFloat, CombineRule<Float> combineRule) {
        return new PrimitiveComponent<>(Optional.empty(), Optional.of(aFloat), Optional.empty(), combineRule);
    }

    public static PrimitiveComponent<Boolean> ofBoolean(boolean aBoolean, CombineRule<Boolean> combineRule) {
        return new PrimitiveComponent<>(Optional.empty(), Optional.empty(), Optional.of(aBoolean), combineRule);
    }

    @Override
    public Codec<PrimitiveComponent<?>> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<FriendlyByteBuf, PrimitiveComponent<?>> streamCodec() {
        return STREAM_CODEC;
    }

    public T combine(PrimitiveComponent<T> another, T defaultValue) {
        return combineRule.combine(this, another, defaultValue);
    }

    public T combineAll(Collection<PrimitiveComponent<T>> another, T defaultValue) {
        T value = defaultValue;
        for (PrimitiveComponent<T> component : another) {
            value = combine(component, value);
        }
        return value;
    }

    @Override
    public String toString() {
        return "PrimitivesComponent{" +
                "aInteger=" + aInteger +
                ", aFloat=" + aFloat +
                ", aBoolean=" + aBoolean +
                ", combineRule=" + combineRule +
                '}';
    }
}
