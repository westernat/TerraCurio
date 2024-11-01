package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.StepStoolHandler;
import org.jetbrains.annotations.NotNull;

public record StepStoolSteppingPacketS2C(int slot, int maxStep) implements CustomPacketPayload {
    public static final int NO_CURIO = -1;
    public static final int RESET_STEP = -2;
    public static final Type<StepStoolSteppingPacketS2C> TYPE = new Type<>(TerraCurio.asResource("step_stool_stepping_s2c"));
    public static final StreamCodec<ByteBuf, StepStoolSteppingPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.slot,
            ByteBufCodecs.INT, p -> p.maxStep,
            StepStoolSteppingPacketS2C::new
    );

    @Override
    public @NotNull Type<StepStoolSteppingPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                StepStoolHandler.handlePacket(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static StepStoolSteppingPacketS2C resetStep() {
        return new StepStoolSteppingPacketS2C(RESET_STEP, 0);
    }
}
