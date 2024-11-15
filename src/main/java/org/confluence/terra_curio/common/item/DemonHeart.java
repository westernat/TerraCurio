package org.confluence.terra_curio.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.animate.ExpertColorAnimation;
import org.confluence.terra_curio.common.init.TCCommonConfigs;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;
import java.util.Map;

public class DemonHeart extends Item {
    public static final ResourceLocation ID = TerraCurio.asResource("demon_heart");

    public DemonHeart() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        CuriosApi.getCuriosInventory(pPlayer).ifPresent(iCuriosItemHandler -> {
            ICurioStacksHandler iCurioStacksHandler = iCuriosItemHandler.getCurios().get("accessory");
            if (iCurioStacksHandler != null && iCurioStacksHandler.getSlots() < TCCommonConfigs.MAX_ACCESSORIES.get()) {
                itemStack.shrink(1);
                Map<ResourceLocation, AttributeModifier> modifiers = iCurioStacksHandler.getModifiers();
                double before = modifiers.containsKey(ID) ? modifiers.get(ID).amount() : 0.0;
                iCurioStacksHandler.removeModifier(ID);
                iCuriosItemHandler.addPermanentSlotModifier("accessory", ID, before + 1.0, AttributeModifier.Operation.ADD_VALUE);
            }
        });
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.demon_heart.0"));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(getDescriptionId()).withStyle(style -> style.withColor(ExpertColorAnimation.INSTANCE.getColor()));
    }
}
