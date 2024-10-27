package org.confluence.mod.terra_curio.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.component.AccessoriesComponent;
import org.confluence.mod.terra_curio.common.entity.StepStoolEntity;
import org.confluence.mod.terra_curio.common.init.ModDataComponentTypes;
import org.confluence.mod.terra_curio.util.CuriosUtils;
import org.jetbrains.annotations.NotNull;

public record StepStoolSteppingPacketC2S(int slot, int step, boolean increase) implements CustomPacketPayload {
    public static final Type<StepStoolSteppingPacketC2S> TYPE = new Type<>(TerraCurio.asResource("step_stool_stepping_c2s"));
    public static final StreamCodec<ByteBuf, StepStoolSteppingPacketC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.slot,
            ByteBufCodecs.INT, p -> p.step,
            ByteBufCodecs.BOOL, p -> p.increase,
            StepStoolSteppingPacketC2S::new
    );

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
                    CuriosUtils.getSlot(serverPlayer, "accessory", slot).ifPresent(itemStack -> {
                        AccessoriesComponent component = itemStack.get(ModDataComponentTypes.ACCESSORIES);
                        if (component != null && component.types().contains(AccessoriesComponent.STEP_STOOL)) {
                            itemStack.get(DataComponents.CUSTOM_DATA).getUnsafe().putInt("id", pEntity.getId());
                        }
                    });
                } else {
                    CuriosUtils.getSlot(serverPlayer, "accessory", slot).ifPresent(itemStack -> {
                        AccessoriesComponent component = itemStack.get(ModDataComponentTypes.ACCESSORIES);
                        if (component != null && component.types().contains(AccessoriesComponent.STEP_STOOL)) {
                            int id = itemStack.get(DataComponents.CUSTOM_DATA).getUnsafe().getInt("id");
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
