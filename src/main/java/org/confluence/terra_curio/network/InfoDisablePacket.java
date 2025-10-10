package org.confluence.terra_curio.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.lib.util.LibStreamCodecUtils;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.InformationHandler;

import static org.confluence.terra_curio.network.s2c.InfoCurioCheckPacketS2C.ARRAY_LENGTH;

@javax.annotation.ParametersAreNonnullByDefault
@net.minecraft.MethodsReturnNonnullByDefault
public record InfoDisablePacket(boolean[] disables) implements CustomPacketPayload {
    public static final Type<InfoDisablePacket> TYPE = new Type<>(TerraCurio.asResource("info_disable"));
    public static final StreamCodec<ByteBuf, InfoDisablePacket> STREAM_CODEC = LibStreamCodecUtils.booleanArray(ARRAY_LENGTH)
            .map(InfoDisablePacket::new, InfoDisablePacket::disables);

    @Override
    public Type<InfoDisablePacket> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                System.arraycopy(disables, 0, InformationHandler.DISABLE, 0, ARRAY_LENGTH);
            } else if (context.player() instanceof ServerPlayer player) {
                ByteArrayTag arrayTag = new ByteArrayTag(new byte[ARRAY_LENGTH]);
                for (int i = 0; i < ARRAY_LENGTH; i++) {
                    arrayTag.set(i, ByteTag.valueOf(disables[i]));
                }
                LibUtils.getOrCreatePersistedData(player).put("terra_curio:info_disable", arrayTag);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer player) {
        byte[] bytes = LibUtils.getOrCreatePersistedData(player).getByteArray("terra_curio:info_disable");
        if (bytes.length != ARRAY_LENGTH) return;
        boolean[] disables = new boolean[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            disables[i] = bytes[i] != 0;
        }
        PacketDistributor.sendToPlayer(player, new InfoDisablePacket(disables));
    }

    public static void sendToServer(boolean[] disables) {
        PacketDistributor.sendToServer(new InfoDisablePacket(disables));
    }
}
