package org.confluence.terra_curio.item.curio.combat;

import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class ThePlan extends BaseCurioItem implements EffectInvul.Slowness, EffectInvul.Nausea {
    public ThePlan() {
        super(ModRarity.PINK);
    }
}
