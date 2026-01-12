package org.confluence.terra_curio.item.curio.combat;

import net.minecraft.world.item.Rarity;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class AnkhCharm extends BaseCurioItem implements EffectInvul.Poison, EffectInvul.Hunger, EffectInvul.MiningFatigue, EffectInvul.Weakness, EffectInvul.Levitation, EffectInvul.Wither, EffectInvul.Darkness, EffectInvul.Blindness, EffectInvul.Nausea, EffectInvul.Slowness {
    public AnkhCharm() {
        super(ModRarity.LIGHT_PURPLE);
    }

    public AnkhCharm(Rarity rarity) {
        super(rarity);
    }
}
