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
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.util.TCUtils;

public record RightClickSubtractorPacketS2C(byte amount) implements CustomPacketPayload {
    public static final Type<RightClickSubtractorPacketS2C> TYPE = new Type<>(TerraCurio.asResource("right_click_sub"));
    public static final StreamCodec<ByteBuf, RightClickSubtractorPacketS2C> STREAM_CODEC = ByteBufCodecs.BYTE.map(RightClickSubtractorPacketS2C::new, RightClickSubtractorPacketS2C::amount);

    @Override
    public Type<RightClickSubtractorPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                TCClientPacketHandler.handleSubstractor(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer(serverPlayer, new RightClickSubtractorPacketS2C(TCUtils.getAccessoriesValue(serverPlayer, TCItems.RIGHT$CLICK$DELAY$SUBSTRACTOR)));
    }
}
