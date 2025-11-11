package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.lib.network.IPacketS2C;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.jetbrains.annotations.Nullable;

public record AttackDamagePacketS2C(float amount) implements IPacketS2C {
    public static final Type<AttackDamagePacketS2C> TYPE = new Type<>(TerraCurio.asResource("attack_damage"));
    public static final StreamCodec<ByteBuf, AttackDamagePacketS2C> STREAM_CODEC = ByteBufCodecs.FLOAT.map(AttackDamagePacketS2C::new, AttackDamagePacketS2C::amount);

    @Override
    public Type<AttackDamagePacketS2C> type() {
        return TYPE;
    }

    @Override
    public void work(Player player) {
        InformationHandler.handleAttackDamage(this, player);
    }

    public static void sendToClient(float amount, @Nullable Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new AttackDamagePacketS2C(amount));
        }
    }
}
