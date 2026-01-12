package org.confluence.terra_curio.item.curio.combat;

import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class DetoxificationCapsule extends BaseCurioItem implements EffectInvul.Poison, EffectInvul.Wither {
    public DetoxificationCapsule() {
        super(ModRarity.PINK);
    }
}
