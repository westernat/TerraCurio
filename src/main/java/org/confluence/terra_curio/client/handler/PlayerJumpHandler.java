package org.confluence.terra_curio.client.handler;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.terra_curio.common.init.TCSoundEvents;
import org.confluence.terra_curio.integration.airhop.AirHopHelper;
import org.confluence.terra_curio.mixin.accessor.LivingEntityAccessor;
import org.confluence.terra_curio.network.c2s.PlayerJumpPacketC2S;
import org.confluence.terra_curio.network.s2c.InfiniteFlightPacketS2C;
import org.confluence.terra_curio.network.s2c.PlayerFlyPacketS2C;
import org.confluence.terra_curio.network.s2c.PlayerJumpPacketS2C;

import static org.confluence.terra_curio.network.c2s.PlayerJumpPacketC2S.JUMP_BY_SELF;
import static org.confluence.terra_curio.network.c2s.PlayerJumpPacketC2S.RESET_FALL_DISTANCE;

@OnlyIn(Dist.CLIENT)
public final class PlayerJumpHandler {
    private static boolean jumpKeyDown = true;

    private static double fartSpeed = 0.0;
    private static boolean fartFinished = false;

    private static double sandstormSpeed = 0.0;
    private static int maxSandstormTicks = 0;
    private static int remainSandstormTicks = 0;
    private static boolean sandstormFinished = false;
    public static boolean isOnSandstormJump = false;

    private static double blizzardSpeed = 0.0;
    private static int maxBlizzardTicks = 0;
    private static int remainBlizzardTicks = 0;
    private static boolean blizzardFinished = false;
    public static boolean isOnBlizzardJump = false;

    private static double tsunamiSpeed = 0.0;
    private static boolean tsunamiFinished = false;
    public static boolean isOnTsunamiJump = false;

    private static double cloudSpeed = 0.0;
    private static boolean cloudFinished = false;
    public static boolean isOnCloudJump = false;

    private static double flySpeed = 0.0;
    private static int maxFlyTicks = 0;
    private static int remainFlyTicks = 0;
    private static boolean couldGlide = false;
    private static boolean horizontalFlight = false;
    static boolean infiniteFlight = false;

    public static boolean onFly = false;

    public static void handle(LocalPlayer localPlayer, boolean jumping) {
        if (StepStoolHandler.onStool()) return;

        if (localPlayer.onGround()) {
            reset(true);
        } else if (jumping) {
            if (AirHopHelper.LOADED && AirHopHelper.notFinishJump(localPlayer)) {
                jumpKeyDown = true;
                return;
            }

            if (couldGlide) {
                if (infiniteFlight || remainFlyTicks > 0) {
                    if (!horizontalFlight || localPlayer.level().getGameTime() % 2 == 0) {
                        remainFlyTicks--;
                    }
                    onFly = true;
                    if (horizontalFlight && localPlayer.isShiftKeyDown()) {
                        horizontalFlight(localPlayer);
                    } else {
                        fly(localPlayer, flySpeed);
                    }
                } else if (!localPlayer.getAbilities().flying && localPlayer.getDeltaMovement().y < -0.15) {
                    onFly = false;
                    glide(localPlayer);
                }
            }
            if (jumpKeyDown || (couldGlide && remainFlyTicks > 0)) return;

            if (!fartFinished && fartSpeed > 0.0) {
                fartFinished = true;
                jumpKeyDown = true;
                multiJump(localPlayer, fartSpeed);
                localPlayer.playSound(TCSoundEvents.FART_SOUND.get());
            } else if (!sandstormFinished && sandstormSpeed > 0.0) {
                if (remainSandstormTicks-- > 0) {
                    oneTimeJump(localPlayer, sandstormSpeed);
                    isOnSandstormJump = true;
                } else {
                    jumpKeyDown = true;
                    isOnSandstormJump = false;
                }
            } else if (!blizzardFinished && blizzardSpeed > 0.0) {
                if (remainBlizzardTicks-- > 0) {
                    oneTimeJump(localPlayer, blizzardSpeed);
                    isOnBlizzardJump = true;
                } else {
                    jumpKeyDown = true;
                    isOnBlizzardJump = false;
                }
            } else if (!tsunamiFinished && tsunamiSpeed > 0.0) {
                tsunamiFinished = true;
                isOnTsunamiJump = true;
                jumpKeyDown = true;
                multiJump(localPlayer, tsunamiSpeed);
                localPlayer.playSound(TCSoundEvents.DOUBLE_JUMP.get());
            } else if (!cloudFinished && cloudSpeed > 0.0) {
                cloudFinished = true;
                isOnCloudJump = true;
                jumpKeyDown = true;
                multiJump(localPlayer, cloudSpeed);
                localPlayer.playSound(TCSoundEvents.DOUBLE_JUMP.get());
            } else if (infiniteFlight || remainFlyTicks-- > 0) {
                onFly = true;
                if (horizontalFlight) {
                    horizontalFlight(localPlayer);
                } else {
                    fly(localPlayer, flySpeed);
                }
            } else {
                jumpKeyDown = true;
            }
        } else {
            jumpKeyDown = false;
            sandstormFinished = remainSandstormTicks < maxSandstormTicks;
            blizzardFinished = remainBlizzardTicks < maxBlizzardTicks;
            isOnSandstormJump = false;
            isOnBlizzardJump = false;
            onFly = false;
        }
    }

    public static void reset(boolean jumpKey) {
        jumpKeyDown = jumpKey;
        fartFinished = false;
        remainSandstormTicks = maxSandstormTicks;
        sandstormFinished = false;
        remainBlizzardTicks = maxBlizzardTicks;
        blizzardFinished = false;
        tsunamiFinished = false;
        cloudFinished = false;
        remainFlyTicks = maxFlyTicks;
    }

    private static void multiJump(LocalPlayer localPlayer, double speed) {
        Vec3 vec3 = localPlayer.getDeltaMovement();
        double motionY = ((LivingEntityAccessor) localPlayer).callGetJumpPower() * speed;
        localPlayer.setDeltaMovement(vec3.x, motionY, vec3.z);
        if (localPlayer.isSprinting()) {
            float f = localPlayer.getYRot() * Mth.DEG_TO_RAD;
            localPlayer.setDeltaMovement(localPlayer.getDeltaMovement().add(-Mth.sin(f) * 0.2, 0.0, Mth.cos(f) * 0.2));
        }
        localPlayer.hasImpulse = true;
        localPlayer.resetFallDistance();
        PacketDistributor.sendToServer(new PlayerJumpPacketC2S((byte) (JUMP_BY_SELF | RESET_FALL_DISTANCE), (float) speed));
    }

    private static void oneTimeJump(LocalPlayer localPlayer, double speed) {
        Vec3 vec3 = localPlayer.getDeltaMovement();
        localPlayer.setDeltaMovement(vec3.x, speed, vec3.z);
        localPlayer.hasImpulse = true;
        localPlayer.resetFallDistance();
        PacketDistributor.sendToServer(new PlayerJumpPacketC2S(RESET_FALL_DISTANCE, (float) speed));
    }

    private static void fly(LocalPlayer localPlayer, double speed) {
        double y = localPlayer.getDeltaMovement().y;
        if (y < speed) {
            y += speed / 2.5;
        } else {
            y = speed;
        }
        airMove(localPlayer, y, localPlayer.getSpeed() + (float) speed);
    }

    private static void glide(LocalPlayer localPlayer) {
        airMove(localPlayer, -0.3, localPlayer.getSpeed() + 0.4F);
    }

    private static void horizontalFlight(LocalPlayer localPlayer) {
        float speed = (float) flySpeed;
        airMove(localPlayer, 0.0, Math.min(localPlayer.getSpeed() * 4.0F + speed - 0.5F, speed + speed));
    }

    private static void airMove(LocalPlayer localPlayer, double y, float h) {
        float rad = localPlayer.getYRot() * Mth.DEG_TO_RAD;
        float cos = Mth.cos(rad);
        float sin = Mth.sin(rad);
        float v = h * 0.15F;
        float x = localPlayer.xxa * v;
        float z = localPlayer.zza * v;
        double mx = x * cos + z * -sin;
        double mz = x * sin + z * cos;
        Vec3 motion = localPlayer.getDeltaMovement();
        localPlayer.setDeltaMovement(motion.x + mx, y, motion.z + mz);
        localPlayer.hasImpulse = true;
        localPlayer.resetFallDistance();
        PacketDistributor.sendToServer(new PlayerJumpPacketC2S(RESET_FALL_DISTANCE, (float) y));
    }

    public static void handleJumpPacket(PlayerJumpPacketS2C packet) {
        if (packet.fartSpeed() > -1.5) fartSpeed = packet.fartSpeed();
        sandstormSpeed = packet.sandstormSpeed();
        maxSandstormTicks = packet.sandstormTicks();
        blizzardSpeed = packet.blizzardSpeed();
        maxBlizzardTicks = packet.blizzardTicks();
        if (packet.tsunamiSpeed() > -1.5) tsunamiSpeed = packet.tsunamiSpeed();
        cloudSpeed = packet.cloudSpeed();
    }

    public static void handleFlyPacket(PlayerFlyPacketS2C packet) {
        flySpeed = packet.flySpeed();
        maxFlyTicks = packet.flyTicks();
        couldGlide = packet.couldGlide();
        horizontalFlight = packet.horizontalFlight();
    }

    public static void handleInfiniteFlight(InfiniteFlightPacketS2C packet) {
        infiniteFlight = packet.enable();
    }

    public static boolean isOnHorizontalFlight() {
        return onFly && horizontalFlight;
    }

    public static boolean isInfiniteFlight() {
        return infiniteFlight;
    }
}
