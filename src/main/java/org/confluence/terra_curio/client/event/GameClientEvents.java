package org.confluence.terra_curio.client.event;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.event.PerformJumpingEvent;
import org.confluence.terra_curio.client.animate.ExpertColorAnimation;
import org.confluence.terra_curio.client.animate.MasterColorAnimation;
import org.confluence.terra_curio.client.handler.*;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.common.init.TCTags;
import org.confluence.terra_curio.mixin.client.accessor.MinecraftAccessor;

@EventBusSubscriber(modid = TerraCurio.MODID, value = Dist.CLIENT)
public final class GameClientEvents {
    @SubscribeEvent
    public static void clientTick$Post(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        GravitationHandler.tick(localPlayer);
        if (localPlayer == null) return;
        TCClientPacketHandler.applyAutoAttack(minecraft, localPlayer);
        InformationHandler.handle(localPlayer);

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
        } else if (GravitationHandler.isHasGlobe() || localPlayer.hasEffect(TCEffects.GRAVITATION)) {
            GravitationHandler.handle(localPlayer, jumping);
        } else {
            GravitationHandler.expire();
            PlayerJumpHandler.handle(localPlayer, jumping);
            PlayerClimbHandler.handle(localPlayer, input.getMoveVector(), jumping);
        }
        if (TCClientPacketHandler.isHasTabi()) PlayerSprintingHandler.handle(localPlayer, input);
    }

    @SubscribeEvent
    public static void fov(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        if (TCClientPacketHandler.isHasScope() && player.isCrouching() &&
                Minecraft.getInstance().options.getCameraType().isFirstPerson() &&
                player.getItemInHand(InteractionHand.MAIN_HAND).is(TCTags.RANGED_WEAPON)
        ) event.setNewFovModifier(0.1F);
    }

    @SubscribeEvent
    public static void interactionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isUseItem()) {
            MinecraftAccessor instance = (MinecraftAccessor) Minecraft.getInstance();
            int delay = instance.getRightClickDelay() - TCClientPacketHandler.getRightClickSubtractor();
            instance.setRightClickDelay(Math.max(0, delay));
        }
    }
}
