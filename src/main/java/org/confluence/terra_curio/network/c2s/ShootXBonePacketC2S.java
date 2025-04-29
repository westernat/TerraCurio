package org.confluence.terra_curio.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.entity.XBoneProjectile;
import org.confluence.terra_curio.common.init.TCEntities;

public record ShootXBonePacketC2S() implements CustomPacketPayload {
    public static final ShootXBonePacketC2S INSTANCE = new ShootXBonePacketC2S();
    public static final Type<ShootXBonePacketC2S> TYPE = new Type<>(TerraCurio.asResource("shoot_x_bone"));
    public static final StreamCodec<ByteBuf, ShootXBonePacketC2S> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<ShootXBonePacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                long l = serverPlayer.getPersistentData().getLong("terra_curio:last_x_bone");
                Level level = serverPlayer.level();
                long gameTime = level.getGameTime();
                if (gameTime - l > 20) {
                    serverPlayer.getPersistentData().putLong("terra_curio:last_x_bone", gameTime);
                    XBoneProjectile projectile = new XBoneProjectile(TCEntities.X_BONE.get(), level);
                    projectile.setPos(serverPlayer.getX(), serverPlayer.getEyeY() - 0.1, serverPlayer.getZ());
                    projectile.shootFromRotation(serverPlayer, serverPlayer.getXRot(), serverPlayer.getYRot(), 0, 1.2F, 0);
                    projectile.setOwner(serverPlayer);
                    level.addFreshEntity(projectile);
                }
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
