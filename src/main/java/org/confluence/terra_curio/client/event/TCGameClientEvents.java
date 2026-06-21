package org.confluence.terra_curio.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.common.Tags;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.TCClientConfigs;
import org.confluence.terra_curio.client.TCKeyBindings;
import org.confluence.terra_curio.client.handler.*;
import org.confluence.terra_curio.client.renderer.tooltip.MultiFunctionTooltip;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.mixin.client.accessor.MinecraftAccessor;
import org.confluence.terra_curio.network.c2s.ShootXBonePacketC2S;
import org.confluence.terra_curio.util.TCUtils;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.*;

public final class TCGameClientEvents {
    public static void init() {
        PortEventHandler.addListener(TCGameClientEvents::clientTick$Post);
        PortEventHandler.addListener(TCGameClientEvents::clientPlayerNetwork$LoggingOut);
        PortEventHandler.addListener(TCGameClientEvents::movementInputUpdate);
        PortEventHandler.addListener(TCGameClientEvents::cameraSetup);
        PortEventHandler.addListener(TCGameClientEvents::fov);
        PortEventHandler.addListener(TCGameClientEvents::interactionKeyMappingTriggered);
        PortEventHandler.addListener(TCGameClientEvents::input$MouseScrolling);
        PortEventHandler.addListener(TCGameClientEvents::screen$MouseScrolled$Pre);
    }

    private static void clientTick$Post(PortClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null) {
            GravitationHandler.tryExpire(player);
            StepStoolHandler.handle(player);
            TCClientPacketHandler.handle(minecraft, player);
            InformationHandler.handle(player);
            ScopeFovHandler.handle(player);
            TCUtils.applyCthulhuSprinting(TCKeyBindings.CTHULHU_SPRINTING.get().isDown(), player);
        }
    }

    private static void clientPlayerNetwork$LoggingOut(PortClientPlayerNetworkEvent.LoggingOut event) {
        GravitationHandler.reset();
        StepStoolHandler.reset();
        TCClientPacketHandler.reset();
        InformationHandler.reset();
        PlayerJumpHandler.reset(true);
        PlayerClimbHandler.reset();
        PlayerSprintingHandler.reset();
        ScopeFovHandler.reset();
    }

    private static void movementInputUpdate(MovementInputUpdateEvent event) {
        LocalPlayer player = (LocalPlayer) event.getEntity();
        Input input = event.getInput();
        boolean jumping = input.jumping;

        MobEffectInstance effect = player.getEffect(TCEffects.GRAVITATION.get());
        if (effect != null) {
            if (effect.getAmplifier() > 0) {
                GravitationHandler.force(player);
            } else {
                GravitationHandler.handle(player);
            }
        } else if (GravitationHandler.isHasGlobe()) {
            GravitationHandler.handle(player);
        } else {
            GravitationHandler.expire();
        }

        PlayerJumpHandler.handle(player, jumping);
        PlayerClimbHandler.handle(player, input.getMoveVector(), jumping);

        if (TCClientPacketHandler.isHasTabi() /* confluence mixin here */) {
            PlayerSprintingHandler.handle(player, input);
        }
    }

    private static void cameraSetup(PortViewportEvent.ComputeCameraAngles event) {
        if (GravitationHandler.isShouldRot()) {
            event.setRoll(180.0F);
        }
    }

    private static void fov(PortComputeFovModifierEvent event) {
        if (ScopeFovHandler.isScoping()) {
            event.setNewFovModifier(ScopeFovHandler.getFovModifier());
        }
    }

    private static void interactionKeyMappingTriggered(PortInputEvent.InteractionKeyMappingTriggered event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (TCClientConfigs.rightClickDelay && event.isUseItem() && player.getItemInHand(event.getHand()).getItem() instanceof BlockItem) {
            MinecraftAccessor instance = (MinecraftAccessor) Minecraft.getInstance();
            int delay = instance.getRightClickDelay() - TCClientPacketHandler.getRightClickSubtractor();
            instance.setRightClickDelay(Math.max(0, delay));
        }
        if (TCClientPacketHandler.isBoneGlove() && player.getMainHandItem().is(Tags.Items.TOOLS)) {
            TerraCurio.NETWORK_HANDLER.sendToServer(ShootXBonePacketC2S.INSTANCE);
        }
    }

    private static void input$MouseScrolling(PortInputEvent.MouseScrollingEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && ScopeFovHandler.isScoping()) {
            ScopeFovHandler.handleScroll(player, event.getScrollDeltaY());
            event.setCanceled(true);
        }
    }

    private static void screen$MouseScrolled$Pre(PortScreenEvent.PortMouseScrolled.Pre event) {
        if (MultiFunctionTooltip.isShowing) {
            MultiFunctionTooltip.mouseScrollY -= (int) event.getScrollDeltaY();
        } else {
            MultiFunctionTooltip.mouseScrollY = 0;
        }
    }
}
