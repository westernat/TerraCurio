package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.PlayerClimbHandler;
import org.jetbrains.annotations.NotNull;

public record PlayerClimbPacketS2C(int climberAmount) implements CustomPacketPayload {
    public static final Type<PlayerClimbPacketS2C> TYPE = new Type<>(TerraCurio.asResource("player_climb"));
    public static final StreamCodec<ByteBuf, PlayerClimbPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.climberAmount,
            PlayerClimbPacketS2C::new
    );

    @Override
    public @NotNull Type<PlayerClimbPacketS2C> type() {
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
}
