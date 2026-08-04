package org.confluence.terra_curio.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.lib.common.item.CustomRarityItem;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCArmorMaterials;
import org.confluence.terra_curio.common.init.TCTags;
import org.jspecify.annotations.Nullable;

public class DivingHelmet extends CustomRarityItem {
    private static final float DIVISION = 1.0F / 6.0F;

    public DivingHelmet(Identifier id) {
        super(new Properties().fireResistant()
                .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD).setSwappable(false).setCameraOverlay(TerraCurio.asResource("gui/diving_helmet")).build())
                .humanoidArmor(TCArmorMaterials.DIVING, ArmorType.HELMET)
                .setId(ResourceKey.create(Registries.ITEM, id)), ModRarity.GREEN);
    }

    @Override
    public boolean isGazeDisguise(ItemStack stack, Player player, @Nullable LivingEntity entity) {
        return true;
    }

    public static float apply(LivingEntity living, DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypeTags.IS_DROWNING) && living.getItemBySlot(EquipmentSlot.HEAD).is(TCTags.DIVING)) {
            return amount * DIVISION;
        }
        return amount;
    }
}
