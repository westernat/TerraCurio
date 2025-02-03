package org.confluence.terra_curio.common.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

public record NbtComponent(CompoundTag nbt) implements DataComponentType<NbtComponent> {
    public static final Codec<NbtComponent> CODEC = Codec.withAlternative(CompoundTag.CODEC, TagParser.AS_CODEC).xmap(NbtComponent::new, NbtComponent::nbt);
    public static final StreamCodec<ByteBuf, NbtComponent> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(NbtComponent::new, NbtComponent::nbt);

    @Override
    public @Nullable Codec<NbtComponent> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, NbtComponent> streamCodec() {
        return STREAM_CODEC;
    }
}
