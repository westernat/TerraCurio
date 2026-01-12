package org.confluence.terra_curio.item.curio.combat;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import org.confluence.terra_curio.network.NetworkHandler;
import org.confluence.terra_curio.network.s2c.ScopeEnablePacketS2C;
import org.confluence.terra_curio.util.CuriosUtils;

public interface IScope {
    Component TOOLTIP = Component.translatable("curios.tooltip.scope");
    Component TOOLTIP2 = Component.translatable("curios.tooltip.scope2");

    static void sendMsg(ServerPlayer serverPlayer) {
        NetworkHandler.CHANNEL.send(
            PacketDistributor.PLAYER.with(() -> serverPlayer),
            new ScopeEnablePacketS2C(CuriosUtils.hasCurio(serverPlayer, IScope.class))
        );
    }
}
