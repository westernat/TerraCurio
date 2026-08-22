package org.confluence.terra_curio.common.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.common.LibAttributes;
import org.confluence.terra_curio.TerraCurio;
import org.jetbrains.annotations.ApiStatus;

@Deprecated(since = "1.3.0", forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "1.4.0")
public final class TCAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, TerraCurio.MODID);

    public static final DeferredHolder<Attribute, PercentageAttribute> CRITICAL_CHANCE = ConfluenceMagicLib.CRITICAL_CHANCE;
    public static final DeferredHolder<Attribute, RangedAttribute> RANGED_VELOCITY = ConfluenceMagicLib.RANGED_VELOCITY;
    public static final DeferredHolder<Attribute, RangedAttribute> RANGED_DAMAGE = ConfluenceMagicLib.RANGED_DAMAGE;
    public static final DeferredHolder<Attribute, PercentageAttribute> DODGE_CHANCE = ConfluenceMagicLib.DODGE_CHANCE;
    public static final DeferredHolder<Attribute, RangedAttribute> MAGIC_DAMAGE = ConfluenceMagicLib.MAGIC_DAMAGE;
    public static final DeferredHolder<Attribute, RangedAttribute> ARMOR_PENETRATION = ConfluenceMagicLib.ARMOR_PENETRATION;

    public static final DeferredHolder<Attribute, RangedAttribute> PICKUP_RANGE = ConfluenceMagicLib.PICKUP_RANGE;
    public static final DeferredHolder<Attribute, RangedAttribute> AGGRO = ConfluenceMagicLib.AGGRO;

    public static Holder<Attribute> getCriticalChance() {
        return LibAttributes.getCriticalChance();
    }

    public static Holder<Attribute> getRangedVelocity() {
        return LibAttributes.getRangedVelocity();
    }

    public static Holder<Attribute> getRangedDamage() {
        return LibAttributes.getRangedDamage();
    }

    public static Holder<Attribute> getDodgeChance() {
        return LibAttributes.getDodgeChance();
    }

    public static Holder<Attribute> getMagicDamage() {
        return LibAttributes.getMagicDamage();
    }

    public static Holder<Attribute> getArmorPenetration() {
        return LibAttributes.getArmorPenetration();
    }

    public static Holder<Attribute> getCustomAttribute(Holder<Attribute> attribute) {
        return LibAttributes.getCustomAttribute(attribute);
    }

    public static boolean hasCustomAttribute(Holder<Attribute> attribute) {
        return LibAttributes.getCustomAttribute(attribute).equals(attribute);
    }
}
