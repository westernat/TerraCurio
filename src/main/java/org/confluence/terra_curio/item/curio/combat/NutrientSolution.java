package org.confluence.terra_curio.item.curio.combat;

import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class NutrientSolution extends BaseCurioItem implements EffectInvul.Weakness, EffectInvul.Hunger {
    public NutrientSolution() {
        super(ModRarity.PINK);
    }
}
