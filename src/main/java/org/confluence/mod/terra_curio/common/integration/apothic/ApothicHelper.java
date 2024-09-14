package org.confluence.mod.terra_curio.common.integration.apothic;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.fml.ModList;
import org.confluence.mod.terra_curio.common.misc.ModAttributes;

import java.util.Hashtable;

public class ApothicHelper {
    private static Boolean isAttributesLoaded;
    public static final String ATTRIBUTES_ID = "attributeslib";
    public static final ResourceLocation CRIT_CHANCE = ResourceLocation.fromNamespaceAndPath(ATTRIBUTES_ID, "crit_chance");
    public static final ResourceLocation ARROW_VELOCITY = ResourceLocation.fromNamespaceAndPath(ATTRIBUTES_ID, "arrow_velocity");
    public static final ResourceLocation ARROW_DAMAGE = ResourceLocation.fromNamespaceAndPath(ATTRIBUTES_ID, "arrow_damage");
    public static final ResourceLocation DODGE_CHANCE = ResourceLocation.fromNamespaceAndPath(ATTRIBUTES_ID, "dodge_chance");
    public static final ResourceLocation MINING_SPEED = ResourceLocation.fromNamespaceAndPath(ATTRIBUTES_ID, "mining_speed");;
    public static final ResourceLocation ARMOR_PIERCE = ResourceLocation.fromNamespaceAndPath(ATTRIBUTES_ID, "armor_pierce");

    public static boolean isAttributesLoaded() {
        if (isAttributesLoaded == null) {
            isAttributesLoaded = ModList.get().isLoaded(ATTRIBUTES_ID);
        }
        return isAttributesLoaded;
    }

    public static void preset(Hashtable<Attribute, Attribute> map) {
        if (isAttributesLoaded()) {
            map.put(ModAttributes.CRIT_CHANCE.value(), BuiltInRegistries.ATTRIBUTE.get(ApothicHelper.CRIT_CHANCE));
            map.put(ModAttributes.RANGED_VELOCITY.value(), BuiltInRegistries.ATTRIBUTE.get(ApothicHelper.ARROW_VELOCITY));
            map.put(ModAttributes.RANGED_DAMAGE.value(), BuiltInRegistries.ATTRIBUTE.get(ApothicHelper.ARROW_DAMAGE));
            map.put(ModAttributes.DODGE_CHANCE.value(), BuiltInRegistries.ATTRIBUTE.get(ApothicHelper.DODGE_CHANCE));
            map.put(ModAttributes.MINING_SPEED.value(), BuiltInRegistries.ATTRIBUTE.get(ApothicHelper.MINING_SPEED));
            map.put(ModAttributes.ARMOR_PASS.value(), BuiltInRegistries.ATTRIBUTE.get(ApothicHelper.ARMOR_PIERCE));
        }
    }
}
