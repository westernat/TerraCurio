package org.confluence.mod.terra_curio.client.handler;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.mod.terra_curio.client.KeyBindings;
import org.confluence.mod.terra_curio.network.c2s.StepStoolSteppingPacketC2S;
import org.confluence.mod.terra_curio.network.s2c.StepStoolSteppingPacketS2C;

@OnlyIn(Dist.CLIENT)
public final class StepStoolHandler {
    private static boolean upKeyDown = false;
    private static boolean shiftKeyDown = false;
    private static int step = 0;
    private static int maxStep = 0;
    private static int slot = StepStoolSteppingPacketS2C.NO_CURIO;

    public static void handle(LocalPlayer localPlayer) {
        if (localPlayer == null) {
            step = 0;
            maxStep = 0;
            slot = StepStoolSteppingPacketS2C.NO_CURIO;
            return;
        } else if (slot == StepStoolSteppingPacketS2C.NO_CURIO || (step == 0 && !localPlayer.onGround())) {
            step = 0;
            return;
        }

        if (step > 0) {
            if (localPlayer.input.jumping) {
                localPlayer.jumpFromGround();
                setStep(0, false);
                return;
            } else if (localPlayer.getVehicle() != null) {
                setStep(0, false);
                return;
            }
        }

        if (KeyBindings.STEP_STOOL.get().isDown()) {
            if (!upKeyDown && step < maxStep) {
                setStep(step + 1, true);
                upKeyDown = true;
            }
        } else {
            upKeyDown = false;
        }

        if (!upKeyDown && localPlayer.isShiftKeyDown()) {
            if (!shiftKeyDown && step > 0) {
                setStep(step - 1, false);
                shiftKeyDown = true;
            }
        } else {
            shiftKeyDown = false;
        }

        if (step > 0) {
            localPlayer.setDeltaMovement(new Vec3(0.0, localPlayer.getDeltaMovement().y, 0.0));
        }
    }

    public static void setStep(int step, boolean increase) {
        StepStoolHandler.step = step;
        PacketDistributor.sendToServer(new StepStoolSteppingPacketC2S(slot, step, increase));
    }

    public static int getStep() {
        return step;
    }

    public static boolean onStool() {
        return step > 0;
    }

    public static void handlePacket(StepStoolSteppingPacketS2C packet) {
        if (packet.slot() == StepStoolSteppingPacketS2C.RESET_STEP) {
            step = 0;
        } else {
            maxStep = packet.maxStep();
            slot = maxStep == 0 ? StepStoolSteppingPacketS2C.NO_CURIO : packet.slot();
        }
    }
}
