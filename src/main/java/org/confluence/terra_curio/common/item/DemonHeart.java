package org.confluence.terra_curio.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.lib.util.LibClientUtils;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCCommonConfigs;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.function.Consumer;

public class DemonHeart extends Item {
    public final Identifier id;

    public DemonHeart(Identifier id) {
        super(new Properties().fireResistant()
                .component(ConfluenceMagicLib.MOD_RARITY, ModRarity.EXPERT).stacksTo(1)
                .setId(ResourceKey.create(Registries.ITEM, id)));
        this.id = id;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer) {
            CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
                ICurioStacksHandler iCurioStacksHandler = iCuriosItemHandler.getCurios().get(TerraCurio.CURIO_SLOT);
                if (iCurioStacksHandler != null && iCurioStacksHandler.getSlots() < TCCommonConfigs.MAX_ACCESSORIES.get()) {
                    itemStack.shrink(1);
                    Map<Identifier, AttributeModifier> modifiers = iCurioStacksHandler.getModifiers();
                    double before = modifiers.containsKey(id) ? modifiers.get(id).amount() : 0.0;
                    iCurioStacksHandler.removeModifier(id);
                    iCuriosItemHandler.addPermanentSlotModifier(TerraCurio.CURIO_SLOT, id, before + 1.0, AttributeModifier.Operation.ADD_VALUE);
                    LibUtils.forMixin$ModifyExpression(serverPlayer);
                }
            });
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (LibUtils.isPhysicalClient()) {
            builder.accept(Component.translatable("tooltip.item.terra_curio.demon_heart.0").withStyle(ChatFormatting.GREEN));
            CuriosApi.getCuriosInventory(LibClientUtils.getPlayer()).ifPresent(iCuriosItemHandler -> {
                ICurioStacksHandler iCurioStacksHandler = iCuriosItemHandler.getCurios().get(TerraCurio.CURIO_SLOT);
                builder.accept(Component.translatable(
                        "tooltip.item.terra_curio.demon_heart.1",
                        TCCommonConfigs.MAX_ACCESSORIES.get() - iCurioStacksHandler.getSlots()
                ).withStyle(ChatFormatting.GRAY));
            });
        }
    }
}
