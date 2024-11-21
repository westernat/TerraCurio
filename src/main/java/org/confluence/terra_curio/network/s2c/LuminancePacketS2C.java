package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.NotNull;

public record LuminancePacketS2C(int playerId, int luminance) implements CustomPacketPayload {
    public static final Type<LuminancePacketS2C> TYPE = new Type<>(TerraCurio.asResource("luminance"));
    public static final StreamCodec<ByteBuf, LuminancePacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.playerId,
            ByteBufCodecs.INT, p -> p.luminance,
            LuminancePacketS2C::new
    );

    @Override
    public @NotNull Type<LuminancePacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                TCClientPacketHandler.handleLuminance(this, context.player());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToAll(ServerPlayer serverPlayer) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            Integer luminance = TCUtils.getAccessoriesValue(serverPlayer, ValueType.LUMINANCE);
            PacketDistributor.sendToAllPlayers(new LuminancePacketS2C(serverPlayer.getId(), luminance));
        }
    }
}
