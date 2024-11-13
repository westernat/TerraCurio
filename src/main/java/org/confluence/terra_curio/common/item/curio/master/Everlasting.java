package org.confluence.terra_curio.common.item.curio.master;

import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;

public class Everlasting extends BaseCurioItem {
    public Everlasting() {
        super(builder("everlasting").rarity(ModRarity.MASTER));
    }
}
