package org.confluence.terra_curio.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.item.curio.movement.BaseSpeedBoots;
import org.confluence.terra_curio.util.CuriosUtils;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public record SpeedBootsNBTPacketC2S(int slot, int value) implements CustomPacketPayload {
    public static final Type<SpeedBootsNBTPacketC2S> TYPE = new Type<>(TerraCurio.asResource("speed_boots_nbt"));
    public static final StreamCodec<ByteBuf, SpeedBootsNBTPacketC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.slot,
            ByteBufCodecs.INT, p -> p.value,
            SpeedBootsNBTPacketC2S::new
    );
    private static final Predicate<ItemStack> PREDICATE = itemStack -> itemStack.getItem() instanceof BaseSpeedBoots;

    @Override
    public @NotNull Type<SpeedBootsNBTPacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                CuriosUtils.getSlot(serverPlayer, PREDICATE, slot).ifPresent(itemStack -> {
                    TCUtils.updateItemStackNbt(itemStack, nbt -> nbt.putInt(BaseSpeedBoots.KEY, value));
                });
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
