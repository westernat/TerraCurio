package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.mixed.ILivingEntity;
import org.confluence.terra_curio.util.CuriosUtils;

import java.util.HashSet;
import java.util.Set;

public record FluidWalkUpdatePacketS2C() implements CustomPacketPayload {
    public static final Type<FluidWalkUpdatePacketS2C> TYPE = new Type<>(TerraCurio.asResource("fluid_walk_update"));
    public static final StreamCodec<ByteBuf, FluidWalkUpdatePacketS2C> STREAM_CODEC = CustomPacketPayload.codec(FluidWalkUpdatePacketS2C::encode, FluidWalkUpdatePacketS2C::decode);

    private void encode(ByteBuf byteBuf) {}

    private static FluidWalkUpdatePacketS2C decode(ByteBuf byteBuf) {
        return new FluidWalkUpdatePacketS2C();
    }

    @Override
    public Type<FluidWalkUpdatePacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                TCClientPacketHandler.handleFluidWalk(context.player());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer(serverPlayer, new FluidWalkUpdatePacketS2C());
        reset(serverPlayer);
    }

    public static void reset(Player player) {
        Set<FluidState> walkableFluidStates = new HashSet<>();
        Set<TagKey<Fluid>> tagKeys = CuriosUtils.calculateValue(player, TCItems.FLUID$WALK);
        BuiltInRegistries.FLUID.stream().flatMap(fluid -> fluid.getStateDefinition().getPossibleStates().stream()).forEach(state -> {
            if (tagKeys.stream().anyMatch(state::is)) {
                walkableFluidStates.add(state);
            }
        });
        ((ILivingEntity) player).terra_curio$resetLastWalkedFluidState(walkableFluidStates);
    }
}
