package org.confluence.terra_curio.item.curio.movement;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.confluence.terra_curio.misc.ModTags;

import java.util.List;

public interface IFluidWalk {
    List<TagKey<Fluid>> ONLY_WATER = List.of(ModTags.WATER_LIKE_WALK);
    List<TagKey<Fluid>> ALL_FLUIDS = List.of(ModTags.WATER_LIKE_WALK, ModTags.LAVA_LIKE_WALK);

    default List<TagKey<Fluid>> canStandOn() {
        return ONLY_WATER;
    }

    Component WATER = Component.translatable("curios.tooltip.fluid_walk.part");

    Component ALL_FLUID = Component.translatable("curios.tooltip.fluid_walk.all");
}
