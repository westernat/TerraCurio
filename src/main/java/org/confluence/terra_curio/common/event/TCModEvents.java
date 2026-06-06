package org.confluence.terra_curio.common.event;

import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCCommonConfigs;
import org.confluence.terra_curio.network.InfoDisablePacket;
import org.confluence.terra_curio.network.c2s.*;
import org.confluence.terra_curio.network.s2c.*;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.lifecycle.PortFMLCommonSetupEvent;
import org.mesdag.portlib.network.PortNetworkHandler;

public final class TCModEvents {
    public static void init() {
        PortEventHandler.addListener(TCModEvents::commonSetup);
        PortEventHandler.addListener(TCModEvents::modConfig$Loading);
        PortEventHandler.addListener(TCModEvents::modConfig$Reloading);
        registerPayloadHandlers();
    }

    private static void commonSetup(PortFMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
//            NeoForgeMod.enableMergedAttributeTooltips();
        });
    }

    private static void modConfig$Loading(ModConfigEvent.Loading event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON && TerraCurio.MODID.equals(event.getConfig().getModId())) {
            TCCommonConfigs.onLoad();
        }
    }

    private static void modConfig$Reloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON && TerraCurio.MODID.equals(event.getConfig().getModId())) {
            TCCommonConfigs.onLoad();
        }
    }

    private static void registerPayloadHandlers() {
        PortNetworkHandler handler = TerraCurio.HANDLER;
        handler.registerInGameC2S(GravitationPacketC2S.class, GravitationPacketC2S.ID, GravitationPacketC2S.STREAM_CODEC);
        handler.registerInGameC2S(StepStoolSteppingPacketC2S.class, StepStoolSteppingPacketC2S.ID, StepStoolSteppingPacketC2S.STREAM_CODEC);
        handler.registerInGameC2S(PlayerJumpPacketC2S.class, PlayerJumpPacketC2S.ID, PlayerJumpPacketC2S.STREAM_CODEC);
        handler.registerInGameC2S(SpeedBootsNBTPacketC2S.class, SpeedBootsNBTPacketC2S.ID, SpeedBootsNBTPacketC2S.STREAM_CODEC);
        handler.registerInGameC2S(PlayerSprintPacketC2S.class, PlayerSprintPacketC2S.ID, PlayerSprintPacketC2S.STREAM_CODEC);
        handler.registerInGameC2S(ShootXBonePacketC2S.class, ShootXBonePacketC2S.ID, ShootXBonePacketC2S.STREAM_CODEC);
        handler.registerInGameC2S(RamRuneFallPacketC2S.class, RamRuneFallPacketC2S.ID, RamRuneFallPacketC2S.STREAM_CODEC);

        handler.registerInGameS2C(BroadcastGravitationRotPacketS2C.class, BroadcastGravitationRotPacketS2C.ID, BroadcastGravitationRotPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(CurioExistsPacketS2C.class, CurioExistsPacketS2C.ID, CurioExistsPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(EntityKilledPacketS2C.class, EntityKilledPacketS2C.ID, EntityKilledPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(InfoCurioCheckPacketS2C.class, InfoCurioCheckPacketS2C.ID, InfoCurioCheckPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(StepStoolSteppingPacketS2C.class, StepStoolSteppingPacketS2C.ID, StepStoolSteppingPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(PlayerJumpPacketS2C.class, PlayerJumpPacketS2C.ID, PlayerJumpPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(PlayerFlyPacketS2C.class, PlayerFlyPacketS2C.ID, PlayerFlyPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(PlayerClimbPacketS2C.class, PlayerClimbPacketS2C.ID, PlayerClimbPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(RightClickSubtractorPacketS2C.class, RightClickSubtractorPacketS2C.ID, RightClickSubtractorPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(SetItemEntityPickupDelayPacketS2C.class, SetItemEntityPickupDelayPacketS2C.ID, SetItemEntityPickupDelayPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(BroadcastRenderPacketS2C.class, BroadcastRenderPacketS2C.ID, BroadcastRenderPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(InfiniteFlightPacketS2C.class, InfiniteFlightPacketS2C.ID, InfiniteFlightPacketS2C.STREAM_CODEC);
        handler.registerInGameS2C(FluidWalkUpdatePacketS2C.class, FluidWalkUpdatePacketS2C.ID, FluidWalkUpdatePacketS2C.STREAM_CODEC);

        handler.registerInGameBidirectional(InfoDisablePacket.class, InfoDisablePacket.ID, InfoDisablePacket.STREAM_CODEC);
    }
}
