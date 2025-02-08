package org.confluence.terra_curio.common.init;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.event.RangePickupItemEvent;
import org.confluence.terra_curio.integration.apothic.ApothicHelper;
import org.confluence.terra_curio.mixin.accessor.RangedAttributeAccessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public final class TCAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, TerraCurio.MODID);

    public static final DeferredHolder<Attribute, Attribute> CRIT_CHANCE = ATTRIBUTES.register("generic.crit_chance", () -> new PercentageAttribute("attribute.name.generic.critical_chance", 0.0, 0.0, 10.0).setSyncable(true)); // ADDITION
    public static final DeferredHolder<Attribute, Attribute> RANGED_VELOCITY = ATTRIBUTES.register("generic.ranged_velocity", () -> new RangedAttribute("attribute.name.generic.ranged_velocity", 1.0, 0.0, 10.0).setSyncable(true)); // MULTIPLY_TOTAL
    public static final DeferredHolder<Attribute, Attribute> RANGED_DAMAGE = ATTRIBUTES.register("generic.ranged_damage", () -> new RangedAttribute("attribute.name.generic.ranged_damage", 1.0, 0.0, 10.0).setSyncable(true)); // MULTIPLY_TOTAL
    public static final DeferredHolder<Attribute, Attribute> DODGE_CHANCE = ATTRIBUTES.register("generic.dodge_chance", () -> new RangedAttribute("attribute.name.generic.dodge_chance", 0.0, 0.0, 1.0).setSyncable(true)); // ADDITION
    public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE = ATTRIBUTES.register("generic.magic_damage", () -> new RangedAttribute("attribute.name.generic.magic_damage", 1.0, 0.0, 10.0).setSyncable(true)); // MULTIPLY_TOTAL
    public static final DeferredHolder<Attribute, Attribute> ARMOR_PASS = ATTRIBUTES.register("generic.armor_pass", () -> new RangedAttribute("attribute.name.generic.armor_pass", 0.0, 0.0, 10000).setSyncable(true)); // ADDITION

    public static final DeferredHolder<Attribute, Attribute> PICKUP_RANGE = ATTRIBUTES.register("player.pickup_range", () -> new RangedAttribute("attribute.name.player.pickup_range", 0.0, 0.0, 64.0).setSyncable(true)); // ADDITION
    public static final DeferredHolder<Attribute, Attribute> AGGRO = ATTRIBUTES.register("player.aggro", () -> new RangedAttribute("attribute.name.generic.aggro", 0.0, -10000.0, 10000.0).setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE)); // ADDITION

    private static final Map<Holder<Attribute>, Holder<Attribute>> MAP = Util.make(new HashMap<>(), table -> {
        table.put(CRIT_CHANCE, null);
        table.put(RANGED_DAMAGE, null);
        table.put(RANGED_VELOCITY, null);
        table.put(DODGE_CHANCE, null);
        table.put(MAGIC_DAMAGE, null);
        table.put(ARMOR_PASS, null);
    });

    public static Holder<Attribute> getCriticalChance() {
        return getCustomAttribute(CRIT_CHANCE);
    }

    public static Holder<Attribute> getRangedVelocity() {
        return getCustomAttribute(RANGED_VELOCITY);
    }

    public static Holder<Attribute> getRangedDamage() {
        return getCustomAttribute(RANGED_DAMAGE);
    }

    public static Holder<Attribute> getDodgeChance() {
        return getCustomAttribute(DODGE_CHANCE);
    }

    public static Holder<Attribute> getMagicDamage() {
        return getCustomAttribute(MAGIC_DAMAGE);
    }

    public static Holder<Attribute> getArmorPass() {
        return getCustomAttribute(ARMOR_PASS);
    }

    public static Holder<Attribute> getCustomAttribute(Holder<Attribute> attribute) {
        Holder<Attribute> target = MAP.get(attribute);
        if (target == null) return attribute;
        return target;
    }

    public static boolean hasCustomAttribute(Holder<Attribute> attribute) {
        return MAP.get(attribute) != null;
    }

    public static void registerAttribute(Holder<Attribute> attribute, BiConsumer<EntityType<? extends LivingEntity>, Holder<Attribute>> consumer) {
        if (!hasCustomAttribute(attribute)) consumer.accept(EntityType.PLAYER, attribute);
    }

    public static void applyToArrow(LivingEntity living, AbstractArrow abstractArrow) {
        AttributeInstance attributeInstance;
        if (!hasCustomAttribute(RANGED_VELOCITY)) {
            attributeInstance = living.getAttribute(RANGED_VELOCITY);
            if (attributeInstance != null) {
                abstractArrow.setDeltaMovement(abstractArrow.getDeltaMovement().scale(attributeInstance.getValue()));
            }
        }
        if (!abstractArrow.isCritArrow() && !hasCustomAttribute(CRIT_CHANCE)) {
            attributeInstance = living.getAttribute(CRIT_CHANCE);
            if (attributeInstance != null) {
                abstractArrow.setCritArrow(living.getRandom().nextFloat() < attributeInstance.getValue());
            }
        }
    }

    public static double applyArrowKnockback(Entity attacker, double original) {
        if (attacker instanceof LivingEntity living) {
            AttributeInstance instance = living.getAttribute(Attributes.ATTACK_KNOCKBACK);
            if (instance != null) return original * (1.0 + instance.getValue());
        }
        return original;
    }

    public static boolean applyDodge(LivingEntity living, RandomSource random) {
        if (hasCustomAttribute(DODGE_CHANCE)) return false;
        AttributeInstance attributeInstance = living.getAttribute(DODGE_CHANCE);
        if (attributeInstance == null) return false;
        return random.nextFloat() < attributeInstance.getValue();
    }

    public static float applyRangedDamage(DamageSource damageSource, float amount) {
        if (hasCustomAttribute(RANGED_DAMAGE)) return amount;
        if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && damageSource.getEntity() instanceof LivingEntity living) {
            AttributeInstance attributeInstance = living.getAttribute(RANGED_DAMAGE);
            if (attributeInstance == null) return amount;
            return amount * (float) attributeInstance.getValue();
        }
        return amount;
    }

    public static float applyMagicDamage(DamageSource damageSource, float amount) {
        if (TerraCurio.IS_CONFLUENCE_LOADED || hasCustomAttribute(MAGIC_DAMAGE)) return amount;
        if (damageSource.is(Tags.DamageTypes.IS_MAGIC)) {
            if (damageSource.getEntity() instanceof LivingEntity living) {
                AttributeInstance attributeInstance = living.getAttribute(MAGIC_DAMAGE);
                if (attributeInstance == null) return amount;
                return amount * (float) attributeInstance.getValue();
            }
        }
        return amount;
    }

    public static void applyPickupRange(Player player) {
        AttributeInstance attributeInstance = player.getAttribute(PICKUP_RANGE);
        float originalRange = attributeInstance == null ? 0.0F : (float) attributeInstance.getValue();
        float range = NeoForge.EVENT_BUS.post(new RangePickupItemEvent.Pre(player, originalRange)).getRange();
        if (range <= 0.0F) return;
        player.level().getEntitiesOfClass(
                ItemEntity.class,
                new AABB(player.getOnPos()).inflate(range),
                itemEntity -> !itemEntity.hasPickUpDelay()
        ).forEach(itemEntity -> {
            if (itemEntity.isRemoved() || NeoForge.EVENT_BUS.post(new RangePickupItemEvent.Post(player, itemEntity, originalRange)).isCanceled()) return;
            itemEntity.addDeltaMovement(player.position().subtract(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ()).normalize().scale(0.05F).add(0, 0.04F, 0));
            itemEntity.move(MoverType.SELF, itemEntity.getDeltaMovement());
        });
    }

    public static void prepareReplacements() {
        Map<String, Holder<Attribute>> available = Map.of(
                "crit_chance", CRIT_CHANCE,
                "ranged_velocity", RANGED_VELOCITY,
                "ranged_damage", RANGED_DAMAGE,
                "dodge_chance", DODGE_CHANCE,
                "magic_damage", MAGIC_DAMAGE,
                "armor_pass", ARMOR_PASS
        );

        ApothicHelper.preset(MAP);

        List<? extends String> attributes = TCStartupConfigs.ATTRIBUTE_REPLACE.get();
        for (String attribute : attributes) {
            String[] split = attribute.split("=");
            if (split.length != 2) {
                TerraCurio.LOGGER.warn("Bad format of '{}', which must contains exactly one '='", attribute);
                continue;
            }
            Holder<Attribute> holder = available.get(split[0]);
            if (holder == null) {
                TerraCurio.LOGGER.warn("Unsupported attribute: {}", split[0]);
                continue;
            }
            Optional<Holder.Reference<Attribute>> optional = BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(split[1]));
            if (optional.isEmpty()) {
                TerraCurio.LOGGER.warn("Unknown attribute: {}", split[1]);
            } else {
                MAP.replace(holder, optional.get());
            }
        }
    }

    public static void modifyAttributesUpperLimit() {
        if (!ModList.get().isLoaded("attributefix")) {
            if (Attributes.ARMOR.value() instanceof RangedAttribute rangedAttribute) {
                ((RangedAttributeAccessor) rangedAttribute).setMaxValue(1024.0);
            }
            if (Attributes.ARMOR_TOUGHNESS.value() instanceof RangedAttribute rangedAttribute) {
                ((RangedAttributeAccessor) rangedAttribute).setMaxValue(1024.0);
            }
            if (Attributes.MAX_HEALTH.value() instanceof RangedAttribute rangedAttribute) {
                ((RangedAttributeAccessor) rangedAttribute).setMaxValue(8192.0);
            }
        }
    }
}
