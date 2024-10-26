package org.confluence.mod.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.client.handler.InformationHandler;
import org.jetbrains.annotations.NotNull;

public record AttackDamagePacketS2C(float amount) implements CustomPacketPayload {
    public static final Type<AttackDamagePacketS2C> TYPE = new Type<>(TerraCurio.asResource("attack_damage"));
    public static final StreamCodec<ByteBuf, AttackDamagePacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, p -> p.amount,
            AttackDamagePacketS2C::new
    );

    @Override
    public @NotNull Type<AttackDamagePacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                InformationHandler.handleAttackDamage(this, context.player());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
