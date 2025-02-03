package org.confluence.terra_curio.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;

public record PlayerJumpPacketC2S(byte jumpState, float motionY) implements CustomPacketPayload {
    public static final byte JUMP_BY_SELF = 1;
    public static final byte RESET_FALL_DISTANCE = 2;

    public static final Type<PlayerJumpPacketC2S> TYPE = new Type<>(TerraCurio.asResource("player_jump_c2s"));
    public static final StreamCodec<ByteBuf, PlayerJumpPacketC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, p -> p.jumpState,
            ByteBufCodecs.FLOAT, p -> p.motionY,
            PlayerJumpPacketC2S::new
    );

    @Override
    public Type<PlayerJumpPacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.hasImpulse = true;
                if ((jumpState & JUMP_BY_SELF) != 0) {
                    serverPlayer.awardStat(Stats.JUMP);
                    serverPlayer.causeFoodExhaustion(serverPlayer.isSprinting() ? 0.2F : 0.05F);
                }
                if ((jumpState & RESET_FALL_DISTANCE) != 0) {
                    serverPlayer.resetFallDistance();
                }
                Vec3 motion = serverPlayer.getDeltaMovement();
                serverPlayer.setDeltaMovement(motion.x, motionY, motion.z);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
