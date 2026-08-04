package org.confluence.terra_curio.common.item.curio.movement;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.confluence.lib.util.LibClientUtils;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.client.TCKeyBindings;
import org.confluence.terra_curio.common.entity.StepStoolEntity;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.network.s2c.StepStoolSteppingPacketS2C;
import org.confluence.terra_curio.util.CuriosUtils;
import top.theillusivec4.curios.api.SlotContext;

import java.util.function.Consumer;

public class StepStool extends BaseCurioItem {
    public StepStool(Builder builder) {
        super(builder);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (prevStack.getItem() == stack.getItem()) return;
        super.onEquip(slotContext, prevStack, stack);
        if (!slotContext.entity().level().isClientSide()) {
            StepStoolSteppingPacketS2C.sendToClient(slotContext, getMaxStep(stack));
        }
    }

    public static int getMaxStep(ItemStack stack) {
        return LibUtils.getItemStackNbtNoCopy(stack).getIntOr("extraStep", 0) + 1;
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (newStack.getItem() == stack.getItem()) return;
        super.onUnequip(slotContext, newStack, stack);
        Level level = slotContext.entity().level();
        if (!level.isClientSide()) {
            StepStoolSteppingPacketS2C.resetStep(slotContext.entity(), 0);
            if (level.getEntity(LibUtils.getItemStackNbtNoCopy(stack).getIntOr("id", 0)) instanceof StepStoolEntity stepStool) {
                stepStool.setOwner(null);
            }
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosUtils.noSameCurio(slotContext.entity(), StepStool.class);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (LibUtils.isPhysicalClient()) {
            builder.accept(Component.translatable(
                    "tooltip.item.terra_curio.step_stool.0",
                    LibClientUtils.keyMappingComponent(TCKeyBindings.STEP_STOOL.get())
            ));
        }
        builder.accept(Component.translatable(
                "tooltip.item.terra_curio.step_stool.1", LibUtils.getItemStackNbtNoCopy(itemStack).getInt("extraStep")
        ).withStyle(ChatFormatting.BLUE));
    }
}
