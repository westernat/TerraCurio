package org.confluence.terra_curio.item.common;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.item.curio.informational.*;
import org.confluence.terra_curio.misc.ModRarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CellPhone extends MagicMirror implements ICompass, IDepthMeter, IDPSMeter, IFishermansPocketGuide,
    ILifeFormAnalyzer, IMetalDetector, IRadar, ISextant, IStopwatch, ITallyCounter, IWatch, IWeatherRadio {
    public CellPhone() {
        super(ModRarity.LIME);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.terra_curio.cell_phone.tooltip"));
        pTooltipComponents.add(Component.translatable("item.terra_curio.cell_phone.tooltip2"));
    }
}
