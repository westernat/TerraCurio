package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public record BroadcastGravitationRotPacketS2C(
        int entityId,
        boolean enabled
) implements IPortPacket.S2C {
    public static final ResourceLocation ID = TerraCurio.asResource("broadcast_gravitation_rot");
    public static final PortStreamCodec<ByteBuf, BroadcastGravitationRotPacketS2C> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.VAR_INT, BroadcastGravitationRotPacketS2C::entityId,
            PortByteBufCodecs.BOOL, BroadcastGravitationRotPacketS2C::enabled,
            BroadcastGravitationRotPacketS2C::new
    );

    @Override
    public ResourceLocation identifier() {
        return ID;
    }

    @Override
    public void work(Player player) {
        GravitationHandler.handleRemoteRot(this, player);
    }
}
