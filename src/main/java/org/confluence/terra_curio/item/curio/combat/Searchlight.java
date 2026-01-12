package org.confluence.terra_curio.item.curio.combat;

import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class Searchlight extends BaseCurioItem implements EffectInvul.Blindness, EffectInvul.Darkness {
    public Searchlight() {
        super(ModRarity.PINK);
    }
}
