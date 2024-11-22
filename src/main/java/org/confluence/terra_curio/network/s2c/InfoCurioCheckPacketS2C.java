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
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.util.CuriosUtils;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public record InfoCurioCheckPacketS2C(int playerId, byte[] enabled) implements CustomPacketPayload {
    public static final Type<InfoCurioCheckPacketS2C> TYPE = new Type<>(TerraCurio.asResource("info_curio_check"));
    public static final StreamCodec<ByteBuf, InfoCurioCheckPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.playerId,
            ByteBufCodecs.BYTE_ARRAY, p -> p.enabled,
            InfoCurioCheckPacketS2C::new
    );
    public static final int ARRAY_LENGTH = 13;
    public static final byte[] FULL_MYSELF_ARRAY = new byte[]{3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    public static final byte[] FULL_REMOTE_ARRAY = new byte[]{-128, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public static final double MAX_SHARE_DISTANCE_SQR = 1024.0;

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

    public static void sendToClient(ServerPlayer serverPlayer, Inventory inventory) {
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
        byte mechanicalView = 0;
        for (ItemStack stack : itemStacks) {
            AccessoriesComponent component = TCUtils.getAccessoriesComponent(stack);
            if (component == null) continue;
            if (component.contains(TCItems.FULL$INFORMATION)) {
                PacketDistributor.sendToPlayer(serverPlayer, new InfoCurioCheckPacketS2C(serverPlayer.getId(), FULL_MYSELF_ARRAY));
                return;
            }

            if (watch < 1 && component.contains(TCItems.HOUR$WATCH)) watch = 1;
            else if (watch < 2 && component.contains(TCItems.HALF$HOUR$WATCH)) watch = 2;
            else if (watch < 3 && component.contains(TCItems.MINUTE$WATCH)) watch = 3;
            if (weatherRadio == 0 && component.contains(TCItems.WEATHER$RADIO)) weatherRadio = 1;
            if (sextant == 0 && component.contains(TCItems.$SEXTANT)) sextant = 1;
            if (fishermansPocketGuide == 0 && component.contains(TCItems.FISHERMANS$POCKET$GUIDE)) fishermansPocketGuide = 1;
            if (metalDetector == 0 && component.contains(TCItems.METAL$DETECTOR)) metalDetector = 1;
            if (lifeFormAnalyzer == 0 && component.contains(TCItems.LIFE$FORM$ANALYZER)) lifeFormAnalyzer = 1;
            if (radar == 0 && component.contains(TCItems.$RADAR)) radar = 1;
            if (tallyCounter == 0 && component.contains(TCItems.TALLY$COUNTER)) tallyCounter = 1;
            if (dpsMeter == 0 && component.contains(TCItems.DPS$METER)) dpsMeter = 1;
            if (stopwatch == 0 && component.contains(TCItems.$STOPWATCH)) stopwatch = 1;
            if (compass == 0 && component.contains(TCItems.$COMPASS)) compass = 1;
            if (depthMeter == 0 && component.contains(TCItems.DEPTH$METER)) depthMeter = 1;
            if (mechanicalView == 0 && component.contains(TCItems.MECHANICAL$LENS)) mechanicalView = 1;
        }
        PacketDistributor.sendToPlayer(serverPlayer, new InfoCurioCheckPacketS2C(serverPlayer.getId(), new byte[]{
                watch, weatherRadio, sextant, fishermansPocketGuide, metalDetector, lifeFormAnalyzer,
                radar, tallyCounter, dpsMeter, stopwatch, compass, depthMeter, mechanicalView
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
        byte mechanicalView = -128;
        for (ItemStack stack : itemStacks) {
            AccessoriesComponent component = TCUtils.getAccessoriesComponent(stack);
            if (component == null) continue;
            if (component.contains(TCItems.FULL$INFORMATION)) {
                PacketDistributor.sendToPlayer(serverPlayer, new InfoCurioCheckPacketS2C(serverPlayer.getId(), FULL_REMOTE_ARRAY));
                return;
            }

            if (watch > -126 && component.contains(TCItems.HOUR$WATCH)) watch = -126;
            else if (watch > -127 && component.contains(TCItems.HALF$HOUR$WATCH)) watch = -127;
            else if (watch > -128 && component.contains(TCItems.MINUTE$WATCH)) watch = -128;
            if (weatherRadio == -128 && component.contains(TCItems.WEATHER$RADIO)) weatherRadio = -1;
            if (sextant == -128 && component.contains(TCItems.$SEXTANT)) sextant = -1;
            if (fishermansPocketGuide == -128 && component.contains(TCItems.FISHERMANS$POCKET$GUIDE)) fishermansPocketGuide = -1;
            if (metalDetector == -128 && component.contains(TCItems.METAL$DETECTOR)) metalDetector = -1;
            if (lifeFormAnalyzer == -128 && component.contains(TCItems.LIFE$FORM$ANALYZER)) lifeFormAnalyzer = -1;
            if (radar == -128 && component.contains(TCItems.$RADAR)) radar = -1;
            if (tallyCounter == -128 && component.contains(TCItems.TALLY$COUNTER)) tallyCounter = -1;
            if (dpsMeter == -128 && component.contains(TCItems.DPS$METER)) dpsMeter = -1;
            if (stopwatch == -128 && component.contains(TCItems.$STOPWATCH)) stopwatch = -1;
            if (compass == -128 && component.contains(TCItems.$COMPASS)) compass = -1;
            if (depthMeter == -128 && component.contains(TCItems.DEPTH$METER)) depthMeter = -1;
            if (mechanicalView == -128 && component.contains(TCItems.MECHANICAL$LENS)) mechanicalView = -1;
        }
        boolean equals = watch == -125 && weatherRadio == -128 && sextant == -128 && fishermansPocketGuide == -128 && metalDetector == -128 && lifeFormAnalyzer == -128 &&
                radar == -128 && tallyCounter == -128 && dpsMeter == -128 && stopwatch == -128 && compass == -128 && depthMeter == -128 && mechanicalView == -128;
        if (equals) return; // 如果不需要发送, 则返回
        InfoCurioCheckPacketS2C packet = new InfoCurioCheckPacketS2C(serverPlayer.getId(), new byte[]{
                watch, weatherRadio, sextant, fishermansPocketGuide, metalDetector, lifeFormAnalyzer,
                radar, tallyCounter, dpsMeter, stopwatch, compass, depthMeter, mechanicalView
        });
        Team team = serverPlayer.getTeam();
        serverPlayer.serverLevel().players().forEach(player -> {
            if (player != serverPlayer && player.getTeam() == team && player.distanceToSqr(serverPlayer) < MAX_SHARE_DISTANCE_SQR) {
                PacketDistributor.sendToPlayer(player, packet);
            }
        });
    }
}
