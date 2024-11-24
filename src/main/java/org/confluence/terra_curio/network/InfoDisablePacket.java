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
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.jetbrains.annotations.NotNull;

import static org.confluence.terra_curio.network.s2c.InfoCurioCheckPacketS2C.ARRAY_LENGTH;

public record InfoDisablePacket(boolean[] disables) implements CustomPacketPayload {
    public static final Type<InfoDisablePacket> TYPE = new Type<>(TerraCurio.asResource("info_disable"));
    public static final StreamCodec<ByteBuf, InfoDisablePacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull InfoDisablePacket decode(@NotNull ByteBuf buffer) {
            byte[] bytes = new byte[ARRAY_LENGTH];
            buffer.readBytes(bytes);
            boolean[] disables = new boolean[ARRAY_LENGTH];
            for (int i = 0; i < ARRAY_LENGTH; i++) {
                disables[i] = bytes[i] != 0;
            }
            return new InfoDisablePacket(disables);
        }

        @Override
        public void encode(@NotNull ByteBuf buffer, @NotNull InfoDisablePacket value) {
            byte[] bytes = new byte[ARRAY_LENGTH];
            for (int i = 0; i < ARRAY_LENGTH; i++) {
                bytes[i] = (byte) (value.disables[i] ? 1 : 0);
            }
            buffer.writeBytes(bytes);
        }
    };

    @Override
    public @NotNull Type<InfoDisablePacket> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                System.arraycopy(disables, 0, InformationHandler.DISABLE, 0, ARRAY_LENGTH);
            } else if (context.player() instanceof ServerPlayer serverPlayer) {
                ByteArrayTag arrayTag = new ByteArrayTag(new byte[ARRAY_LENGTH]);
                for (int i = 0; i < ARRAY_LENGTH; i++) {
                    arrayTag.set(i, ByteTag.valueOf(disables[i]));
                }
                serverPlayer.getPersistentData().put("terra_curio:info_disable", arrayTag);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        byte[] bytes = serverPlayer.getPersistentData().getByteArray("terra_curio:info_disable");
        if (bytes.length != ARRAY_LENGTH) return;
        boolean[] disables = new boolean[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            disables[i] = bytes[i] != 0;
        }
        PacketDistributor.sendToPlayer(serverPlayer, new InfoDisablePacket(disables));
    }

    public static void sendToServer(boolean[] disables) {
        PacketDistributor.sendToServer(new InfoDisablePacket(disables));
    }
}
