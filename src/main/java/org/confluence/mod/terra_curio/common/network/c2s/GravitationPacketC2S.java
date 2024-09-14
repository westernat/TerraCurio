package org.confluence.mod.terra_curio.common.network.c2s;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.terra_curio.TerraCurio;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.NetworkChannel;

public class GravitationPacketC2S implements CustomPacketPayload {

    public final boolean enable;


    public static final Type<GravitationPacketC2S> TYPE = new Type<>(TerraCurio.SPACE("packet.gravity_reverse"));
    public static final StreamCodec<FriendlyByteBuf, GravitationPacketC2S> STREAM_CODEC =
            CustomPacketPayload.codec(GravitationPacketC2S::write, GravitationPacketC2S::new);

    //客户端序列化
    private void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.enable);

    }

    //服务端反序列化
    public GravitationPacketC2S(FriendlyByteBuf buf) {
        this.enable = buf.readBoolean();

    }
    //客户端构造
    public GravitationPacketC2S(boolean enable){
        this.enable = enable;

    }

    //服务端处理方法
    public static void receive(final GravitationPacketC2S packet, final IPayloadContext ctx) {
        //todo
        /*
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer == null) return;
            serverPlayer.resetFallDistance();
            AttributeMap attributeMap = serverPlayer.getAttributes();
            if (packet.enable) {
                attributeMap.addTransientAttributeModifiers(GravitationEffect.GRAVITY);
            } else {
                AttributeInstance attributeInstance = attributeMap.getInstance(ForgeMod.ENTITY_GRAVITY.get());
                if (attributeInstance != null) {
                    attributeInstance.removeModifier(GravitationEffect.GRAVITY_UUID);
                }
            }
            ((IEntity)serverPlayer).c$setShouldRot(packet.enable);
            NetworkHandler.CHANNEL.send(
                    PacketDistributor.ALL.noArg(),
                    new BroadcastGravitationRotPacketS2C(serverPlayer.getId(), packet.enable)
            );
        });

*/
    }


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}