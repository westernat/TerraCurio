package org.confluence.terra_curio.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.entity.StepStoolEntity;
import org.confluence.terra_curio.common.item.curio.movement.StepStool;
import org.confluence.terra_curio.util.CuriosUtils;

import java.util.function.Predicate;

public record StepStoolSteppingPacketC2S(int slot, byte step) implements CustomPacketPayload {
    public static final byte STEP_MASK = 0b0111111;
    public static final byte INCREASE  = 0b1000000;

    public static final Type<StepStoolSteppingPacketC2S> TYPE = new Type<>(TerraCurio.asResource("step_stool_stepping_c2s"));
    public static final StreamCodec<ByteBuf, StepStoolSteppingPacketC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, StepStoolSteppingPacketC2S::slot,
            ByteBufCodecs.BYTE, StepStoolSteppingPacketC2S::step,
            StepStoolSteppingPacketC2S::new
    );
    private static final Predicate<ItemStack> PREDICATE = itemStack -> itemStack.getItem() instanceof StepStool;

    @Override
    public Type<StepStoolSteppingPacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (slot == -1) return;
            if (context.player() instanceof ServerPlayer serverPlayer) {
                ItemStack itemStack = CuriosUtils.getSlot(serverPlayer, PREDICATE, slot);
                if (itemStack == null) return;
                int actualStep = step & STEP_MASK;
                boolean increase = (step & INCREASE) == INCREASE;

                if (actualStep == 1 && increase) {
                    StepStoolEntity entity = new StepStoolEntity(serverPlayer);
                    serverPlayer.level().addFreshEntity(entity);
                    serverPlayer.teleportRelative(0.0, 1.001, 0.0);
                    LibUtils.updateItemStackNbt(itemStack, nbt -> nbt.putInt("id", entity.getId()));
                } else {
                    int id = LibUtils.getItemStackNbtNoCopy(itemStack).getInt("id");
                    Entity entity = serverPlayer.level().getEntity(id);
                    if (entity instanceof StepStoolEntity stepStool) {
                        if (actualStep == 0) {
                            stepStool.setOwner(null);
                        } else {
                            stepStool.setStep(actualStep);
                            if (increase) {
                                serverPlayer.teleportRelative(0.0, 1.001, 0.0);
                            }
                        }
                    }
                }
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
