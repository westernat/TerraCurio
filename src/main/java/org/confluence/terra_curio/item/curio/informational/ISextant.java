package org.confluence.terra_curio.item.curio.informational;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public interface ISextant {
    static Component getInfo(Player localPlayer) {
        return Component.translatable("info.terra_curio.sextant." + localPlayer.level().getMoonPhase());
    }

    Component TOOLTIP = Component.translatable("curios.tooltip.sextant");
    byte INDEX = 2;
}
