package org.confluence.mod.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.client.handler.InformationHandler;
import org.jetbrains.annotations.NotNull;

public record EntityKilledPacketS2C(int amount, ResourceLocation entityType) implements CustomPacketPayload {
    public static final Type<EntityKilledPacketS2C> TYPE = new Type<>(TerraCurio.asResource("entity_killed"));
    public static final StreamCodec<ByteBuf, EntityKilledPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.amount,
            ResourceLocation.STREAM_CODEC, p -> p.entityType,
            EntityKilledPacketS2C::new
    );

    @Override
    public @NotNull Type<EntityKilledPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                InformationHandler.handleEntityKilled(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
