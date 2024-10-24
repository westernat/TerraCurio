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

public record Combat(List<Holder<MobEffect>> immunities) implements DataComponentType<Combat> {
    public static Combat EMPTY = new Combat(List.of());


    public static final Codec<Combat> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            MobEffect.CODEC.listOf().fieldOf("immunities").forGetter(Combat::immunities)
    ).apply(ins, Combat::new));


    public static final StreamCodec<FriendlyByteBuf, Combat> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(FriendlyByteBuf buffer, Combat value) {
            buffer.writeJsonWithCodec(CODEC, value);

        }

        @Override
        @NotNull
        public Combat decode(FriendlyByteBuf buffer) {
            return buffer.readJsonWithCodec(CODEC);
        }
    };

    public static Combat of(List<Holder<MobEffect>> effectImmunities) {
        return new Combat(effectImmunities);
    }

    @Nullable
    @Override
    public Codec<Combat> codec() {
        return CODEC;
    }

    @Override
    @NotNull
    public StreamCodec<FriendlyByteBuf, Combat> streamCodec() {
        return STREAM_CODEC;
    }

    public boolean contains(Holder<MobEffect> mobEffect) {
        return immunities.contains(mobEffect);
    }
}
