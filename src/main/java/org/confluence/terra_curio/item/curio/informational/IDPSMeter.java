package org.confluence.terra_curio.item.curio.informational;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;
import org.confluence.terra_curio.network.NetworkHandler;
import org.confluence.terra_curio.network.s2c.AttackDamagePacketS2C;

public interface IDPSMeter {
    static Component getInfo(float amount) {
        return Component.translatable(
            "info.terra_curio.dps_meter",
            "%.2f".formatted(amount)
        );
    }

    static void sendMsg(float amount, Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            NetworkHandler.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> serverPlayer),
                new AttackDamagePacketS2C(amount, serverPlayer.level().getGameTime())
            );
        }
    }

    Component TOOLTIP = Component.translatable("curios.tooltip.dps_meter");
    byte INDEX = 8;
}
