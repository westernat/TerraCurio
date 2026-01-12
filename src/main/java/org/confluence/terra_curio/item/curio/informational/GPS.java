package org.confluence.terra_curio.item.curio.informational;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.datagen.limit.CustomName;
import org.confluence.terra_curio.misc.ModRarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GPS extends AbstractInfoCurio implements IWatch, IDepthMeter, ICompass, CustomName {
    public GPS() {
        super(ModRarity.ORANGE);
    }

    @Override
    public String getGenName() {
        return "GPS";
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(ICompass.TOOLTIP);
        list.add(IDepthMeter.TOOLTIP);
        list.add(IWatch.TOOLTIP);
    }
}
