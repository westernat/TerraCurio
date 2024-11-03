package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.confluence.terra_curio.common.init.TCAttributes;
import org.confluence.terra_curio.common.init.TCDamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @WrapOperation(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"))
    private float passArmor(LivingEntity entity, float damage, DamageSource damageSource, float armorValue, float armorToughness, Operation<Float> original) {
        if (!TCAttributes.hasCustomAttribute(TCAttributes.ARMOR_PASS) && damageSource.getEntity() instanceof LivingEntity attacker) {
            AttributeInstance attributeInstance = attacker.getAttribute(TCAttributes.ARMOR_PASS);
            if (attributeInstance != null) armorValue -= (float) attributeInstance.getValue();
            if (damageSource.is(TCDamageTypes.STAR_CLOAK)) armorValue -= 3.0F;
            armorValue = Math.max(armorValue, 0.0F);
        }
        return original.call(entity, damage, damageSource, armorValue, armorToughness);
    }
}
