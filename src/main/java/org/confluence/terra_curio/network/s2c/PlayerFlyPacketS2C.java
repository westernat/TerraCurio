package org.confluence.terra_curio.network.s2c;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.MayFlyAbilityValue;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.util.TCUtils;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.IntFunction;

public record PlayerFlyPacketS2C(Map<ResourceKey<Item>, MayFlyAbilityValue.FlyStack> flyStacks) implements CustomPacketPayload {
    public static final Type<PlayerFlyPacketS2C> TYPE = new Type<>(TerraCurio.asResource("player_fly"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerFlyPacketS2C> STREAM_CODEC = ByteBufCodecs.map((IntFunction<Map<ResourceKey<Item>, MayFlyAbilityValue.FlyStack>>) IdentityHashMap::new, ResourceKey.streamCodec(Registries.ITEM), MayFlyAbilityValue.FlyStack.STREAM_CODEC).map(PlayerFlyPacketS2C::new, PlayerFlyPacketS2C::flyStacks);

    @Override
    public Type<PlayerFlyPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                PlayerJumpHandler.handleFlyPacket(flyStacks);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer(serverPlayer, new PlayerFlyPacketS2C(TCUtils.getValue(serverPlayer, TCItems.MAY$FLY)));
    }
}
