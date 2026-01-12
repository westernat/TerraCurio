package org.confluence.terra_curio.item.curio.movement;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.item.curio.combat.IFireImmune;
import org.confluence.terra_curio.misc.ModRarity;
import org.confluence.terra_curio.util.CuriosUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class ObsidianWaterWalkingBoots extends BaseCurioItem implements IFluidWalk, IFireImmune {
    public ObsidianWaterWalkingBoots() {
        super(ModRarity.LIGHT_RED);
    }

    @Override
    public List<TagKey<Fluid>> canStandOn() {
        return ALL_FLUIDS;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosUtils.noSameCurio(slotContext.entity(), IFluidWalk.class);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(IFluidWalk.WATER);
        list.add(IFireImmune.TOOLTIP);
    }
}
