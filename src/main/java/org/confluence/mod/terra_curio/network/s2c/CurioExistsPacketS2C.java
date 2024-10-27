package org.confluence.mod.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.client.handler.ClientPacketHandler;
import org.confluence.mod.terra_curio.common.component.AccessoriesComponent;
import org.confluence.mod.terra_curio.common.init.ModDataComponentTypes;
import org.confluence.mod.terra_curio.util.CuriosUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record CurioExistsPacketS2C(int item) implements CustomPacketPayload {
    public static final int AUTO_ATTACK = 0b00001;
    public static final int SHIELD_OF_CTHULHU = 0b00010;
    public static final int TABI = 0b00100;
    public static final int SCOPE = 0b01000;
    public static final int GRAVITY_GLOBE = 0b10000;

    public static final Type<CurioExistsPacketS2C> TYPE = new Type<>(TerraCurio.asResource("curio_exists"));
    public static final StreamCodec<ByteBuf, CurioExistsPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.item,
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

    public static void sendToClient(ServerPlayer player) {
        int item = 0b00000;
        for (ItemStack itemStack : CuriosUtils.getCurios(player)) {
            AccessoriesComponent component = itemStack.get(ModDataComponentTypes.ACCESSORIES);
            if (component != null) {
                Set<ResourceLocation> types = component.types();
                if (types.contains(AccessoriesComponent.AUTO_ATTACK)) {
                    item |= AUTO_ATTACK;
                }
                if (types.contains(AccessoriesComponent.SHIELD_OF_CTHULHU)) {
                    item |= SHIELD_OF_CTHULHU;
                }
                if (types.contains(AccessoriesComponent.TABI)) {
                    item |= TABI;
                }
                if (types.contains(AccessoriesComponent.SCOPE)) {
                    item |= SCOPE;
                }
                if (types.contains(AccessoriesComponent.GRAVITY_GLOBE)) {
                    item |= GRAVITY_GLOBE;
                }
            }
        }
        PacketDistributor.sendToPlayer(player, new CurioExistsPacketS2C(item));
    }
}
