package org.confluence.terra_curio.item.curio.combat;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class HoneyComb extends BaseCurioItem implements IHoneycomb {
    public HoneyComb() {
        super(ModRarity.GREEN);
    }

    public HoneyComb(Rarity rarity) {
        super(rarity);
    }

    public Component[] getInformation() {
        return new Component[]{
                Component.translatable("item.terra_curio.honey_comb.info")
        };
    }
}
