package org.confluence.mod.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.client.handler.ClientPacketHandler;
import org.jetbrains.annotations.NotNull;

public record CurioExistsPacketS2C(int item, boolean enable) implements CustomPacketPayload {
    public static final int ALL = -1;
    public static final int AUTO_ATTACK = 0;
    public static final int CTHULHU = 1;
    public static final int TABI = 2;
    public static final int SCOPE = 3;
    public static final int GRAVITY_GLOBE = 4;

    public static final Type<CurioExistsPacketS2C> TYPE = new Type<>(TerraCurio.asResource("curio_exists"));
    public static final StreamCodec<ByteBuf, CurioExistsPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.item,
            ByteBufCodecs.BOOL, p -> p.enable,
            CurioExistsPacketS2C::new
    );

    @Override
    public @NotNull Type<CurioExistsPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                ClientPacketHandler.handleCurioExists(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
