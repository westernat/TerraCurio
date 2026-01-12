package org.confluence.terra_curio.item.curio.informational;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public interface IDepthMeter {
    static Component getInfo(Player localPlayer) {
        double y = localPlayer.getY();
        return Component.translatable("info.terra_curio.depth_meter." + (y > 63 ? "surface" : "underground"), "%.2f".formatted(y));
    }

    Component TOOLTIP = Component.translatable("curios.tooltip.depth_meter");
    byte INDEX = 11;
}
