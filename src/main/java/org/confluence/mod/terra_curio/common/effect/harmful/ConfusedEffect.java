package org.confluence.mod.terra_curio.common.effect.harmful;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ConfusedEffect extends MobEffect { //困惑 反向移动
    //todo packet
    public ConfusedEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B008B);
    }
}
