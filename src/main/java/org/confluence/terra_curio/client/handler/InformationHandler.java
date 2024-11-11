package org.confluence.terra_curio.client.handler;

import com.google.common.collect.EvictingQueue;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.Tags;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.client.TCKeyBindings;
import org.confluence.terra_curio.common.init.TCCommonConfigs;
import org.confluence.terra_curio.network.s2c.AttackDamagePacketS2C;
import org.confluence.terra_curio.network.s2c.EntityKilledPacketS2C;
import org.confluence.terra_curio.network.s2c.InfoCurioCheckPacketS2C;
import org.confluence.terra_curio.network.s2c.WindSpeedPacketS2C;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public final class InformationHandler {
    public static final int WATCH = 0;
    public static final int WEATHER_RADIO = 1;
    public static final int SEXTANT = 2;
    public static final int FISHERMANS_POCKET_GUIDE = 3;
    public static final int METAL_DETECTOR = 4;
    public static final int LIFE_FORM_ANALYZER = 5;
    public static final int RADAR = 6;
    public static final int TALLY_COUNTER = 7;
    public static final int DPS_METER = 8;
    public static final int STOPWATCH = 9;
    public static final int COMPASS = 10;
    public static final int DEPTH_METER = 11;

    private static final ArrayList<Component> INFORMATION = new ArrayList<>();
    private static final byte[] INFO_DATA = new byte[InfoCurioCheckPacketS2C.ARRAY_LENGTH];
    private static final Int2ObjectOpenHashMap<byte[]> REMOTE_DATA = new Int2ObjectOpenHashMap<>();

    private static @Nullable Function<Long, Component> timeInfo = null;
    private static final Vector2f WIND_SPEED = new Vector2f();
    private static String windSpeedInfo = "0.00";
    private static Component weatherRadioInfo = Component.translatable("info.terra_curio.weather_radio.clear", "0.00");
    private static boolean detectorPressed = false;
    private static Component metalDetectorInfo = Component.translatable("info.terra_curio.metal_detector.none");
    private static Component lifeFormAnalyzerInfo = Component.translatable("info.terra_curio.life_form_analyzer.none");
    private static Component radarInfo = Component.translatable("info.terra_curio.radar", 0);
    private static Component tallyCounterInfo = Component.translatable("info.terra_curio.tally_counter.unknown");
    private static long lastAttackTime = 0;
    private static final EvictingQueue<Float> ATTACK_DAMAGE = EvictingQueue.create(5);
    private static Component dpsMeterInfo = Component.translatable("info.terra_curio.dps_meter", 0.00F);

    public static void handle(LocalPlayer localPlayer) {
        INFORMATION.clear();
        long gameTime = localPlayer.level().getGameTime();

        byte b = INFO_DATA[WATCH];
        if (b != 0 && timeInfo != null) {
            INFORMATION.add(timeInfo.apply(localPlayer.level().dayTime()));
        }

        long tenSec = gameTime % 200;
        if (INFO_DATA[WEATHER_RADIO] != 0) {
            if (tenSec == WEATHER_RADIO) weatherRadioInfo = getWeatherInfo(localPlayer);
            INFORMATION.add(weatherRadioInfo);
        }

        if (INFO_DATA[SEXTANT] != 0) {
            INFORMATION.add(Component.translatable("info.terra_curio.sextant." + localPlayer.level().getMoonPhase()));
        }

        if (INFO_DATA[FISHERMANS_POCKET_GUIDE] != 0) {
            INFORMATION.add(getFishingPowerInfo(localPlayer));
        }

        b = INFO_DATA[METAL_DETECTOR];
        if (TCKeyBindings.METAL_DETECTOR.get().isDown()) {
            if (!detectorPressed && b != 0) {
                detectorPressed = true;
                metalDetectorInfo = getMetalDetectorInfo(localPlayer);
            }
        } else detectorPressed = false;
        if (b != 0) {
            INFORMATION.add(metalDetectorInfo);
        }

        if (INFO_DATA[LIFE_FORM_ANALYZER] != 0) {
            if (tenSec == LIFE_FORM_ANALYZER) lifeFormAnalyzerInfo = getLifeFormAnalyzerInfo(localPlayer);
            INFORMATION.add(lifeFormAnalyzerInfo);
        }

        if (INFO_DATA[RADAR] != 0) {
            if (tenSec == RADAR) radarInfo = Component.translatable(
                    "info.terra_curio.radar",
                    localPlayer.level().getEntities(localPlayer, new AABB(localPlayer.getOnPos()).inflate(63.5), entity -> entity instanceof Enemy).size()
            );
            INFORMATION.add(radarInfo);
        }

        if (INFO_DATA[TALLY_COUNTER] != 0) {
            INFORMATION.add(tallyCounterInfo);
        }

        if (INFO_DATA[DPS_METER] != 0) {
            long delta = gameTime - lastAttackTime;
            if (delta % 20 == 0) {
                float sum = 0.0F;
                for (float value : ATTACK_DAMAGE) sum += value;
                dpsMeterInfo = Component.translatable(
                        "info.terra_curio.dps_meter",
                        "%.2f".formatted(sum / (ATTACK_DAMAGE.size() + 1))
                );
            }
            INFORMATION.add(dpsMeterInfo);
        }

        if (INFO_DATA[STOPWATCH] != 0) {
            INFORMATION.add(Component.translatable(
                    "info.terra_curio.stopwatch",
                    "%.2f".formatted(Mth.length(
                            localPlayer.getX() - localPlayer.xOld,
                            localPlayer.getY() - localPlayer.yOld,
                            localPlayer.getZ() - localPlayer.zOld
                    ) * 20)
            ));
        }

        if (INFO_DATA[COMPASS] != 0) {
            INFORMATION.add(getCompassInfo(localPlayer));
        }

        if (INFO_DATA[DEPTH_METER] != 0) {
            INFORMATION.add(getDepthMeterInfo(localPlayer));
        }

        if (tenSec == 0) {
            for (int i = 0; i < INFO_DATA.length; i++) {
                if (INFO_DATA[i] >= 0) continue;
                boolean match = false;
                for (Player player : localPlayer.level().players()) {
                    if (player == localPlayer || player.distanceToSqr(localPlayer) > 1024.0) continue;
                    byte[] data = REMOTE_DATA.get(player.getId());
                    if (data == null) continue;
                    if (data[i] > -125) {
                        match = true;
                        break;
                    }
                }
                if (!match) INFO_DATA[i] = 0;
            }
        }
    }

    public static void reset() {
        if (!INFORMATION.isEmpty()) {
            INFORMATION.clear();
            Arrays.fill(INFO_DATA, (byte) 0);
            REMOTE_DATA.clear();
        }
    }

    private static Component getWeatherInfo(Player player) {
        Level level = player.level();
        String weather = level.dimension() == Level.OVERWORLD ? "clear" : "cloudy";
        if (level.isRaining()) {
            if (level.getBiome(player.getOnPos()).is(Tags.Biomes.IS_COLD)) {
                weather = "snow";
            } else {
                weather = "rain";
            }
        } else if (level.isThundering()) {
            weather = "thunder";
        }
        if (TerraCurio.isConfluenceLoaded()) {
            return Component.translatable("info.confluence.weather_radio." + weather, windSpeedInfo);
        }
        return Component.translatable("info.terra_curio.weather_radio." + weather);
    }

    private static Component getFishingPowerInfo(Player player) {
        float fishingPower = TCUtils.getAccessoriesValue(player, ValueType.FISHING$POWER);
        return Component.translatable(
                "info.terra_curio.fishermans_pocket_guide",
                "%.2f".formatted(fishingPower)
        );
    }

    private static Component getMetalDetectorInfo(Player localPlayer) {
        AtomicReference<Component> atomic = new AtomicReference<>(Component.translatable("info.terra_curio.metal_detector.none"));
        localPlayer.level().getBlockStates(new AABB(localPlayer.getOnPos()).inflate(15.5))
                .filter(TCCommonConfigs.rareBlocks::contains)
                .min(Comparator.comparingInt(TCCommonConfigs.rareBlocks::indexOf))
                .ifPresent(blockState -> {
                    Block block = blockState.getBlock();
                    atomic.set(Component.translatable("info.terra_curio.metal_detector", block.getName()));
                });
        return atomic.get();
    }

    private static Component getLifeFormAnalyzerInfo(Player localPlayer) {
        AtomicReference<Component> atomic = new AtomicReference<>(Component.translatable("info.terra_curio.life_form_analyzer.none"));
        localPlayer.level().getEntities(localPlayer, new AABB(localPlayer.getOnPos()).inflate(47.5), entity -> TCCommonConfigs.rareCreatures.contains(entity.getType()))
                .stream().min(Comparator.comparingInt(entity -> TCCommonConfigs.rareCreatures.indexOf(entity.getType())))
                .ifPresent(entity -> atomic.set(Component.translatable("info.terra_curio.life_form_analyzer", entity.getType().getDescription())));
        return atomic.get();
    }

    private static Component getCompassInfo(Player localPlayer) {
        double x = localPlayer.getX();
        double z = localPlayer.getZ();
        return Component.translatable("info.terra_curio.compass." + (x > 0 ? "east" : "west"), "%.2f".formatted(x))
                .append(Component.translatable("info.terra_curio.compass." + (z > 0 ? "south" : "north"), "%.2f".formatted(z)));
    }

    private static Component getDepthMeterInfo(Player localPlayer) {
        double y = localPlayer.getY();
        return Component.translatable("info.terra_curio.depth_meter." + (y > 63 ? "surface" : "underground"), "%.2f".formatted(y));
    }

    public static ArrayList<Component> getInformation() {
        return INFORMATION;
    }

    public static void handlePacket(InfoCurioCheckPacketS2C packet, Player player) {
        byte[] enabled = packet.enabled();
        if (player != null && packet.playerId() != player.getId()) {
            // 存入远程玩家信息
            REMOTE_DATA.put(packet.playerId(), packet.enabled());
        }
        byte b = enabled[WATCH];
        byte c = INFO_DATA[WATCH];
        // 玩家发给自己的信息 || 收到别人共享的信息
        if ((b >= 0 && c >= 0) || (b != -125 && c <= 0)) INFO_DATA[WATCH] = b;
        timeInfo = switch (INFO_DATA[WATCH]) {
            case 1, -126 -> InformationHandler::wrapHour;
            case 2, -127 -> InformationHandler::wrapHalfHour;
            case 3, -128 -> InformationHandler::wrapMinute;
            default -> null;
        };
        setInfoData(enabled, WEATHER_RADIO);
        setInfoData(enabled, SEXTANT);
        setInfoData(enabled, FISHERMANS_POCKET_GUIDE);
        setInfoData(enabled, METAL_DETECTOR);
        setInfoData(enabled, LIFE_FORM_ANALYZER);
        setInfoData(enabled, RADAR);
        setInfoData(enabled, TALLY_COUNTER);
        setInfoData(enabled, DPS_METER);
        setInfoData(enabled, STOPWATCH);
        setInfoData(enabled, COMPASS);
        setInfoData(enabled, DEPTH_METER);
    }

    private static Component wrapHour(long dayTime) {
        long hour = (dayTime % 24000) / 1000 + 6;
        if (hour > 23) hour -= 24;
        return Component.translatable("info.terra_curio.time", format(hour), "00");
    }

    private static Component wrapHalfHour(long dayTime) {
        dayTime = dayTime % 24000;
        long hour = dayTime / 1000 + 6;
        if (hour > 23) hour -= 24;
        String half = dayTime % 1000 > 499 ? "30" : "00";
        return Component.translatable("info.terra_curio.time", format(hour), half);
    }

    private static Component wrapMinute(long dayTime) {
        dayTime = dayTime % 24000;
        long hour = dayTime / 1000 + 6;
        if (hour > 23) hour -= 24;
        long minute = (long) ((dayTime % 1000) * 0.06F);
        return Component.translatable("info.terra_curio.time", format(hour), format(minute));
    }

    private static String format(long time) {
        return (time < 10 ? "0" : "") + time;
    }

    private static void setInfoData(byte[] enabled, int index) {
        byte b = enabled[index];
        byte c = INFO_DATA[index];
        // 玩家发给自己的信息 || 收到别人共享的信息
        if ((b >= 0 && c >= 0) || (b != -128 && c <= 0)) INFO_DATA[index] = b;
    }

    public static void handleEntityKilled(EntityKilledPacketS2C packet) {
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(packet.entityType());
        tallyCounterInfo = Component.translatable("info.terra_curio.tally_counter")
                .append(entityType.getDescription()).append("': " + (packet.amount() + 1));
    }

    public static void handleAttackDamage(AttackDamagePacketS2C packet, Player player) {
        ATTACK_DAMAGE.add(packet.amount());
        lastAttackTime = player.level().getGameTime();
    }

    public static void handleWindSpeed(WindSpeedPacketS2C packet) {
        WIND_SPEED.set(packet.x(), packet.z());
        windSpeedInfo = "%.2f".formatted(WIND_SPEED.length());
    }
}
