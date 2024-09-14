package org.confluence.mod.terra_curio.common.effect.neutral;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.misc.ModAttributes;

public class CerebralMindtrickEffect extends MobEffect {

    public CerebralMindtrickEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFFA885);
        addAttributeModifier(ModAttributes.CRIT_CHANCE, ResourceLocation.fromNamespaceAndPath(TerraCurio.MOD_ID,"crit_chance"), 0.04, AttributeModifier.Operation.ADD_VALUE);
    }
}
