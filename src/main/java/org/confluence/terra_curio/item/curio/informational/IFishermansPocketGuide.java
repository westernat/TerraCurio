package org.confluence.terra_curio.item.curio.informational;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public interface IFishermansPocketGuide {
    static Component getInfo(Player localPlayer) {
        return Component.translatable(
                "info.terra_curio.fishermans_pocket_guide",
                "%.2f".formatted(localPlayer.getLuck())
        );
    }

    Component TOOLTIP = Component.translatable("curios.tooltip.fishermans_pocket_guide");
    byte INDEX = 3;
}
