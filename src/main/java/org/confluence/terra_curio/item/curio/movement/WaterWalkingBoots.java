package org.confluence.terra_curio.item.curio.movement;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;
import org.confluence.terra_curio.util.CuriosUtils;
import top.theillusivec4.curios.api.SlotContext;

public class WaterWalkingBoots extends BaseCurioItem implements IFluidWalk {
    public WaterWalkingBoots() {
        super(ModRarity.LIGHT_RED);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosUtils.noSameCurio(slotContext.entity(), IFluidWalk.class);
    }

    @Override
    public Component[] getInformation() {
        return new Component[]{
            Component.translatable("item.terra_curio.water_walking_boots.info"),
        };
    }
}
