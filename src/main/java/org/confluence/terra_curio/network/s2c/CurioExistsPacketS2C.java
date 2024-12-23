package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
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
    public static final Object2IntMap<ValueType<Unit, UnitValue>> MAP = new Object2IntArrayMap<>();

    public static final int AUTO_ATTACK = register(TCItems.AUTO$ATTACK);
    public static final int SHIELD_OF_CTHULHU = register(TCItems.SHIELD$OF$CTHULHU);
    public static final int TABI = register(TCItems.SPRINTING);
    public static final int SCOPE = register(TCItems.SCOPE);
    public static final int GRAVITY_GLOBE = register(TCItems.GRAVITY$GLOBE);
    public static final int MAGILUMINESCENCE = register(TCItems.$MAGILUMINESCENCE);
    public static final int FLOAT_ON_LIQUID_SURFACE = register(TCItems.FLOAT$ON$LIQUID$SURFACE);

    private static int register(ValueType<Unit, UnitValue> type) {
        int i = 1 << MAP.size();
        MAP.put(type, i);
        return i;
    }

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
