package org.confluence.terra_curio.network.c2s;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes.PortAttributesExtension;
import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.effect.GravitationEffect;
import org.confluence.terra_curio.mixed.IEntity;
import org.confluence.terra_curio.network.s2c.BroadcastGravitationRotPacketS2C;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.UUID;

public record GravitationPacketC2S(boolean enable) implements IPortPacket.C2S {
    public static final ResourceLocation ID = TerraCurio.asResource("gravitation");
    public static final UUID UUID = java.util.UUID.fromString("gravitation");
    public static final PortStreamCodec<ByteBuf, GravitationPacketC2S> STREAM_CODEC = PortByteBufCodecs.BOOL.map(GravitationPacketC2S::new, GravitationPacketC2S::enable);

    @Override
    public ResourceLocation identifier() {
        return ID;
    }

    @Override
    public void work(ServerPlayer player) {
        player.resetFallDistance();
        AttributeMap attributeMap = player.getAttributes();
        if (enable) {
            attributeMap.addTransientAttributeModifiers(GravitationEffect.GRAVITY);
        } else {
            AttributeInstance attributeInstance = attributeMap.getInstance(PortAttributesExtension.gravity().value());
            if (attributeInstance != null) attributeInstance.removeModifier(UUID);
        }
        IEntity.of(player).terra_curio$setShouldRot(enable);
        TerraCurio.HANDLER.sendToAllPlayers(new BroadcastGravitationRotPacketS2C(player.getId(), enable));
    }

    public static void sendToServer(boolean enable) {
        TerraCurio.HANDLER.sendToServer(new GravitationPacketC2S(enable));
    }
}
