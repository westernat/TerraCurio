package org.confluence.terra_curio.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.capability.ability.AbilityProvider;
import org.confluence.terra_curio.capability.ability.PlayerAbility;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.confluence.terra_curio.misc.ModAttributes;
import org.confluence.terra_curio.network.s2c.InfoCurioCheckPacketS2C;
import org.confluence.terra_curio.util.ModUtils;

@Mod.EventBusSubscriber(modid = TerraCurio.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlayerEvents {
    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            InfoCurioCheckPacketS2C.send(serverPlayer, serverPlayer.getInventory());
            ModUtils.resetClientPacket(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.player.isLocalPlayer()) {
                GravitationHandler.unCrouching(event.player);
            }
        }
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        Player oldPlayer = event.getOriginal();
        Player neoPlayer = event.getEntity();
        oldPlayer.revive();

        PlayerAbility.of(oldPlayer).ifPresent(old -> neoPlayer.getCapability(AbilityProvider.CAPABILITY).ifPresent(neo -> neo.copyFrom(old)));

        if (neoPlayer instanceof ServerPlayer serverPlayer) {
            ModUtils.resetClientPacket(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (ModAttributes.hasCustomAttribute(ModAttributes.MINING_SPEED.get())) return;
        AttributeInstance attributeInstance = event.getEntity().getAttribute(ModAttributes.MINING_SPEED.get());
        if (attributeInstance == null) return;
        event.setNewSpeed(event.getNewSpeed() * (float) attributeInstance.getValue());
    }
}
