package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.network.codec.ByteBufCodecs.FLOAT;
import static net.minecraft.network.codec.ByteBufCodecs.INT;

public record PlayerJumpPacketS2C(float fartSpeed, float sandstormSpeed, int sandstormTicks, float blizzardSpeed, int blizzardTicks, float tsunamiSpeed, float cloudSpeed) implements CustomPacketPayload {
    public static final Type<PlayerJumpPacketS2C> TYPE = new Type<>(TerraCurio.asResource("player_jump_s2c"));
    public static final StreamCodec<ByteBuf, PlayerJumpPacketS2C> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(@NotNull ByteBuf buffer, PlayerJumpPacketS2C value) {
            FLOAT.encode(buffer, value.fartSpeed);
            FLOAT.encode(buffer, value.sandstormSpeed);
            INT.encode(buffer, value.sandstormTicks);
            FLOAT.encode(buffer, value.blizzardSpeed);
            INT.encode(buffer, value.blizzardTicks);
            FLOAT.encode(buffer, value.tsunamiSpeed);
            FLOAT.encode(buffer, value.cloudSpeed);
        }

        @Override
        public @NotNull PlayerJumpPacketS2C decode(@NotNull ByteBuf buffer) {
            float t1 = FLOAT.decode(buffer);
            float t2 = FLOAT.decode(buffer);
            int t3 = INT.decode(buffer);
            float t4 = FLOAT.decode(buffer);
            int t5 = INT.decode(buffer);
            float t6 = FLOAT.decode(buffer);
            float t7 = FLOAT.decode(buffer);
            return new PlayerJumpPacketS2C(t1, t2, t3, t4, t5, t6, t7);
        }
    };

    @Override
    public @NotNull Type<PlayerJumpPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                PlayerJumpHandler.handleJumpPacket(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
