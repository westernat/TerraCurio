package org.confluence.terra_curio.item.curio.combat;

import org.confluence.terra_curio.datagen.limit.CustomName;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class ExplorersEquipment extends BaseCurioItem implements EffectInvul.MiningFatigue, EffectInvul.Levitation, CustomName {
    public ExplorersEquipment() {
        super(ModRarity.PINK);
    }

    @Override
    public String getGenName() {
        return "Explorer's Equipment";
    }
}
