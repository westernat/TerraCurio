package org.confluence.mod.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.client.handler.ClientPacketHandler;
import org.jetbrains.annotations.NotNull;

public record RightClickSubtractorPacketS2C(int amount) implements CustomPacketPayload {
    public static final Type<RightClickSubtractorPacketS2C> TYPE = new Type<>(TerraCurio.asResource("right_click_sub"));
    public static final StreamCodec<ByteBuf, RightClickSubtractorPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.amount,
            RightClickSubtractorPacketS2C::new
    );

    @Override
    public @NotNull Type<RightClickSubtractorPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                ClientPacketHandler.handleSubstractor(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
