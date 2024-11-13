package org.confluence.terra_curio.common.data.gen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCAttributes;
import org.confluence.terra_curio.common.init.TCEffects;
import org.confluence.terra_curio.common.init.TCEntities;
import org.confluence.terra_curio.common.init.TCItems;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLanguageProvider extends LanguageProvider {
    private final Map<String, String> enData = new TreeMap<>();
    private final Map<String, String> zhData = new TreeMap<>();
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
        add("info.terra_curio.metal_detector.none", "No treasure nearby!", "未发现稀有方块");
        add("info.terra_curio.metal_detector", "%s detected nearby!", "在附近发现%s!");
        add("info.terra_curio.stopwatch", "Speed: %s m/s", "速度: %s m/s");
        add("info.terra_curio.dps_meter", "DPS: %s", "DPS: %s");
        add("info.terra_curio.sextant.0", "Moon phase: Full Moon", "月相: 满月");
        add("info.terra_curio.sextant.1", "Moon phase: Waning Gibbous", "月相: 亏凸月");
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

        add("container.terra_curio.workshop", "工匠作坊", "Workshop");
        add("title.terra_curio.workshop", "工匠作坊", "Workshop");

        add(TCItems.BASE_POINT.get().getDescriptionId(), "基点", "Base Point");
        add(TCItems.EVERLASTING.get().getDescriptionId(), "亘古", "Everlasting");

        onlyJeiInfo(TCItems.AGLET, "金属带扣", "It can be found in Shipwreck Chests.", "它出现在沉船宝箱中");
        onlyTooltip(TCItems.AMBER_HORSESHOE_BALLOON, "琥珀马掌气球", "增加跳跃高度、消除掉落伤害", "Increases jump height and negates fall damage");
//        "ambhipian_boots": "水陆两用靴",
//        "ancient_chisel": "远古凿子",
//        "angler_earring": "渔夫耳环",
//        "ankh_charm": "十字章护身符",
//        "ankh_shield": "十字章护盾",
//        "anklet_of_the_wind": "疾风脚镯",
//        "architect_gizmo_pack": "建筑师发明背包",
//        "avenger_emblem": "复仇者勋章",
//        "balloon_pufferfish": "气球河豚鱼",
//        "band_of_regeneration": "再生手环",
//        "bee_cloak": "蜜蜂斗篷",
//        "berserkers_glove": "狂战士手套",
        tooltipAndJeiInfo(TCItems.BEZOAR, "牛黄", "Immunity to Poison", "对中毒免疫", "It have a Chance to be dropped from Cave Spider.", "它有几率从洞穴蜘蛛身上掉落。");
//        "black_belt": "黑腰带",
//        "blindfold": "蒙眼布",
//        "blizzard_in_a_balloon": "暴雪气球",
//        "blizzard_in_a_bottle": "暴雪瓶",
//        "blue_horseshoe_balloon": "蓝马掌气球",
//        "brain_of_confusion": "混乱之脑",
//        "brick_layer": "砌砖刀",
//        "bundle_of_balloons": "气球束",
//        "bundle_of_horseshoe_balloons": "马掌气球束",
//        "celestial_stone": "天界石",
//        "cell_phone": "手机",
//        "climbing_claws": "攀爬爪",
//        "cloud_in_a_balloon": "云朵气球",
//        "cloud_in_a_bottle": "云朵瓶",
//        "cobalt_shield": "钴护盾",
//        "compass": "罗盘",
//        "copper_watch": "铜表",
//        "cross_necklace": "十字项链",
        tooltipAndJeiInfo(TCItems.DEMON_HEART, "恶魔之心", "Permanently increases the number of accessory slots", "永久增加配饰栏数量", "It to be dropped from Wither.", "它必定从凋零身上掉落。");
//        "depth_meter": "深度计",
//        "destroyer_emblem": "毁灭者勋章",
//        "detoxification_capsule": "解毒囊",
//        "dps_meter": "每秒伤害计数器",
//        "dunerider_boots": "沙丘行者靴",
//        "energy_bar": "能量棒",
//        "explorers_equipment": "探险家宝具",
//        "extendo_grip": "加长握爪",
//        "eye_of_the_golem": "石巨人之眼",
//        "fairy_boots": "仙灵靴",
//        "fart_in_a_balloon": "臭屁气球",
//        "fart_in_a_jar": "罐中臭屁",
//        "fast_clock": "快走时钟",
//        "feral_claws": "狂爪手套",
//        "fire_gauntlet": "烈火手套",
//        "fish_finder": "探鱼器",
//        "fishermans_pocket_guide": "渔民袖珍宝典",
//        "flashlight": "手电筒",
//        "flesh_knuckles": "血肉指虎",
//        "flipper": "脚蹼",
//        "flower_boots": "花靴",
//        "flurry_boots": "疾风雪靴",
//        "frog_flipper": "青蛙脚蹼",
//        "frog_gear": "青蛙装备",
//        "frog_leg": "蛙腿",
//        "frog_webbing": "青蛙蹼",
//        "frostspark_boots": "霜花靴",
//        "frozen_shield": "冰冻护盾",
//        "frozen_turtle_shell": "冰冻海龟壳",
//        "goblin_tech": "哥布林数据仪",
//        "gold_watch": "金表",
//        "gps": "全球定位系统",
//        "gravity_globe": "重力球",
//        "green_horseshoe_balloon": "绿马掌气球",
//        "hand_drill": "手钻",
//        "hand_of_creation": "创造之手",
//        "hand_warmer": "暖手宝",
//        "hermes_boots": "赫尔墨斯靴",
//        "hero_shield": "英雄护盾",
//        "hive_pack": "蜂巢背包",
//        "holy_water": "圣水",
//        "honey_balloon": "蜂蜜气球",
//        "honey_comb": "蜂窝",
//        "ice_skates": "溜冰鞋",
//        "lava_charm": "熔岩护身符",
//        "lava_waders": "熔岩靴",
//        "life_form_analyzer": "生命体分析机",
//        "lightning_boots": "闪电靴",
//        "lucky_horseshoe": "幸运马掌",
//        "magic_mirror": "魔镜",
//        "magic_quiver": "魔法箭袋",
//        "magiluminescence": "魔光护符",
//        "magma_skull": "岩浆骷髅头",
//        "magma_stone": "岩浆石",
//        "master_ninja_gear": "忍者大师装备",
//        "mechanical_glove": "机械手套",
//        "metal_detector": "金属探测器",
//        "molten_charm": "熔火护身符",
//        "molten_quiver": "熔火箭袋",
//        "molten_skull_rose": "熔火骷髅头玫瑰",
//        "moon_stone": "月亮石",
//        "nutrient_solution": "营养液",
//        "obsidian_horseshoe": "黑曜石马掌",
//        "obsidian_rose": "黑曜石玫瑰",
//        "obsidian_shield": "黑曜石护盾",
//        "obsidian_skull": "黑曜石骷髅头",
//        "obsidian_skull_rose": "黑曜石骷髅头玫瑰",
//        "obsidian_water_walking_boots": "黑曜石水上漂靴",
//        "paladins_shield": "圣骑士护盾",
//        "panic_necklace": "恐慌项链",
//        "pda": "个人数字助手",
//        "pink_horseshoe_balloon": "粉马掌气球",
//        "platinum_watch": "铂金表",
//        "portable_cement_mixer": "便携式水泥搅拌机",
//        "power_glove": "强力手套",
//        "putrid_scent": "腐香囊",
//        "radar": "雷达",
//        "ranger_emblem": "游侠徽章",
//        "recon_scope": "侦察镜",
//        "rek_3000": "R.E.K.3000",
//        "rifle_scope": "步枪瞄准镜",
//        "rocket_boots": "火箭靴",
//        "royal_gel": "皇家凝胶",
//        "sailfish_boots": "旗鱼靴",
//        "sandstorm_in_a_balloon": "沙暴气球",
//        "sandstorm_in_a_bottle": "沙暴瓶",
//        "searchlight": "探照灯",
//        "sextant": "六分仪",
//        "shackle": "脚镣",
//        "shark_tooth_necklace": "鲨牙项链",
//        "sharkron_balloon": "鲨鱼龙气球",
//        "shield_of_cthulhu": "克苏鲁护盾",
//        "shiny_red_balloon": "闪亮红气球",
//        "shoe_spikes": "鞋钉",
//        "shot_put": "铅球",
//        "silver_watch": "银表",
//        "sniper_scope": "狙击镜",
//        "sorcerer_emblem": "巫师徽章",
//        "spectre_boots": "幽灵靴",
//        "stalkers_quiver": "潜行者箭袋",
//        "star_cloak": "星星斗篷",
//        "star_veil": "星星面纱",
//        "step_stool": "梯凳",
//        "stinger_necklace": "毒刺项链",
//        "stopwatch": "秒表",
//        "sun_stone": "太阳石",
//        "sweetheart_necklace": "甜心项链",
//        "tabi": "分趾厚底袜",
//        "tally_counter": "杀怪计数器",
//        "terraspark_boots": "泰拉闪耀靴",
//        "the_plan": "计划书",
//        "tiger_climbing_gear": "猛虎攀爬装备",
//        "tin_watch": "锡表",
//        "titan_glove": "泰坦手套",
//        "toolbelt": "工具腰带",
//        "toolbox": "工具箱",
//        "treasure_magnet": "宝藏磁石",
//        "trifold_map": "三折地图",
//        "tsunami_in_a_bottle": "海啸瓶",
//        "tungsten_watch": "钨表",
//        "vitamins": "维生素",
//        "warrior_emblem": "战士徽章",
//        "water_walking_boots": "水上漂靴",
//        "weather_radio": "天气收音机",
//        "white_horseshoe_balloon": "白马掌气球",
//        "worm_scarf": "蠕虫围巾",
//        "yellow_horseshoe_balloon": "黄马掌气球",

        if (locale.equals("en_us")) {
            sidedAdd(TCItems.EXPLORERS_EQUIPMENT.get().getDescriptionId(), "Explorer's Equipment", enData);
            sidedAdd(TCItems.PALADINS_SHIELD.get().getDescriptionId(), "Paladin's Shield", enData);
            sidedAdd(TCItems.STALKERS_QUIVER.get().getDescriptionId(), "Stalker's Quiver", enData);
            sidedAdd(TCItems.DPS_METER.get().getDescriptionId(), "DPS Meter", enData);
            sidedAdd(TCItems.FISHERMANS_POCKET_GUIDE.get().getDescriptionId(), "Fisherman's Pocket Guide", enData);
            sidedAdd(TCItems.GPS.get().getDescriptionId(), "GPS", enData);
            sidedAdd(TCItems.PDA.get().getDescriptionId(), "PDA", enData);
            sidedAdd(TCItems.REK_3000.get().getDescriptionId(), "R.E.K.3000", enData);
            TCItems.CURIOS.getEntries().forEach(item -> {
                Item item1 = item.get();
                sidedAdd(item1.getDescriptionId(), toTitleCase(item.getId().getPath()), enData);
            });
            TCEntities.ENTITIES.getEntries().forEach(entity -> sidedAdd(entity.get().getDescriptionId(), toTitleCase(entity.getId().getPath()), enData));
            TCEffects.EFFECTS.getEntries().forEach(effect -> sidedAdd(effect.get().getDescriptionId(), toTitleCase(effect.getId().getPath()), enData));
            TCAttributes.ATTRIBUTES.getEntries().forEach(attribute -> sidedAdd(attribute.get().getDescriptionId(), toTitleCase(attribute.getId().getPath()), enData));
        }
    }

    private static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        addTranslations();
        Path path = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(TerraCurio.MODID).resolve("lang");
        if (locale.equals("en_us") && !enData.isEmpty()) {
            return save(enData, cache, path.resolve("en_us.json"));
        }
        if (locale.equals("zh_cn") && !zhData.isEmpty()) {
            return save(zhData, cache, path.resolve("zh_cn.json"));
        }
        return CompletableFuture.allOf();
    }

    private CompletableFuture<?> save(Map<String, String> data, CachedOutput cache, Path target) {
        JsonObject json = new JsonObject();
        data.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, target);
    }

    private void tooltipsAndJeiInfos(Supplier<? extends Item> item, String zhName, String[] enTooltip, String[] zhTooltip, String[] enJeiInfo, String[] zhJeiInfo) {
        String key = item.get().getDescriptionId();
        sidedAdd(key, zhName, zhData);
        addTooltips(key, enTooltip, zhTooltip);
        addJeiInfos(key, enJeiInfo, zhJeiInfo);
    }

    private void tooltipsAndJeiInfo(Supplier<? extends Item> item, String zhName, String[] enTooltip, String[] zhTooltip, String enJeiInfo, String zhJeiInfo) {
        String key = item.get().getDescriptionId();
        sidedAdd(key, zhName, zhData);
        addTooltips(key, enTooltip, zhTooltip);
        add("jei.tooltip." + key + ".0", enJeiInfo, zhJeiInfo);
    }

    private void tooltipAndJeiInfo(Supplier<? extends Item> item, String zhName, String enTooltip, String zhTooltip, String enJeiInfo, String zhJeiInfo) {
        String key = item.get().getDescriptionId();
        sidedAdd(key, zhName, zhData);
        add("tooltip." + key + ".0", enTooltip, zhTooltip);
        add("jei.tooltip." + key + ".0", enJeiInfo, zhJeiInfo);
    }

    private void onlyTooltips(Supplier<? extends Item> item, String zhName, String[] enTooltip, String[] zhTooltip) {
        String key = item.get().getDescriptionId();
        sidedAdd(key, zhName, zhData);
        addTooltips(key, enTooltip, zhTooltip);
    }

    private void onlyJeiInfos(Supplier<? extends Item> item, String zhName, String[] enJeiInfo, String[] zhJeiInfo) {
        String key = item.get().getDescriptionId();
        sidedAdd(key, zhName, zhData);
        addJeiInfos(key, enJeiInfo, zhJeiInfo);
    }

    private void onlyZhName(Supplier<? extends Item> item, String zhName) {
        sidedAdd(item.get().getDescriptionId(), zhName, zhData);
    }

    private void onlyTooltip(Supplier<? extends Item> item, String zhName, String enTooltip, String zhTooltip) {
        String key = item.get().getDescriptionId();
        sidedAdd(key, zhName, zhData);
        add("tooltip." + key + ".0", enTooltip, zhTooltip);
    }

    private void onlyJeiInfo(Supplier<? extends Item> item, String zhName, String enJeiInfo, String zhJeiInfo) {
        String key = item.get().getDescriptionId();
        sidedAdd(key, zhName, zhData);
        add("jei.tooltip." + key + ".0", enJeiInfo, zhJeiInfo);
    }

    private void addTooltip(Supplier<? extends Item> key, int index, String en, String zh) {
        add("tooltip." + key.get().getDescriptionId() + "." + index, en, zh);
    }

    private void addTooltips(String key, String[] enTooltip, String[] zhTooltip) {
        if (enTooltip.length == zhTooltip.length) {
            String tooltip = "tooltip." + key + ".";
            for (int i = 0; i < enTooltip.length; i++) {
                String enLang = enTooltip[i];
                String cnLang = zhTooltip[i];
                add(tooltip + i, enLang, cnLang);
            }
        }
    }

    private void addJeiInfos(String key, String[] enJeiInfo, String[] zhJeiInfo) {
        if (enJeiInfo.length == zhJeiInfo.length) {
            String jeiInfo = "jei.tooltip." + key + ".";
            for (int i = 0; i < enJeiInfo.length; i++) {
                String enLang = enJeiInfo[i];
                String zhLang = zhJeiInfo[i];
                add(jeiInfo + i, enLang, zhLang);
            }
        }
    }

    private void add(String key, String en, String zh) {
        if (locale.equals("en_us") && !enData.containsKey(key)) {
            enData.put(key, en);
        } else if (locale.equals("zh_cn") && !zhData.containsKey(key)) {
            zhData.put(key, zh);
        }
    }

    private void sidedAdd(String key, String value, Map<String, String> side) {
        if (!side.containsKey(key)) side.put(key, value);
    }
}