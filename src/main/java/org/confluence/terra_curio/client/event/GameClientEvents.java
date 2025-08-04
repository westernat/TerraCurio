package org.confluence.terra_curio.client.event;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.lib.client.animate.ExpertColorAnimation;
import org.confluence.lib.client.animate.MasterColorAnimation;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.event.PerformJumpingEvent;
import org.confluence.terra_curio.client.TCClientConfigs;
import org.confluence.terra_curio.client.TCKeyBindings;
import org.confluence.terra_curio.client.handler.*;
import org.confluence.terra_curio.client.renderer.tooltip.MultiFunctionTooltip;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.mixin.client.accessor.MinecraftAccessor;
import org.confluence.terra_curio.network.c2s.ShootXBonePacketC2S;
import org.confluence.terra_curio.util.TCUtils;

@EventBusSubscriber(modid = TerraCurio.MODID, value = Dist.CLIENT)
public final class GameClientEvents {
    @SubscribeEvent
    public static void clientTick$Post(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            GravitationHandler.reset();
            StepStoolHandler.reset();
            TCClientPacketHandler.reset();
            InformationHandler.reset();
            PlayerJumpHandler.reset(true);
            PlayerClimbHandler.reset();
            PlayerSprintingHandler.reset();
            ScopeFovHandler.reset();
        } else {
            GravitationHandler.handle(localPlayer);
            StepStoolHandler.handle(localPlayer);
            TCClientPacketHandler.handle(minecraft, localPlayer);
            InformationHandler.handle(localPlayer);
            ScopeFovHandler.handle(localPlayer);
            TCUtils.applyCthulhuSprinting(TCKeyBindings.CTHULHU_SPRINTING.get().isDown(), localPlayer);
        }

        ExpertColorAnimation.INSTANCE.updateColor();
        MasterColorAnimation.INSTANCE.updateColor();
    }

    @SubscribeEvent
    public static void movementInputUpdate(MovementInputUpdateEvent event) {
        LocalPlayer localPlayer = (LocalPlayer) event.getEntity();
        Input input = event.getInput();
        boolean jumping = input.jumping;
        if (jumping && !localPlayer.mayFly() && !NeoForge.EVENT_BUS.post(new PerformJumpingEvent(localPlayer)).isCanPerform()) {
            input.jumping = false;
        } else if (GravitationHandler.isHasGlobe()) {
            GravitationHandler.handle(localPlayer, jumping);
        } else {
            MobEffectInstance effect = localPlayer.getEffect(TCEffects.GRAVITATION);
            if (effect == null) {
                GravitationHandler.expire();
                PlayerJumpHandler.handle(localPlayer, jumping);
                PlayerClimbHandler.handle(localPlayer, input.getMoveVector(), jumping);
            } else {
                if (effect.getAmplifier() > 0) {
                    GravitationHandler.force(localPlayer);
                } else {
                    GravitationHandler.handle(localPlayer, jumping);
                }
            }
        }
        if (TCClientPacketHandler.isHasTabi() /* confluence mixin here */) {
            PlayerSprintingHandler.handle(localPlayer, input);
        }
    }

    @SubscribeEvent
    public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
        if (GravitationHandler.isShouldRot()) {
            event.setRoll(180.0F);
        }
    }

    @SubscribeEvent
    public static void fov(ComputeFovModifierEvent event) {
        if (ScopeFovHandler.isScoping()) {
            event.setNewFovModifier(ScopeFovHandler.getFovModifier());
        }
    }

    @SubscribeEvent
    public static void interactionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (TCClientConfigs.rightClickDelay && event.isUseItem()) {
            MinecraftAccessor instance = (MinecraftAccessor) Minecraft.getInstance();
            int delay = instance.getRightClickDelay() - TCClientPacketHandler.getRightClickSubtractor();
            instance.setRightClickDelay(Math.max(0, delay));
        }
        if (TCClientPacketHandler.isBoneGlove() && Minecraft.getInstance().player.getMainHandItem().is(Tags.Items.TOOLS)) {
            PacketDistributor.sendToServer(ShootXBonePacketC2S.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void input$MouseScrolling(InputEvent.MouseScrollingEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && ScopeFovHandler.isScoping()) {
            ScopeFovHandler.handleScroll(player, event.getScrollDeltaY());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void screen$MouseScrolled$Pre(ScreenEvent.MouseScrolled.Pre event) {
        if (MultiFunctionTooltip.isShowing) {
            MultiFunctionTooltip.mouseScrollY -= (int) event.getScrollDeltaY();
        } else {
            MultiFunctionTooltip.mouseScrollY = 0;
        }
    }
}
