package org.confluence.mod.terra_curio.client.handler;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.mod.terra_curio.network.s2c.CurioExistsPacketS2C;
import org.confluence.mod.terra_curio.network.s2c.RightClickSubtractorPacketS2C;

@OnlyIn(Dist.CLIENT)
public final class ClientPacketHandler {
    private static boolean autoAttack = false;
    private static boolean hasCthulhu = false;
    private static boolean hasTabi = false;
    private static boolean hasScope = false;
    private static int rightClickSubtractor = 0;

    public static boolean couldAutoAttack() {
        return autoAttack;
    }

    public static boolean isHasCthulhu() {
        return hasCthulhu;
    }

    public static boolean isHasTabi() {
        return hasTabi;
    }

    public static boolean isHasScope() {
        return hasScope;
    }

    public static int getRightClickSubtractor() {
        return rightClickSubtractor;
    }

    public static void handleSubstractor(RightClickSubtractorPacketS2C packet) {
        rightClickSubtractor = packet.amount();
    }

    public static void handleCurioExists(CurioExistsPacketS2C packet) {
        int item = packet.item();
        boolean enable = packet.enable();
        if (item == CurioExistsPacketS2C.ALL) {
            autoAttack = enable;
            hasCthulhu = enable;
            hasTabi = enable;
            hasScope = enable;
        } else if (item == CurioExistsPacketS2C.AUTO_ATTACK) {
            autoAttack = packet.enable();
        } else if (item == CurioExistsPacketS2C.CTHULHU) {
            hasCthulhu = packet.enable();
        } else if (item == CurioExistsPacketS2C.TABI) {
            hasTabi = packet.enable();
        } else if (item == CurioExistsPacketS2C.SCOPE) {
            hasScope = packet.enable();
        }
    }
}
