package org.confluence.mod.terra_curio.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record EffectImmunities(List<Holder<MobEffect>> immunities) implements DataComponentType<EffectImmunities> {
    public static EffectImmunities EMPTY = new EffectImmunities(List.of());


    public static final Codec<EffectImmunities> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            MobEffect.CODEC.listOf().fieldOf("immunities").forGetter(EffectImmunities::immunities)
    ).apply(ins, EffectImmunities::new));


    public static final StreamCodec<FriendlyByteBuf, EffectImmunities> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(FriendlyByteBuf buffer, EffectImmunities value) {
            buffer.writeJsonWithCodec(CODEC, value);
        }

        @Override
        @NotNull
        public EffectImmunities decode(FriendlyByteBuf buffer) {
            return buffer.readJsonWithCodec(CODEC);
        }
    };

    public static EffectImmunities of(List<Holder<MobEffect>> effectImmunities) {
        return new EffectImmunities(effectImmunities);
    }

    @Nullable
    @Override
    public Codec<EffectImmunities> codec() {
        return CODEC;
    }

    @Override
    @NotNull
    public StreamCodec<FriendlyByteBuf, EffectImmunities> streamCodec() {
        return STREAM_CODEC;
    }

    public boolean contains(Holder<MobEffect> mobEffect) {
        return immunities.contains(mobEffect);
    }
}
