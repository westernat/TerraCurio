package org.confluence.terra_curio.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.confluence.terra_curio.common.init.TCSoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MagicMirror extends Item {
    public MagicMirror(ModRarity rarity) {
        super(new Properties().component(TCDataComponentTypes.MOD_RARITY, rarity).fireResistant().stacksTo(1));
    }

    public MagicMirror(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 30;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity living) {
        if (level.isClientSide) {
            Minecraft.getInstance().gameRenderer.displayItemActivation(itemStack);
        } else if (living instanceof ServerPlayer serverPlayer) {
            BlockPos pos = serverPlayer.getRespawnPosition();
            if (pos == null) {
                ServerLevel serverLevel = serverPlayer.server.getLevel(Level.OVERWORLD);
                if (serverLevel == null) serverLevel = (ServerLevel) level;
                BlockPos spawnPos = serverLevel.getLevelData().getSpawnPos();
                serverPlayer.teleportTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            } else {
                serverPlayer.teleportTo(pos.getX(), pos.getY(), pos.getZ());
            }
            serverPlayer.getCooldowns().addCooldown(this, 10);
        }
        living.playSound(TCSoundEvents.TRANSMISSION.get());
        return itemStack;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
       tooltipComponents.add(Component.translatable("tooltip.item.terra_curio.magic_mirror"));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(getDescriptionId()).withStyle(style -> style.withColor(stack.get(TCDataComponentTypes.MOD_RARITY).getColor()));
    }
}
