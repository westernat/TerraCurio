package org.confluence.mod.terra_curio.client.event;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.api.event.PerformJumpingEvent;
import org.confluence.mod.terra_curio.client.animate.ExpertColorAnimation;
import org.confluence.mod.terra_curio.client.animate.MasterColorAnimation;
import org.confluence.mod.terra_curio.client.handler.*;
import org.confluence.mod.terra_curio.common.effect.ModEffects;

@EventBusSubscriber(modid = TerraCurio.MODID,value = Dist.CLIENT)
public final class GameClientEvents {
    @SubscribeEvent
    public static void clientTick$Post(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        GravitationHandler.tick(localPlayer);
        if (localPlayer == null) return;
        ClientPacketHandler.applyAutoAttack(minecraft, localPlayer);
        InformationHandler.handle(localPlayer);

        ExpertColorAnimation.INSTANCE.updateColor();
        MasterColorAnimation.INSTANCE.updateColor();
    }

    @SubscribeEvent
    public static void movementInputUpdate(MovementInputUpdateEvent event) {
        LocalPlayer localPlayer = (LocalPlayer) event.getEntity();
        Input input = event.getInput();
        boolean jumping = input.jumping;
        if (jumping && !localPlayer.mayFly() && NeoForge.EVENT_BUS.post(new PerformJumpingEvent(localPlayer)).isCanPerform()) {
            input.jumping = false;
        } else if (GravitationHandler.isHasGlobe() || localPlayer.hasEffect(ModEffects.GRAVITATION)) {
            GravitationHandler.handle(localPlayer, jumping);
        } else {
            GravitationHandler.expire();
            PlayerJumpHandler.handle(localPlayer, jumping);
            PlayerClimbHandler.handle(localPlayer, input.getMoveVector(), jumping);
        }
        if (ClientPacketHandler.isHasTabi()) PlayerSprintingHandler.handle(localPlayer, input);
    }
}
