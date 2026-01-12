package org.confluence.terra_curio.item.curio.combat;

import net.minecraft.network.chat.Component;
import org.confluence.terra_curio.misc.ModRarity;

public class SweetheartNecklace extends PanicNecklace implements IHoneycomb {
    public SweetheartNecklace() {
        super(ModRarity.ORANGE);
    }

    @Override
    public Component[] getInformation() {
        return new Component[]{};
    }
}
