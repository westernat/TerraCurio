package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.util.TCUtils;

public record FluidWalkUpdatePacketS2C() implements CustomPacketPayload {
    public static final Type<FluidWalkUpdatePacketS2C> TYPE = new Type<>(TerraCurio.asResource("fluid_walk_update"));
    private static final FluidWalkUpdatePacketS2C INSTANCE = new FluidWalkUpdatePacketS2C();
    public static final StreamCodec<ByteBuf, FluidWalkUpdatePacketS2C> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<FluidWalkUpdatePacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                TCUtils.updateWalkableFluidStates(context.player());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        TCUtils.updateWalkableFluidStates(serverPlayer);
        PacketDistributor.sendToPlayer(serverPlayer, INSTANCE);
    }
}
