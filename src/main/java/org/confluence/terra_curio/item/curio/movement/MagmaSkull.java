package org.confluence.terra_curio.item.curio.movement;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.item.curio.ILavaImmune;
import org.confluence.terra_curio.item.curio.combat.IFireImmune;
import org.confluence.terra_curio.misc.ModRarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagmaSkull extends BaseCurioItem implements IFireImmune, ILavaImmune {
    public MagmaSkull() {
        super(ModRarity.PINK);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(ILavaImmune.TOOLTIP);
        list.add(IFireImmune.TOOLTIP);
    }
}
