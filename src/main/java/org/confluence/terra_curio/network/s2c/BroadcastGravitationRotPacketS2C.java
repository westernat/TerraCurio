package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.GravitationHandler;

public record BroadcastGravitationRotPacketS2C(int entityId, boolean enabled) implements CustomPacketPayload {
    public static final Type<BroadcastGravitationRotPacketS2C> TYPE = new Type<>(TerraCurio.asResource("broadcast_gravitation_rot"));
    public static final StreamCodec<ByteBuf, BroadcastGravitationRotPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.entityId,
            ByteBufCodecs.BOOL, p -> p.enabled,
            BroadcastGravitationRotPacketS2C::new
    );

    @Override
    public Type<BroadcastGravitationRotPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                GravitationHandler.handleRemoteRot(this, context.player());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
