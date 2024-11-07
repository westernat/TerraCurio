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
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.entity.StepStoolEntity;
import org.confluence.terra_curio.common.item.curio.movement.StepStool;
import org.confluence.terra_curio.util.CuriosUtils;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public record StepStoolSteppingPacketC2S(int slot, int step, boolean increase) implements CustomPacketPayload {
    public static final Type<StepStoolSteppingPacketC2S> TYPE = new Type<>(TerraCurio.asResource("step_stool_stepping_c2s"));
    public static final StreamCodec<ByteBuf, StepStoolSteppingPacketC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.slot,
            ByteBufCodecs.INT, p -> p.step,
            ByteBufCodecs.BOOL, p -> p.increase,
            StepStoolSteppingPacketC2S::new
    );
    private static final Predicate<ItemStack> PREDICATE = itemStack -> itemStack.getItem() instanceof StepStool;

    @Override
    public @NotNull Type<StepStoolSteppingPacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                if (slot == -1) return;
                if (step == 1 && increase) {
                    StepStoolEntity pEntity = new StepStoolEntity(serverPlayer);
                    serverPlayer.level().addFreshEntity(pEntity);
                    serverPlayer.teleportRelative(0.0, 1.001, 0.0);
                    CuriosUtils.getSlot(serverPlayer, PREDICATE, slot).ifPresent(itemStack -> {
                        TCUtils.updateItemStackNbt(itemStack, nbt -> nbt.putInt("id", pEntity.getId()));
                    });
                } else {
                    CuriosUtils.getSlot(serverPlayer, PREDICATE, slot).ifPresent(itemStack -> {
                        int id = TCUtils.getItemStackNbt(itemStack).getInt("id");
                        Entity entity = serverPlayer.level().getEntity(id);
                        if (entity instanceof StepStoolEntity stepStool) {
                            if (step == 0) {
                                stepStool.setOwner(null);
                            } else {
                                stepStool.setStep(step);
                                if (increase) {
                                    serverPlayer.teleportRelative(0.0, 1.001, 0.0);
                                }
                            }
                        }
                    });
                }
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
