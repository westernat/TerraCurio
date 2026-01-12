package org.confluence.terra_curio.item.curio.movement;

import net.minecraft.network.chat.Component;

public class ShinyRedBalloon extends Balloon {
    @Override
    public Component[] getInformation() {
        return new Component[]{
            Component.translatable("item.terra_curio.shiny_red_balloon.info"),
        };
    }
}
