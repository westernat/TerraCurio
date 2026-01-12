package org.confluence.terra_curio.item.curio.movement;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.item.curio.combat.HoneyComb;
import org.confluence.terra_curio.item.curio.combat.IHoneycomb;
import org.confluence.terra_curio.misc.ModConfigs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HoneyBalloon extends HoneyComb implements IJumpBoost {
    @Override
    public double getBoost() {
        return ModConfigs.HONEY_BALLOON_JUMP_BOOST.get();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(IHoneycomb.TOOLTIP);
        list.add(IJumpBoost.TOOLTIP);
    }

    @Override
    public Component[] getInformation() {
        return new Component[]{};
    }
}
