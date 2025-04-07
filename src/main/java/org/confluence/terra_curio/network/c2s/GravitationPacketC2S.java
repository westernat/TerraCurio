package org.confluence.terra_curio.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.effect.GravitationEffect;
import org.confluence.terra_curio.mixed.IEntity;
import org.confluence.terra_curio.network.s2c.BroadcastGravitationRotPacketS2C;

public record GravitationPacketC2S(boolean enable) implements CustomPacketPayload {
    public static final Type<GravitationPacketC2S> TYPE = new Type<>(TerraCurio.asResource("gravitation"));
    public static final StreamCodec<ByteBuf, GravitationPacketC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, p -> p.enable,
            GravitationPacketC2S::new
    );

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.resetFallDistance();
                AttributeMap attributeMap = serverPlayer.getAttributes();
                if (enable) {
                    attributeMap.addTransientAttributeModifiers(GravitationEffect.GRAVITY);
                } else {
                    AttributeInstance attributeInstance = attributeMap.getInstance(Attributes.GRAVITY);
                    if (attributeInstance != null) attributeInstance.removeModifier(GravitationEffect.ID);
                }
                ((IEntity) serverPlayer).terra_curio$setShouldRot(enable);
                PacketDistributor.sendToAllPlayers(new BroadcastGravitationRotPacketS2C(serverPlayer.getId(), enable));
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    @Override
    public Type<GravitationPacketC2S> type() {
        return TYPE;
    }
}
