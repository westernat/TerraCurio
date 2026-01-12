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

public class FishermansPocketGuide extends AbstractInfoCurio implements IFishermansPocketGuide, CustomName {
    public FishermansPocketGuide() {
        super(ModRarity.BLUE);
    }

    @Override
    public String getGenName() {
        return "Fisherman's Pocket Guide";
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(TOOLTIP);
    }

    @Override
    public Component[] getInformation() {
        return new Component[]{
            Component.translatable("item.terra_curio.fishermans_pocket_guide.info")
        };
    }
}
