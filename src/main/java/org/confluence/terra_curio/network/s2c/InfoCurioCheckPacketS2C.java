package org.confluence.terra_curio.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Team;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.confluence.terra_curio.util.CuriosUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static org.confluence.terra_curio.common.component.AccessoriesComponent.*;

public record InfoCurioCheckPacketS2C(int playerId, byte[] enabled) implements CustomPacketPayload {
    public static final Type<InfoCurioCheckPacketS2C> TYPE = new Type<>(TerraCurio.asResource("info_curio_check"));
    public static final StreamCodec<ByteBuf, InfoCurioCheckPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.playerId,
            ByteBufCodecs.BYTE_ARRAY, p -> p.enabled,
            InfoCurioCheckPacketS2C::new
    );
    public static final int ARRAY_LENGTH = 12;
    public static final byte[] FULL_MYSELF_ARRAY = new byte[]{3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    public static final byte[] FULL_REMOTE_ARRAY = new byte[]{-128, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    @Override
    public @NotNull Type<InfoCurioCheckPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().isLocalPlayer()) {
                InformationHandler.handlePacket(this, context.player());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToPlayer(ServerPlayer serverPlayer, Inventory inventory) {
        ArrayList<ItemStack> itemStacks = CuriosUtils.getCurios(serverPlayer);
        itemStacks.addAll(inventory.items);
        byte watch = 0;
        byte weatherRadio = 0;
        byte sextant = 0;
        byte fishermansPocketGuide = 0;
        byte metalDetector = 0;
        byte lifeFormAnalyzer = 0;
        byte radar = 0;
        byte tallyCounter = 0;
        byte dpsMeter = 0;
        byte stopwatch = 0;
        byte compass = 0;
        byte depthMeter = 0;
        for (ItemStack stack : itemStacks) {
            AccessoriesComponent component = stack.get(TCDataComponentTypes.ACCESSORIES);
            if (component == null) continue;
            if (component.contains(FULL_INFORMATION)) {
                PacketDistributor.sendToPlayer(serverPlayer, new InfoCurioCheckPacketS2C(serverPlayer.getId(), FULL_MYSELF_ARRAY));
                return;
            }

            if (watch < 1 && component.contains(HOUR_WATCH)) watch = 1;
            else if (watch < 2 && component.contains(HALF_HOUR_WATCH)) watch = 2;
            else if (watch < 3 && component.contains(MINUTE_WATCH)) watch = 3;
            if (component.contains(WEATHER_RADIO)) weatherRadio = 1;
            if (component.contains(SEXTANT)) sextant = 1;
            if (component.contains(FISHERMANS_POCKET_GUIDE)) fishermansPocketGuide = 1;
            if (component.contains(METAL_DETECTOR)) metalDetector = 1;
            if (component.contains(LIFE_FORM_ANALYZER)) lifeFormAnalyzer = 1;
            if (component.contains(RADAR)) radar = 1;
            if (component.contains(TALLY_COUNTER)) tallyCounter = 1;
            if (component.contains(DPS_METER)) dpsMeter = 1;
            if (component.contains(STOPWATCH)) stopwatch = 1;
            if (component.contains(COMPASS)) compass = 1;
            if (component.contains(DEPTH_METER)) depthMeter = 1;
        }
        PacketDistributor.sendToPlayer(serverPlayer, new InfoCurioCheckPacketS2C(serverPlayer.getId(), new byte[]{
                watch, weatherRadio, sextant, fishermansPocketGuide, metalDetector,
                lifeFormAnalyzer, radar, tallyCounter, dpsMeter, stopwatch, compass, depthMeter
        }));
    }

    public static void sendToOthers(ServerPlayer serverPlayer) {
        ArrayList<ItemStack> itemStacks = CuriosUtils.getCurios(serverPlayer);
        itemStacks.addAll(serverPlayer.getInventory().items);
        byte watch = -125;
        byte weatherRadio = -128;
        byte sextant = -128;
        byte fishermansPocketGuide = -128;
        byte metalDetector = -128;
        byte lifeFormAnalyzer = -128;
        byte radar = -128;
        byte tallyCounter = -128;
        byte dpsMeter = -128;
        byte stopwatch = -128;
        byte compass = -128;
        byte depthMeter = -128;
        for (ItemStack stack : itemStacks) {
            AccessoriesComponent component = stack.get(TCDataComponentTypes.ACCESSORIES);
            if (component == null) continue;
            if (component.contains(FULL_INFORMATION)) {
                PacketDistributor.sendToPlayer(serverPlayer, new InfoCurioCheckPacketS2C(serverPlayer.getId(), FULL_REMOTE_ARRAY));
                return;
            }

            if (watch > -126 && component.contains(HOUR_WATCH)) watch = -126;
            else if (watch > -127 && component.contains(HALF_HOUR_WATCH)) watch = -127;
            else if (watch > -128 && component.contains(MINUTE_WATCH)) watch = -128;
            if (component.contains(WEATHER_RADIO)) weatherRadio = -1;
            if (component.contains(SEXTANT)) sextant = -1;
            if (component.contains(FISHERMANS_POCKET_GUIDE)) fishermansPocketGuide = -1;
            if (component.contains(METAL_DETECTOR)) metalDetector = -1;
            if (component.contains(LIFE_FORM_ANALYZER)) lifeFormAnalyzer = -1;
            if (component.contains(RADAR)) radar = -1;
            if (component.contains(TALLY_COUNTER)) tallyCounter = -1;
            if (component.contains(DPS_METER)) dpsMeter = -1;
            if (component.contains(STOPWATCH)) stopwatch = -1;
            if (component.contains(COMPASS)) compass = -1;
            if (component.contains(DEPTH_METER)) depthMeter = -1;
        }
        boolean equals = watch == -125 && weatherRadio == -128 && sextant == -128 && fishermansPocketGuide == -128 && metalDetector == -128 &&
                lifeFormAnalyzer == -128 && radar == -128 && tallyCounter == -128 && dpsMeter == -128 && stopwatch == -128 && compass == -128 && depthMeter == -128;
        if (equals) return; // 如果不需要发送, 则返回
        InfoCurioCheckPacketS2C packet = new InfoCurioCheckPacketS2C(serverPlayer.getId(), new byte[]{
                watch, weatherRadio, sextant, fishermansPocketGuide, metalDetector,
                lifeFormAnalyzer, radar, tallyCounter, dpsMeter, stopwatch, compass, depthMeter
        });
        Team team = serverPlayer.getTeam();
        serverPlayer.serverLevel().players().forEach(player -> {
            if (player != serverPlayer && player.getTeam() == team && player.distanceToSqr(serverPlayer) < 1024.0) {
                PacketDistributor.sendToPlayer(player, packet);
            }
        });
    }
}
