package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;

public record SetItemEntityPickupDelayPacketS2C(int id, int delay) implements CustomPacketPayload {
    public static final Type<SetItemEntityPickupDelayPacketS2C> TYPE = new Type<>(TerraCurio.asResource("set_item_entity_pickup_delay"));
    public static final StreamCodec<ByteBuf, SetItemEntityPickupDelayPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.id,
            ByteBufCodecs.INT, p -> p.delay,
            SetItemEntityPickupDelayPacketS2C::new
    );

    @Override
    public Type<SetItemEntityPickupDelayPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                TCClientPacketHandler.handleItemPickupDelay(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToAll(int id, int delay) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            PacketDistributor.sendToAllPlayers(new SetItemEntityPickupDelayPacketS2C(id, delay));
        } else {
            TerraCurio.LOGGER.warn("Trying send a packet with no server!");
        }
    }
}
