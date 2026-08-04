package org.confluence.terra_curio.common.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;

public final class TCArmorMaterials {
    public static final ArmorMaterial DIVING = new ArmorMaterial(165, Util.make(
            new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 2);
                map.put(ArmorType.LEGGINGS, 5);
                map.put(ArmorType.CHESTPLATE, 6);
                map.put(ArmorType.HELMET, 2);
                map.put(ArmorType.BODY, 5);
            }),
            9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            ItemTags.REPAIRS_IRON_ARMOR, EquipmentAssets.IRON // todo assets
    );
}
