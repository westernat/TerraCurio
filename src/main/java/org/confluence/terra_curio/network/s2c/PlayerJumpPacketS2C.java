package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.lib.util.LibStreamCodecUtils;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import org.confluence.terra_curio.common.attachment.AccessoriesAttachment;
import org.confluence.terra_curio.common.init.TCAttachments;
import org.confluence.terra_curio.common.init.TCItems;

public record PlayerJumpPacketS2C(float fartSpeed, float sandstormSpeed, int sandstormTicks, float blizzardSpeed, int blizzardTicks, float tsunamiSpeed, float cloudSpeed) implements CustomPacketPayload {
    public static final Type<PlayerJumpPacketS2C> TYPE = new Type<>(TerraCurio.asResource("player_jump_s2c"));
    public static final StreamCodec<ByteBuf, PlayerJumpPacketS2C> STREAM_CODEC = LibStreamCodecUtils.composite(
            ByteBufCodecs.FLOAT, PlayerJumpPacketS2C::fartSpeed,
            ByteBufCodecs.FLOAT, PlayerJumpPacketS2C::sandstormSpeed,
            ByteBufCodecs.VAR_INT, PlayerJumpPacketS2C::sandstormTicks,
            ByteBufCodecs.FLOAT, PlayerJumpPacketS2C::blizzardSpeed,
            ByteBufCodecs.VAR_INT, PlayerJumpPacketS2C::blizzardTicks,
            ByteBufCodecs.FLOAT, PlayerJumpPacketS2C::tsunamiSpeed,
            ByteBufCodecs.FLOAT, PlayerJumpPacketS2C::cloudSpeed,
            PlayerJumpPacketS2C::new
    );

    @Override
    public Type<PlayerJumpPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                PlayerJumpHandler.handleJumpPacket(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        AccessoriesAttachment attachment = serverPlayer.getData(TCAttachments.ACCESSORIES);
        Tuple<Float, Integer> sandStorm = attachment.getValue(TCItems.SAND$STORM);
        Tuple<Float, Integer> blizzard = attachment.getValue(TCItems.BLIZZARD);
        PacketDistributor.sendToPlayer(serverPlayer, new PlayerJumpPacketS2C(
                attachment.getValue(TCItems.FART),
                sandStorm.getA(), sandStorm.getB(),
                blizzard.getA(), blizzard.getB(),
                attachment.getValue(TCItems.TSUNAMI),
                attachment.getValue(TCItems.CLOUD)
        ));
    }
}
