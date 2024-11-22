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
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.UnitValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.util.CuriosUtils;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record CurioExistsPacketS2C(int item) implements CustomPacketPayload {
    public static final int AUTO_ATTACK = 1;
    public static final int SHIELD_OF_CTHULHU = 1 << 1;
    public static final int TABI = 1 << 2;
    public static final int SCOPE = 1 << 3;
    public static final int GRAVITY_GLOBE = 1 << 4;
    public static final int MAGILUMINESCENCE = 1 << 5;
    public static final int FLOAT_ON_LIQUID_SURFACE = 1 << 6;
    public static final Object2IntMap<ValueType<Unit, UnitValue>> MAP = Util.make(new Object2IntArrayMap<>(), map -> {
        map.put(TCItems.AUTO$ATTACK, AUTO_ATTACK);
        map.put(TCItems.SHIELD$OF$CTHULHU, SHIELD_OF_CTHULHU);
        map.put(TCItems.SPRINTING, TABI);
        map.put(TCItems.SCOPE, SCOPE);
        map.put(TCItems.GRAVITY$GLOBE, GRAVITY_GLOBE);
        map.put(TCItems.$MAGILUMINESCENCE, MAGILUMINESCENCE);
        map.put(TCItems.FLOAT$ON$LIQUID$SURFACE, FLOAT_ON_LIQUID_SURFACE);
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
            AccessoriesComponent component = TCUtils.getAccessoriesComponent(itemStack);
            if (component != null) {
                Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> types = component.types();
                for (Object2IntMap.Entry<ValueType<Unit, UnitValue>> entry : MAP.object2IntEntrySet()) {
                    if (types.containsKey(entry.getKey())) item |= entry.getIntValue();
                }
            }
        }
        PacketDistributor.sendToPlayer(player, new CurioExistsPacketS2C(item));
    }
}
