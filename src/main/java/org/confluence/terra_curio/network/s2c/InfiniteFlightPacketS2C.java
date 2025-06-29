package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.util.TCUtils;

public record InfiniteFlightPacketS2C(boolean enable) implements CustomPacketPayload {
    public static final Type<InfiniteFlightPacketS2C> TYPE = new Type<>(TerraCurio.asResource("infinite_flight"));
    public static final StreamCodec<ByteBuf, InfiniteFlightPacketS2C> STREAM_CODEC = ByteBufCodecs.BOOL.map(InfiniteFlightPacketS2C::new, InfiniteFlightPacketS2C::enable);

    @Override
    public Type<InfiniteFlightPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                PlayerJumpHandler.handleInfiniteFlight(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        boolean enable = TCUtils.hasAccessoriesType(serverPlayer, TCItems.INFINITE$FLIGHT);
        PacketDistributor.sendToPlayer(serverPlayer, new InfiniteFlightPacketS2C(enable));
    }
}
