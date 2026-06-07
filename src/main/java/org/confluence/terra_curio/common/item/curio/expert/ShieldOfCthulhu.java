package org.confluence.terra_curio.common.item.curio.expert;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.lib.util.LibClientUtils;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.client.TCKeyBindings;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShieldOfCthulhu extends BaseCurioItem {
    public ShieldOfCthulhu(Builder builder) {
        super(builder);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (LibUtils.isPhysicalClient()) {
            tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.shield_of_cthulhu.0", LibClientUtils.keyMappingComponent(TCKeyBindings.CTHULHU_SPRINTING.get())));
        }
    }
}
