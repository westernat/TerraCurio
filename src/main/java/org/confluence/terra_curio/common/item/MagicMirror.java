package org.confluence.terra_curio.common.item;

import PortLib.extensions.net.minecraft.world.item.Item.PortItemExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCSoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagicMirror extends Item {
    public MagicMirror(ModRarity rarity) {
        super(PortItemExtension.Properties.component(new Properties().fireResistant().stacksTo(1), ConfluenceMagicLib.MOD_RARITY, rarity));
    }

    public MagicMirror(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 30;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity living) {
        if (level.isClientSide) {
            Minecraft.getInstance().gameRenderer.displayItemActivation(itemStack);
        } else if (living instanceof ServerPlayer player) {
            if (player.getVehicle() != null) {
                player.removeVehicle();
            }
            ServerLevel respawnLevel = player.server.getLevel(player.getRespawnDimension());
            if (respawnLevel == null) {
                respawnLevel = player.server.overworld();
            }
            player.changeDimension(respawnLevel);
        }
        living.playSound(TCSoundEvents.TRANSMISSION.get());
        return itemStack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.magic_mirror.0").withStyle(ChatFormatting.GRAY));
    }
}
