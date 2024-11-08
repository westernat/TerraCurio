package org.confluence.terra_curio.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCAttributes;
import org.confluence.terra_curio.common.init.TCCommonConfigs;
import org.confluence.terra_curio.network.c2s.GravitationPacketC2S;
import org.confluence.terra_curio.network.c2s.PlayerJumpPacketC2S;
import org.confluence.terra_curio.network.c2s.SpeedBootsNBTPacketC2S;
import org.confluence.terra_curio.network.c2s.StepStoolSteppingPacketC2S;
import org.confluence.terra_curio.network.s2c.*;

@EventBusSubscriber(modid = TerraCurio.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TCCommonConfigs.onLoad();
            TCAttributes.modifyAttributesUpperLimit();
        });
    }

    @SubscribeEvent
    public static void entityAttributeModification(EntityAttributeModificationEvent event) {
        TCAttributes.readJsonConfig();
        TCAttributes.registerAttribute(TCAttributes.CRIT_CHANCE, event::add);
        TCAttributes.registerAttribute(TCAttributes.RANGED_VELOCITY, event::add);
        TCAttributes.registerAttribute(TCAttributes.RANGED_DAMAGE, event::add);
        TCAttributes.registerAttribute(TCAttributes.DODGE_CHANCE, event::add);
        TCAttributes.registerAttribute(TCAttributes.AGGRO, event::add);
        if (TerraCurio.isConfluenceLoaded()) {
            TCAttributes.registerAttribute(TCAttributes.MAGIC_DAMAGE, event::add);
        }
        TCAttributes.registerAttribute(TCAttributes.ARMOR_PASS, event::add);
        TCAttributes.registerAttribute(TCAttributes.PICKUP_RANGE, event::add);
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(GravitationPacketC2S.TYPE, GravitationPacketC2S.STREAM_CODEC, GravitationPacketC2S::handle);
        registrar.playToServer(StepStoolSteppingPacketC2S.TYPE, StepStoolSteppingPacketC2S.STREAM_CODEC, StepStoolSteppingPacketC2S::handle);
        registrar.playToServer(PlayerJumpPacketC2S.TYPE, PlayerJumpPacketC2S.STREAM_CODEC, PlayerJumpPacketC2S::handle);
        registrar.playToServer(SpeedBootsNBTPacketC2S.TYPE, SpeedBootsNBTPacketC2S.STREAM_CODEC, SpeedBootsNBTPacketC2S::handle);

        registrar.playToClient(BroadcastGravitationRotPacketS2C.TYPE, BroadcastGravitationRotPacketS2C.STREAM_CODEC, BroadcastGravitationRotPacketS2C::handle);
        registrar.playToClient(CurioExistsPacketS2C.TYPE, CurioExistsPacketS2C.STREAM_CODEC, CurioExistsPacketS2C::handle);
        registrar.playToClient(AttackDamagePacketS2C.TYPE, AttackDamagePacketS2C.STREAM_CODEC, AttackDamagePacketS2C::handle);
        registrar.playToClient(WindSpeedPacketS2C.TYPE, WindSpeedPacketS2C.STREAM_CODEC, WindSpeedPacketS2C::handle);
        registrar.playToClient(EntityKilledPacketS2C.TYPE, EntityKilledPacketS2C.STREAM_CODEC, EntityKilledPacketS2C::handle);
        registrar.playToClient(InfoCurioCheckPacketS2C.TYPE, InfoCurioCheckPacketS2C.STREAM_CODEC, InfoCurioCheckPacketS2C::handle);
        registrar.playToClient(StepStoolSteppingPacketS2C.TYPE, StepStoolSteppingPacketS2C.STREAM_CODEC, StepStoolSteppingPacketS2C::handle);
        registrar.playToClient(PlayerJumpPacketS2C.TYPE, PlayerJumpPacketS2C.STREAM_CODEC, PlayerJumpPacketS2C::handle);
        registrar.playToClient(PlayerFlyPacketS2C.TYPE, PlayerFlyPacketS2C.STREAM_CODEC, PlayerFlyPacketS2C::handle);
        registrar.playToClient(PlayerClimbPacketS2C.TYPE, PlayerClimbPacketS2C.STREAM_CODEC, PlayerClimbPacketS2C::handle);
        registrar.playToClient(RightClickSubtractorPacketS2C.TYPE, RightClickSubtractorPacketS2C.STREAM_CODEC, RightClickSubtractorPacketS2C::handle);
    }
}
