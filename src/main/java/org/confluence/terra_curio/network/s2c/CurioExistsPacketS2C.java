package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.component.primitive.PrimitiveValue;
import org.confluence.terra_curio.common.component.primitive.UnitValue;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.confluence.terra_curio.util.CuriosUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record CurioExistsPacketS2C(int item) implements CustomPacketPayload {
    public static final int AUTO_ATTACK = 1;
    public static final int SHIELD_OF_CTHULHU = 1 << 1;
    public static final int TABI = 1 << 2;
    public static final int SCOPE = 1 << 3;
    public static final int GRAVITY_GLOBE = 1 << 4;
    public static final int MAGILUMINESCENCE = 1 << 5;
    public static final Object2IntMap<AccessoriesComponent.Type<Unit, UnitValue>> MAP = Util.make(new Object2IntArrayMap<>(), map -> {
        map.put(AccessoriesComponent.AUTO_ATTACK, AUTO_ATTACK);
        map.put(AccessoriesComponent.CTHULHU, SHIELD_OF_CTHULHU);
        map.put(AccessoriesComponent.TABI, TABI);
        map.put(AccessoriesComponent.SCOPE, SCOPE);
        map.put(AccessoriesComponent.GRAVITY, GRAVITY_GLOBE);
    });

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
                TCClientPacketHandler.handleCurioExists(this);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToClient(ServerPlayer player) {
        int item = 0;
        for (ItemStack itemStack : CuriosUtils.getCurios(player)) {
            AccessoriesComponent component = itemStack.get(TCDataComponentTypes.ACCESSORIES);
            if (component != null) {
                Map<AccessoriesComponent.Type<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> types = component.types();
                for (Object2IntMap.Entry<AccessoriesComponent.Type<Unit, UnitValue>> entry : MAP.object2IntEntrySet()) {
                    if (types.containsKey(entry.getKey())) item |= entry.getIntValue();
                }
            }
        }
        PacketDistributor.sendToPlayer(player, new CurioExistsPacketS2C(item));
    }
}
