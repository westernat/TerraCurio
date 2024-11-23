package org.confluence.terra_curio.network;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.confluence.terra_curio.network.s2c.InfoCurioCheckPacketS2C;
import org.jetbrains.annotations.NotNull;

public record InfoDisablePacket(boolean[] disables) implements CustomPacketPayload {
    public static final Type<InfoDisablePacket> TYPE = new Type<>(TerraCurio.asResource("info_disable"));
    public static final StreamCodec<ByteBuf, InfoDisablePacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public InfoDisablePacket decode(ByteBuf buffer) {
            BooleanList list = new BooleanArrayList();
            for (int i = 0; i < InfoCurioCheckPacketS2C.ARRAY_LENGTH; i++) {
                list.add(buffer.readBoolean());
            }
            return new InfoDisablePacket(list.toArray(new boolean[InfoCurioCheckPacketS2C.ARRAY_LENGTH]));
        }

        @Override
        public void encode(ByteBuf buffer, InfoDisablePacket value) {
            for (int i = 0; i < InfoCurioCheckPacketS2C.ARRAY_LENGTH; i++) {
                buffer.writeBoolean(value.disables[i]);
            }
        }
    };

    @Override
    public @NotNull Type<InfoDisablePacket> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                System.arraycopy(disables, 0, InformationHandler.DISABLE, 0, InfoCurioCheckPacketS2C.ARRAY_LENGTH);
            } else if (context.player() instanceof ServerPlayer serverPlayer) {
                ListTag listTag = new ListTag();
                for (int i = 0; i < InfoCurioCheckPacketS2C.ARRAY_LENGTH; i++) {
                    listTag.add(NbtOps.INSTANCE.createBoolean(disables[i]));
                }
                serverPlayer.getPersistentData().put("terra_curio:info_disable", listTag);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        boolean[] disables = new boolean[InfoCurioCheckPacketS2C.ARRAY_LENGTH];
        ListTag listTag = serverPlayer.getPersistentData().getList("terra_curio:info_disable", Tag.TAG_BYTE);
        if (listTag.size() != InfoCurioCheckPacketS2C.ARRAY_LENGTH) return;
        for (int i = 0; i < InfoCurioCheckPacketS2C.ARRAY_LENGTH; i++) {
            disables[i] = listTag.get(i) != ByteTag.ZERO;
        }
        PacketDistributor.sendToPlayer(serverPlayer, new InfoDisablePacket(disables));
    }

    public static void sendToServer(boolean[] disables) {
        PacketDistributor.sendToServer(new InfoDisablePacket(disables));
    }
}
