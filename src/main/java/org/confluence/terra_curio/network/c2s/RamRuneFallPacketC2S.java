package org.confluence.terra_curio.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import org.confluence.lib.network.IPacketC2S;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.item.curio.combat.RamRune;

public enum RamRuneFallPacketC2S implements IPacketC2S {
    INSTANCE;

    public static final Type<RamRuneFallPacketC2S> TYPE = new Type<>(TerraCurio.asResource("ram_rune_fall"));
    public static final StreamCodec<ByteBuf, RamRuneFallPacketC2S> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<RamRuneFallPacketC2S> type() {
        return TYPE;
    }

    @Override
    public void work(ServerPlayer player) {
        RamRune.startFalling(player);
    }
}
