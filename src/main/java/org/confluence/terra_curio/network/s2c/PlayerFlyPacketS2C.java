package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import org.jetbrains.annotations.NotNull;

public record PlayerFlyPacketS2C(int maxFlyTicks, float flySpeed, boolean glide) implements CustomPacketPayload {
    public static final Type<PlayerFlyPacketS2C> TYPE = new Type<>(TerraCurio.asResource("player_fly"));
    public static final StreamCodec<ByteBuf, PlayerFlyPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.maxFlyTicks,
            ByteBufCodecs.FLOAT, p -> p.flySpeed,
            ByteBufCodecs.BOOL, p -> p.glide,
            PlayerFlyPacketS2C::new
    );

    @Override
    public @NotNull Type<PlayerFlyPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                PlayerJumpHandler.handleFlyPacket(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        // todo
    }
}
