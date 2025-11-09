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
import org.confluence.terra_curio.client.handler.PlayerClimbHandler;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.util.TCUtils;

public record PlayerClimbPacketS2C(byte climberAmount) implements CustomPacketPayload {
    public static final Type<PlayerClimbPacketS2C> TYPE = new Type<>(TerraCurio.asResource("player_climb"));
    public static final StreamCodec<ByteBuf, PlayerClimbPacketS2C> STREAM_CODEC = ByteBufCodecs.BYTE.map(PlayerClimbPacketS2C::new, PlayerClimbPacketS2C::climberAmount);

    @Override
    public Type<PlayerClimbPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                PlayerClimbHandler.handlePacket(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer(serverPlayer, new PlayerClimbPacketS2C(TCUtils.getValue(serverPlayer, TCItems.WALL$CLIMB)));
    }
}
