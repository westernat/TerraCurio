package org.confluence.terra_curio.common.data.gen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.CurioItems;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModLanguageProvider extends LanguageProvider {
    private final Map<String, String> enData = new TreeMap<>();
    private final Map<String, String> cnData = new TreeMap<>();
    private final PackOutput output;
    private final String locale;

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, TerraCurio.MODID, locale);
        this.output = output;
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        add("creativetab.terra_curio", "Terra Curio", "泰拉饰品");
        add("curios.tooltip.speed_boots", "The wearer can run super fast", "穿戴者可飞速奔跑");
        add("curios.tooltip.may_fly", "Allows flight", "可飞行");
        add("curios.tooltip.jump_boost", "Increases jump height", "增加跳跃高度");
        add("curios.tooltip.multi_jump", "Allows the holder to double jump", "可让持有者二连跳");
        add("curios.tooltip.negates_fall_damage", "Increases fall resistance", "消除掉落伤害");
        add("curios.tooltip.fall_resistance", "Negates fall damage", "增加抗坠落性");
        add("curios.tooltip.watch", "Tell the time", "报时");
        add("curios.tooltip.fire_immune", "Grants immunity to fire blocks", "对火块免疫");
        add("curios.tooltip.fluid_walk.part", "Provides the ability to walk on water & honey", "提供在水和蜂蜜上行走的能力");
        add("curios.tooltip.fluid_walk.all", "Provides the ability t walk on water, honey & lava", "提供在水、蜂蜜、岩浆上行走的能力");
        add("curios.tooltip.lava_immune", "Provides 7 seconds of immunity to lava", "对岩浆免疫7秒");
        add("curios.tooltip.lava_hurt_reduce", "Reduces damage from touching lava", "减少因触碰熔岩而造成的伤害");
        add("curios.tooltip.fire_attack", "Melee attacks inflict fire damage", "近战攻击造成火焰伤害");
        add("curios.tooltip.auto_attack", "Enables auto swing for melee weapons", "启用近战武器自动挥动");
        add("curios.tooltip.aggro_attach", "Enemies are more likely to target you", "多人模式时，敌人更可能以你为目标");
        add("curios.tooltip.armor_pass", "Increases armor penetration by %s", "盔甲穿透力提高%s点");
        add("curios.tooltip.projectile_attack", "%s%% increased ranged damage", "远程伤害提高%s%%");
        add("curios.tooltip.compass", "Displays horizontal position", "显示水平位置");
        add("curios.tooltip.depth_meter", "Displays depth", "显示深度");
        add("curios.tooltip.dps_meter", "Displays your damage per second", "显示你的每秒伤害");
        add("curios.tooltip.fishermans_pocket_guide", "Displays fishing information", "显示钓鱼信息");
        add("curios.tooltip.life_form_analyzer", "Displays the name of rare creatures around you", "显示您周围稀有生物的名称");
        add("curios.tooltip.metal_detector", "Displays the most valuable ore around you", "显示你周围最贵重的矿石");
        add("curios.tooltip.radar", "Detects enemies around you", "探测你周围的敌人");
        add("curios.tooltip.sextant", "Displays the phase of the moon", "显示月相");
        add("curios.tooltip.stopwatch", "Displays how fast the player is moving", "显示玩家的移动速度");
        add("curios.tooltip.tally_counter", "Displays how many monsters have been killed", "显示怪物击杀数量");
        add("curios.tooltip.weather_radio", "Displays the weather", "显示天气");
        add("curios.tooltip.scope", "Increases view range for ranged weapons", "扩大远程武器的视野范围");
        add("curios.tooltip.scope2", "Hold ranged weapon and crouch to zoom multiOut", "手持远程武器并潜行可拉远视野");
        add("curios.tooltip.wall_climb", "Allows the ability to climb walls, hold shift key to slide down", "可爬墙，按住shift键可快速下滑");
        add("curios.tooltip.wall_slide", "Allows the ability to slide down walls, hold shift key to slide down quickly", "可沿墙滑下，按住shift键可更快地下滑");
        add("curios.tooltip.tabi", "Allows the ability to dash while double tap a direction", "双击一个方向可猛冲");
        add("curios.tooltip.dodge", "Gives a chance to dodge attacks", "有几率避开攻击");

        add("info.terra_curio.time", "Time: [%s:%s]", "时间: [%s:%s]");
        add("info.terra_curio.radar", "Enemies: %s", "敌人: %s");
        add("info.terra_curio.compass.east", "East: %s, ", "东: %s, ");
        add("info.terra_curio.compass.west", "West: %s, ", "西: %s, ");
        add("info.terra_curio.compass.south", "South: %s", "南: %s");
        add("info.terra_curio.compass.north", "North: %s", "北: %s");
        add("info.terra_curio.depth_meter.surface", "Surface: %s", "地表: %s");
        add("info.terra_curio.depth_meter.underground", "Underground: %s", "地下: %s");
        add("info.terra_curio.tally_counter.unknown", "Kill count unavailable", "击杀数不可用");
        add("info.terra_curio.tally_counter", "Killed '", "已杀死 '");
        add("info.terra_curio.life_form_analyzer.none", "No rare creatures nearby!", "未发现稀有生物");
        add("info.terra_curio.life_form_analyzer", "%s detected nearby!", "发现稀有生物: %s");
        add("info.terra_curio.metal_detector.none",  "No treasure nearby!", "未发现稀有方块");
        add("info.terra_curio.metal_detector", "%s detected nearby!", "在附近发现%s!");
        add("info.terra_curio.stopwatch", "Speed: %s m/s", "速度: %s m/s");
        add("info.terra_curio.dps_meter", "DPS: %s", "DPS: %s");
        add("info.terra_curio.sextant.0", "Moon phase: Full Moon", "月相: 满月");
        add("info.terra_curio.sextant.1",  "Moon phase: Waning Gibbous", "月相: 亏凸月");
        add("info.terra_curio.sextant.2", "Moon phase: Third Quarter", "月相: 下弦月");
        add("info.terra_curio.sextant.3", "Moon phase: Waning Crescent", "月相: 残月");
        add("info.terra_curio.sextant.4", "Moon phase: New Moon", "月相: 新月");
        add("info.terra_curio.sextant.5", "Moon phase: Waxing Crescent", "月相: 峨眉月");
        add("info.terra_curio.sextant.6", "Moon phase: First Quarter", "月相: 上弦月");
        add("info.terra_curio.sextant.7", "Moon phase: Waxing Gibbous", "月相: 盈凸月");
        add("info.terra_curio.weather_radio.clear", "Weather: Clear", "天气: 晴天");
        add("info.terra_curio.weather_radio.cloudy", "Weather: Cloudy", "天气: 阴天,");
        add("info.terra_curio.weather_radio.rain", "Weather: Rain", "天气: 下雨");
        add("info.terra_curio.weather_radio.snow", "Weather: Snow", "天气: 下雪");
        add("info.terra_curio.weather_radio.thunder", "Weather: Thunder", "天气: 雷暴");
        add("info.terra_curio.fishermans_pocket_guide", "Fishing Power: %s", "渔力: %s");

        add("key.terra_curio.metal_detector", "Detect Metal", "检测矿物");
        add("key.terra_curio.step_stool", "Step Stool", "上梯凳");

        add("curios.identifier.accessory", "Accessory", "配饰");
        add("curios.modifiers.accessory", "When worn as accessory:", "佩戴配饰时：");

        addItem(CurioItems.BEZOAR, "bezoar", "牛黄");
        addTooltips(CurioItems.BEZOAR, "Immunity to Poison", "对中毒免疫");
        addJeiTooltips(CurioItems.BEZOAR,
                new String[]{"The Bezoar is an immunity accessory that grants the player immunity to the Poisoned debuff", "It have a Chance to be dropped from Cave Spider."},
                new String[]{"牛黄是一种免疫配饰，可赋予玩家对中毒减益的免疫力", "它有几率从洞穴蜘蛛中掉落"}
        );
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cache) {
        addTranslations();
        Path path = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(TerraCurio.MODID).resolve("lang");
        if (locale.equals("en_us") && !enData.isEmpty()) {
            return save(enData, cache, path.resolve("en_us.json"));
        }
        if (locale.equals("zh_cn") && !cnData.isEmpty()) {
            return save(cnData, cache, path.resolve("zh_cn.json"));
        }
        return CompletableFuture.allOf();
    }

    private CompletableFuture<?> save(Map<String, String> data, CachedOutput cache, Path target) {
        JsonObject json = new JsonObject();
        data.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, target);
    }

    private void addItem(Supplier<? extends Item> key, String en, String cn) {
        add(key.get().getDescriptionId(), en, cn);
    }

    private void addTooltips(Supplier<? extends Item> key, String en, String cn) {
        add("tooltip." + key.get().getDescriptionId(), en, cn);
    }

    private void addJeiTooltips(Supplier<? extends Item> key, String[] en, String[] cn) {
        if (en.length == cn.length) {
            for (int i = 0; i < en.length; i++) {
                String enLang = en[i];
                String cnLang = cn[i];
                add("jei.tooltip." + key.get().getDescriptionId(), enLang, cnLang);
            }
        }
    }

    private void add(String key, String en, String zh) {
        if (locale.equals("en_us") && !enData.containsKey(key)) {
            enData.put(key, en);
        } else if (locale.equals("zh_cn") && !cnData.containsKey(key)) {
            cnData.put(key, zh);
        }
    }
}