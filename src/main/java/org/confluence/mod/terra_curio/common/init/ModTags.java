package org.confluence.mod.terra_curio.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.confluence.mod.terra_curio.TerraCurio;
import top.theillusivec4.curios.api.CuriosApi;

public final class ModTags {
    public static final TagKey<Item> CURIO = ItemTags.create(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID,"accessory"));
    public static final TagKey<Item> MINUTE_WATCH = ItemTags.create(TerraCurio.asResource("minute_watch"));
    public static final TagKey<Item> RANGED_WEAPON = ItemTags.create(TerraCurio.asResource("ranged_weapon"));
    public static final TagKey<Block> FLOWER_BOOTS_AVAILABLE = BlockTags.create(TerraCurio.asResource("flower_boots_available"));
    public static final TagKey<Fluid> WATER_LIKE_WALK = FluidTags.create(TerraCurio.asResource("water_like_walk"));
    public static final TagKey<Fluid> ALL_FLUID_WALK = FluidTags.create(TerraCurio.asResource("all_fluid_walk"));
}
