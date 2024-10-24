package org.confluence.mod.terra_curio.common.effect.beneficial;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.confluence.mod.terra_curio.TerraCurio;

public class GravitationEffect extends MobEffect {
    public static final ResourceLocation ID = TerraCurio.asResource("gravity");

    //todo packet
    public GravitationEffect() {
        super(MobEffectCategory.BENEFICIAL,0xAA00AA);
    }
}
