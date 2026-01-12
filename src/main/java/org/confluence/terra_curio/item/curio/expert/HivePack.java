package org.confluence.terra_curio.item.curio.expert;

import net.minecraft.network.chat.Component;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class HivePack extends BaseCurioItem implements ModRarity.Expert {
    public HivePack() {
        super(ModRarity.EXPERT);
    }

    @Override
    public Component[] getInformation() {
        return new Component[]{
            Component.translatable("item.terra_curio.hive_pack.info")
        };
    }
}
