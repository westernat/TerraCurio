package org.confluence.terra_curio.item.curio.combat;

import net.minecraft.network.chat.Component;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModConfigs;
import org.confluence.terra_curio.misc.ModRarity;

public class CrossNecklace extends BaseCurioItem implements IInvulnerableTime {
    public CrossNecklace() {
        super(ModRarity.LIGHT_RED);
    }

    @Override
    public int getTime() {
        return ModConfigs.CROSS_NECKLACE_INVULNERABLE_TIME.get();
    }

    public Component[] getInformation() {
        return new Component[]{
            Component.translatable("item.terra_curio.cross_necklace.info")
        };
    }
}
