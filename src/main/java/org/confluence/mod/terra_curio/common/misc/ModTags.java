package org.confluence.mod.terra_curio.common.misc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.CuriosConstants;

public final class ModTags {

    public static final TagKey<Item> CURIO = ItemTags.create(ResourceLocation.fromNamespaceAndPath(CuriosConstants.MOD_ID, "accessory"));
    public static final TagKey<Item> RANGED_WEAPON = ItemTags.create(ResourceLocation.fromNamespaceAndPath(CuriosConstants.MOD_ID,"ranged_weapon"));


    public static final TagKey<Block> FLOWER_BOOTS_AVAILABLE = BlockTags.create(ResourceLocation.fromNamespaceAndPath(CuriosConstants.MOD_ID, "flower_boots_available"));


    public static final TagKey<Fluid> WATER_LIKE_WALK = FluidTags.create(ResourceLocation.fromNamespaceAndPath(CuriosConstants.MOD_ID,"water_like_walk"));
    public static final TagKey<Fluid> ALL_FLUID_WALK = FluidTags.create(ResourceLocation.fromNamespaceAndPath(CuriosConstants.MOD_ID,"all_fluid_walk"));

}
