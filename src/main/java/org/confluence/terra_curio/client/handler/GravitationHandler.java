package org.confluence.terra_curio.client.handler;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.terra_curio.mixin.client.accessor.LocalPlayerAccessor;
import org.confluence.terra_curio.mixinauxi.IEntity;
import org.confluence.terra_curio.network.c2s.GravitationPacketC2S;
import org.confluence.terra_curio.network.s2c.BroadcastGravitationRotPacketS2C;

@OnlyIn(Dist.CLIENT)
public final class GravitationHandler {
    public static final Vec3 DOWN = new Vec3(0.0, -0.3000001, 0.0);
    private static boolean keyDown = false;
    private static boolean shouldRot = false;
    private static boolean hasGlobe = false;

    public static void handle(LocalPlayer localPlayer, boolean jumping) {
        if (StepStoolHandler.onStool() || localPlayer.getAbilities().flying) return;

        if (jumping) {
            if (!keyDown) {
                shouldRot = !shouldRot;
                localPlayer.resetFallDistance();
                PacketDistributor.sendToServer(new GravitationPacketC2S(shouldRot));
            }
            keyDown = true;
        } else {
            keyDown = false;
        }
    }

    public static void expire() {
        if (shouldRot) {
            shouldRot = false;
            PacketDistributor.sendToServer(new GravitationPacketC2S(false));
        }
    }

    public static boolean isShouldRot() {
        return shouldRot;
    }

    public static void handle(LocalPlayer localPlayer) {
        if (localPlayer.getY() > localPlayer.level().getMaxBuildHeight()) {
            expire();
        }
    }

    public static void reset() {
        shouldRot = false;
        hasGlobe = false;
    }

    public static void unCrouching(Player localPlayer) {
        if (shouldRot && localPlayer.onGround() && localPlayer.isCrouching() && !localPlayer.isShiftKeyDown()) {
            localPlayer.move(MoverType.SELF, DOWN);
            localPlayer.setPose(Pose.STANDING);
            ((LocalPlayerAccessor) localPlayer).setCrouching(false);
        }
    }

    static void setHasGlobe(boolean has) {
        hasGlobe = has;
    }

    public static boolean isHasGlobe() {
        return hasGlobe;
    }

    public static void handleRemoteRot(BroadcastGravitationRotPacketS2C packet, Player player) {
        Entity entity = player.level().getEntity(packet.entityId());
        if (entity != null) {
            ((IEntity) entity).confluence$setShouldRot(packet.enabled());
        }
    }
}
