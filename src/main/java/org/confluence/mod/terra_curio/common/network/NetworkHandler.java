package org.confluence.mod.terra_curio.common.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.network.c2s.GravitationPacketC2S;
import org.confluence.mod.terra_curio.common.network.s2c.BroadcastGravitationRotPacketS2C;

@EventBusSubscriber(modid = TerraCurio.MOD_ID,bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(TerraCurio.MOD_ID);


        registrar.playBidirectional(
                GravitationPacketC2S.TYPE,
                GravitationPacketC2S.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        GravitationPacketC2S::receive
                )
        );

        registrar.playBidirectional(
                BroadcastGravitationRotPacketS2C.TYPE,
                BroadcastGravitationRotPacketS2C.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        BroadcastGravitationRotPacketS2C::receive,
                        null
                )
        );

    }
}
