package org.confluence.mod.terra_curio.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.terra_curio.TerraCurio;
import org.jetbrains.annotations.NotNull;

public record PlayerJumpPacketC2S(boolean jumpBySelf, boolean resetFallDistance, float motionY) implements CustomPacketPayload {
    public static final Type<PlayerJumpPacketC2S> TYPE = new Type<>(TerraCurio.asResource("player_jump_c2s"));
    public static final StreamCodec<ByteBuf, PlayerJumpPacketC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, p -> p.jumpBySelf,
            ByteBufCodecs.BOOL, p -> p.resetFallDistance,
            ByteBufCodecs.FLOAT, p -> p.motionY,
            PlayerJumpPacketC2S::new
    );

    @Override
    public @NotNull Type<PlayerJumpPacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.hasImpulse = true;
                if (jumpBySelf) {
                    serverPlayer.awardStat(Stats.JUMP);
                    serverPlayer.causeFoodExhaustion(serverPlayer.isSprinting() ? 0.2F : 0.05F);
                }
                if (resetFallDistance) {
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
