package org.confluence.mod.terra_curio.common.effect.beneficial;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.confluence.mod.terra_curio.TerraCurio;

import java.util.UUID;

public class GravitationEffect extends MobEffect {
    //todo packet
    public GravitationEffect() {
        super(MobEffectCategory.BENEFICIAL,0xAA00AA);
        this.addAttributeModifier(
                Attributes.GRAVITY,
                ResourceLocation.fromNamespaceAndPath(TerraCurio.MOD_ID,"gravity_effect"),
                -2.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );


    }
}
