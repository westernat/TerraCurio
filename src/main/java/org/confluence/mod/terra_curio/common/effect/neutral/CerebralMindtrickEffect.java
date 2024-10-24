package org.confluence.mod.terra_curio.common.effect.neutral;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.init.ModAttributes;

public class CerebralMindtrickEffect extends MobEffect {
    public static final ResourceLocation ID = TerraCurio.asResource("cerebral_mindtrick");

    public CerebralMindtrickEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFFA885);
        addAttributeModifier(ModAttributes.getCriticalChance(), ID, 0.04, AttributeModifier.Operation.ADD_VALUE);
    }
}
