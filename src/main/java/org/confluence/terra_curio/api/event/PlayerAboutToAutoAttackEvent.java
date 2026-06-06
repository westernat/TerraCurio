package org.confluence.terra_curio.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

public class PlayerAboutToAutoAttackEvent extends PlayerEvent {
    private InteractionResult cancellationResult = InteractionResult.PASS;
    private final boolean couldPerform;
    private PortTriState action = PortTriState.DEFAULT;

    public PlayerAboutToAutoAttackEvent(Player player, boolean couldPerform) {
        super(player);
        this.couldPerform = couldPerform;
    }

    public void setAction(PortTriState action) {
        this.action = action;
    }

    public PortTriState getAction() {
        return action;
    }

    public boolean isCouldPerform() {
        return couldPerform;
    }

    @ApiStatus.Internal
    public boolean couldPerform() {
        return action.isTrue() || (couldPerform && action.isDefault());
    }

    public InteractionHand getHand() {
        return InteractionHand.MAIN_HAND;
    }

    public ItemStack getItemStack() {
        return getEntity().getItemInHand(getHand());
    }

    public BlockPos getPos() {
        return getEntity().blockPosition();
    }

    public @Nullable Direction getFace() {
        return null;
    }

    public Level getLevel() {
        return getEntity().level();
    }

    public LogicalSide getSide() {
        return getLevel().isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER;
    }

    public InteractionResult getCancellationResult() {
        return cancellationResult;
    }

    public void setCancellationResult(InteractionResult result) {
        this.cancellationResult = result;
    }
}
