package org.confluence.terra_curio.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.util.TCUtils;

public record PlayerSprintPacketC2S() implements CustomPacketPayload {
    public static final Type<PlayerSprintPacketC2S> TYPE = new Type<>(TerraCurio.asResource("player_sprint"));
    public static final StreamCodec<ByteBuf, PlayerSprintPacketC2S> STREAM_CODEC = StreamCodec.unit(new PlayerSprintPacketC2S());

    @Override
    public Type<PlayerSprintPacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                TCUtils.applyCthulhuSprinting(true, serverPlayer);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
