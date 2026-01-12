package org.confluence.terra_curio.item.curio.informational;

import net.minecraft.network.chat.Component;

public interface ITallyCounter {
    static Component getInfo(int amount, Component info) {
        return Component.translatable("info.terra_curio.tally_counter").append(info).append("': " + amount);
    }

    Component TOOLTIP = Component.translatable("curios.tooltip.tally_counter");
    byte INDEX = 7;
}
