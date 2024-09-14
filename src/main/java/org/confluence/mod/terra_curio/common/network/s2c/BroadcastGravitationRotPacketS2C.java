package org.confluence.mod.terra_curio.common.network.s2c;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.terra_curio.TerraCurio;
import org.jetbrains.annotations.NotNull;

public class BroadcastGravitationRotPacketS2C implements CustomPacketPayload {

    public final boolean enable;


    public static final Type<BroadcastGravitationRotPacketS2C> TYPE = new Type<>(TerraCurio.SPACE("packet.broadcast_gravity"));
    public static final StreamCodec<FriendlyByteBuf, BroadcastGravitationRotPacketS2C> STREAM_CODEC =
            CustomPacketPayload.codec(BroadcastGravitationRotPacketS2C::write, BroadcastGravitationRotPacketS2C::new);
    //发射端序列化
    private void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.enable);

    }
    //发射端构造
    public BroadcastGravitationRotPacketS2C(boolean enable){
        this.enable = enable;

    }
    //接收端反序列化
    public BroadcastGravitationRotPacketS2C(FriendlyByteBuf buf) {
        this.enable = buf.readBoolean();

    }
    //接收端处理方法
    public static void receive(final BroadcastGravitationRotPacketS2C packet, final IPayloadContext context) {
        //todo


    }


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}