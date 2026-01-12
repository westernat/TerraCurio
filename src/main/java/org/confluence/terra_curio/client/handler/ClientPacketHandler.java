package org.confluence.terra_curio.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terra_curio.capability.ability.PlayerAbility;
import org.confluence.terra_curio.network.s2c.*;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class ClientPacketHandler {
    private static boolean autoAttack = false;
    private static boolean hasCthulhu = false;
    private static boolean hasTabi = false;
    private static boolean hasScope = false;
    private static int rightClickSubtractor = 0;
    private static boolean hasMagiluminescence = false;

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

    public static void handleSwing(AutoAttackPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> autoAttack = packet.autoAttack());
        context.setPacketHandled(true);
    }

    public static void handleCthulhu(ShieldOfCthulhuPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> hasCthulhu = packet.has());
        context.setPacketHandled(true);
    }

    public static void handleTabi(TabiPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> hasTabi = packet.has());
        context.setPacketHandled(true);
    }

    public static void handleFlush(FlushPlayerAbilityPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (packet.flush()) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    PlayerAbility.of(player).ifPresent(playerAbility -> playerAbility.flushAbility(player));
                }
            }
        });
        context.setPacketHandled(true);
    }

    public static void handleScope(ScopeEnablePacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> hasScope = packet.enable());
        context.setPacketHandled(true);
    }

    public static void handleSubstractor(RightClickSubtractorPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> rightClickSubtractor = packet.amount());
        context.setPacketHandled(true);
    }

    public static void handleMagiluminescence(MagiluminescencePacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> hasMagiluminescence = packet.has());
        context.setPacketHandled(true);
    }

    public static boolean isHasMagiluminescence() {
        return hasMagiluminescence;
    }
}
