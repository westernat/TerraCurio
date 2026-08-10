package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.lib.network.IPacketS2C;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.renderer.accessory.LayeredGeoRenderer;

public record FlushRenderLayerPacketS2C(int entityId) implements IPacketS2C {
    public static final Type<FlushRenderLayerPacketS2C> TYPE = new Type<>(TerraCurio.asResource("flush_render_layer"));
    public static final StreamCodec<ByteBuf, FlushRenderLayerPacketS2C> STREAM_CODEC = ByteBufCodecs.VAR_INT
            .map(FlushRenderLayerPacketS2C::new, FlushRenderLayerPacketS2C::entityId);

    @Override
    public void work(Player player) {
        LayeredGeoRenderer.handlePacket(player, entityId);
    }

    @Override
    public Type<FlushRenderLayerPacketS2C> type() {
        return TYPE;
    }

    public static void sendToPlayersTrackingEntityAndSelf(LivingEntity living) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(living, new FlushRenderLayerPacketS2C(living.getId()));
    }
}
