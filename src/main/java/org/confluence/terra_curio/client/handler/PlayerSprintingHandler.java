package org.confluence.terra_curio.client.handler;

import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

public final class PlayerSprintingHandler {
    private static Movement xxa = Movement.NONE;
    private static Movement zza = Movement.NONE;
    private static int coolDown = 0;
    private static int sprintingTime = 0;
    private static boolean xxKeyDown = false;
    private static boolean zzKeyDown = false;

    public static void handle(LocalPlayer localPlayer, ClientInput input) {
        if (StepStoolHandler.onStool()) return;

        if (coolDown > 0) {
            coolDown--;
            return;
        }
        double x = 0.0;
        double z = 0.0;
        if (sprintingTime > 0) sprintingTime--;

        if (xxa == Movement.NONE) {
            if (input.keyPresses.left()) {
                xxa = Movement.LEFT;
                sprintingTime = 7;
                xxKeyDown = true;
            } else if (input.keyPresses.right()) {
                xxa = Movement.RIGHT;
                sprintingTime = 7;
                xxKeyDown = true;
            }
        } else if (xxKeyDown) {
            if (!input.keyPresses.left() && !input.keyPresses.right()) xxKeyDown = false;
        } else if (sprintingTime > 0) {
            if (xxa == Movement.LEFT && input.getMoveVector().x >= 0.8) x = 1.0;
            else if (xxa == Movement.RIGHT && input.getMoveVector().x <= -0.8) x = -1.0;
        } else if (sprintingTime == 0) {
            xxa = Movement.NONE;
        }

        if (zza == Movement.NONE) {
            if (input.keyPresses.forward()) {
                zza = Movement.UP;
                sprintingTime = 7;
                zzKeyDown = true;
            } else if (input.keyPresses.backward()) {
                zza = Movement.DOWN;
                sprintingTime = 7;
                zzKeyDown = true;
            }
        } else if (zzKeyDown) {
            if (!input.keyPresses.forward() && !input.keyPresses.backward()) zzKeyDown = false;
        } else if (sprintingTime > 0) {
            if (zza == Movement.UP && input.getMoveVector().y >= 0.8) z = 1.0;
            else if (zza == Movement.DOWN && input.getMoveVector().y <= -0.8) z = -1.0;
        } else if (sprintingTime == 0) {
            zza = Movement.NONE;
        }

        if (x != 0.0 || z != 0.0) {
            float rad = localPlayer.getYRot() * Mth.DEG_TO_RAD;
            float cos = Mth.cos(rad);
            float sin = Mth.sin(rad);
            double mx = x * cos + z * -sin;
            double mz = x * sin + z * cos;
            double factor = localPlayer.onGround() ? 1.5 : 1.1;
            localPlayer.setDeltaMovement(localPlayer.getDeltaMovement().add(factor * mx, 0.0, factor * mz));
            xxa = Movement.NONE;
            zza = Movement.NONE;
            coolDown = 32;
        }
    }

    public static void reset() {
        xxa = Movement.NONE;
        zza = Movement.NONE;
        coolDown = 0;
        sprintingTime = 0;
        xxKeyDown = false;
        zzKeyDown = false;
    }

    private enum Movement {
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
