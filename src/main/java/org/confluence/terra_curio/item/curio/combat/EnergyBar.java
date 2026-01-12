package org.confluence.terra_curio.item.curio.combat;

import net.minecraft.network.chat.Component;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class EnergyBar extends BaseCurioItem implements EffectInvul.Hunger {
    public EnergyBar() {
        super(ModRarity.LIGHT_RED);
    }

    public Component[] getInformation() {
        return new Component[]{
            Component.translatable("item.terra_curio.energy_bar.info")
        };
    }
}
