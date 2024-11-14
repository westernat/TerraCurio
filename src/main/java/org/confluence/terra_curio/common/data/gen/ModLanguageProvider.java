package org.confluence.terra_curio.common.data.gen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.*;
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
        onlyTooltip(TCItems.AMBER_HORSESHOE_BALLOON, "琥珀马掌气球", "Increases jump height and negates fall damage", "增加跳跃高度、消除掉落伤害");
        onlyZhName(TCItems.AMBHIPIAN_BOOTS, "水陆两用靴");
        tooltipAndJeiInfo(TCItems.ANCIENT_CHISEL, "远古凿子", "“Age-old problems require age-old solutions”", "“古老的问题需要古老的解决方案”", "The item can be pass the Archaeology to obtained.", "该物品可以通过考古获得。");
        onlyJeiInfo(TCItems.ANGLER_EARRING, "渔夫耳环", "This item can be located in the chests found within Fisherman Villagers' houses.", "这件物品可以在渔夫村民家中的箱子中找到。");
        onlyTooltip(TCItems.ANKH_CHARM, "十字章护身符", "Grants immunity to most debuffs.", "对大部分减益免疫。");
        onlyTooltips(TCItems.ANKH_SHIELD, "十字章护盾", new String[]{"Grants immunity to most debuffs.", "Grants immunity to fire blocks"}, new String[]{"对大部分减益免疫。", "对火块免疫"});
        onlyZhName(TCItems.ANKLET_OF_THE_WIND, "疾风脚镯");
        onlyTooltip(TCItems.ARCHITECT_GIZMO_PACK, "建筑师发明背包", "Decreased 'Right Click Delay' by 2, cannot stack the decrease of its materials", "右键点击延迟降低2，且降低效果不能与其材料的降低效果叠加。");
        onlyZhName(TCItems.AVENGER_EMBLEM, "复仇者勋章");
        onlyZhName(TCItems.BALLOON_PUFFERFISH, "气球河豚鱼");
        tooltipAndJeiInfo(TCItems.BAND_OF_REGENERATION, "再生手环", "Slowly regenerates life", "缓慢再生生命", "The Band of Regeneration is an accessory that have a Chance to be dropped from Witch", "它有几率从女巫身上掉落。");
        onlyZhName(TCItems.BEE_CLOAK, "蜜蜂斗篷");
        onlyZhName(TCItems.BERSERKERS_GLOVE, "狂战士手套");
        tooltipAndJeiInfo(TCItems.BEZOAR, "牛黄", "Immunity to Poison", "对中毒免疫", "It have a Chance to be dropped from Cave Spider.", "它有几率从洞穴蜘蛛身上掉落。");
        onlyJeiInfo(TCItems.BLACK_BELT, "黑腰带", "It have a Chance to be dropped from Wither Skeleton.", "它有几率从凋零骷髅身上掉落。");
        tooltipAndJeiInfo(TCItems.BLINDFOLD, "蒙眼布", "Immunity to Blindness", "对失明免疫", "It can be discovered in chests found within Ancient City.", "它可以在古代城内的箱子中找到。");
        onlyZhName(TCItems.BLIZZARD_IN_A_BALLOON, "暴雪气球");
        onlyZhName(TCItems.BLIZZARD_IN_A_BOTTLE, "暴雪瓶");
        onlyZhName(TCItems.BLUE_HORSESHOE_BALLOON, "蓝马掌气球");
        tooltipsAndJeiInfo(TCItems.BRAIN_OF_CONFUSION, "混乱之脑",
                new String[]{
                        "Has a chance to create illusions and dodge an attack",
                        "Temporarily increase critical chance after dodge",
                        "May confuse nearby enemies after being struck"
                },
                new String[]{
                        "有几率制造幻觉并躲避攻击",
                        "闪避后暂时增加暴击几率",
                        "被击中后可能会迷惑附近的敌人"
                },
                "It have a Chance to be dropped from Zombie Villager.",
                "它有几率从僵尸村民身上掉落。"
        );
        tooltipAndJeiInfo(TCItems.BRICK_LAYER, "砌砖刀", "Decreased 'Right Click Delay' by 1", "右键点击延迟降低1", "This item can be located in the chests found within Toolsmith Villagers' houses.", "这件物品可以在工具匠村民家中的箱子中找到。");
        onlyTooltip(TCItems.BUNDLE_OF_BALLOONS, "气球束", "Allows the holder to quadruple jump", "可让持有者四段跳");
        onlyZhName(TCItems.BUNDLE_OF_HORSESHOE_BALLOONS, "马掌气球束");
        onlyTooltip(TCItems.CELESTIAL_STONE, "天界石", "Slightly increases the attribute value", "小幅提高属性值");
        onlyTooltips(TCItems.CELL_PHONE, "手机",
                new String[]{
                        "Displays everything",
                        "Allows you to return home at will"
                },
                new String[]{
                        "显示所有信息",
                        "可随意回家"
                }
        );
        tooltipAndJeiInfo(TCItems.CLIMBING_CLAWS, "攀爬爪", "Improved ability if combined with Shoe Spikes", "结合鞋钉使用时能力还会有所提升", "It have a Chance to be dropped from Spider.", "它有几率从蜘蛛身上掉落。");

        onlyZhName(TCItems.CLOUD_IN_A_BALLOON, "云朵气球");
        onlyZhName(TCItems.CLOUD_IN_A_BOTTLE, "云朵瓶");
        tooltipAndJeiInfo(TCItems.COBALT_SHIELD, "钴护盾", "Grants immunity to knockback", "对击退免疫", "It have a Chance to be dropped from Ravager.", "它有几率从劫掠兽身上掉落。");
        tooltipAndJeiInfo(TCItems.COMPASS, "罗盘", "Displays horizontal position", "显示水平位置", "The Compass have a Chance to be dropped from Bats.", "它有几率从蝙蝠身上掉落。");
        onlyZhName(TCItems.COPPER_WATCH, "铜表");
        tooltipAndJeiInfo(TCItems.CROSS_NECKLACE, "十字项链","Increases length of invincibility after taking damage", "增加受到伤害后的无敌时间", "It have a Chance to be dropped from Evoker.", "它有几率从唤魔者身上掉落。");
        tooltipAndJeiInfo(TCItems.DEMON_HEART, "恶魔之心", "Permanently increases the number of accessory slots", "永久增加配饰栏数量", "It to be dropped from Wither.", "它必定从凋零身上掉落。");
        tooltipAndJeiInfo(TCItems.DEPTH_METER, "深度计", "Displays depth", "显示深度", "The Compass have a Chance to be dropped from Bats.", "它有几率从蝙蝠身上掉落。");
        onlyZhName(TCItems.DESTROYER_EMBLEM, "毁灭者勋章");
        onlyTooltip(TCItems.DETOXIFICATION_CAPSULE, "解毒囊", "Immunity to Poison and Wither", "对中毒和凋零免疫");
        tooltipAndJeiInfo(TCItems.DPS_METER, "每秒伤害计数器", "Displays your damage per second", "显示你的每秒伤害", "The DPS Meter have a Chance to be dropped from Creeper.", "它有几率从苦力怕身上掉落。");
        tooltipsAndJeiInfo(TCItems.DUNERIDER_BOOTS, "沙丘行者靴",
                new String[]{
                        "The wearer can run super fast, and even faster on sand",
                        "'Walk without rhythm and you won't attract the worm'"
                },
                new String[]{
                        "穿戴者可飞速奔跑，在沙地上还能跑得更快",
                        "“无节律行走就不会引来蠕虫”"
                },
                "It have a Chance to be dropped from Husk.",
                "它有几率从尸壳身上掉落。"
        );
        tooltipAndJeiInfo(TCItems.ENERGY_BAR, "能量棒", "Immunity to Hunger", "对饥饿免疫",  "It have a Chance to be dropped from Zombified Piglin.",  "它有几率从僵尸猪灵身上掉落。");
        onlyTooltip(TCItems.EXPLORERS_EQUIPMENT, "探险家宝具", "Immunity to Mining Fatigue and Levitation", "对挖掘疲劳和漂浮免疫");
        onlyJeiInfo(TCItems.EXTENDO_GRIP, "加长握爪", "It can be discovered in chests found within Desert Pyramids.", "它可以在沙漠神殿内的箱子中找到");
        onlyJeiInfo(TCItems.EYE_OF_THE_GOLEM, "石巨人之眼", "It can be discovered in chests found within Jungle Pyramids.", "它可以在丛林神庙内的箱子中找到");
        onlyZhName(TCItems.FAIRY_BOOTS, "仙灵靴");
        onlyZhName(TCItems.FART_IN_A_BALLOON, "臭屁气球");
        onlyJeiInfo(TCItems.FART_IN_A_JAR, "罐中臭屁", "It have a Chance to be dropped from Ghost.", "它有几率从恶魂身上掉落");
        tooltipAndJeiInfo(TCItems.FAST_CLOCK, "快走时钟", "Immunity to Slow", "对缓慢免疫",  "It have a Chance to be dropped from Stray.",  "它有几率从流浪者身上掉落。");
        onlyJeiInfo(TCItems.FERAL_CLAWS, "狂爪手套", "It can be discovered in chests found within Jungle Pyramids.", "它可以在丛林神庙内的箱子中找到");
        onlyTooltip(TCItems.FIRE_GAUNTLET, "烈火手套", "Increases melee knockback and melee attacks inflict fire damage", "增加近战击退并使攻击附着火焰伤害");
        onlyZhName(TCItems.FISH_FINDER, "探鱼器");
        tooltipAndJeiInfo(TCItems.FISHERMANS_POCKET_GUIDE, "渔民袖珍宝典", "Displays fishing information", "显示钓鱼信息",  "This item can be located in the chests found within Fisherman Villagers' houses.", "这件物品可以在渔夫村民家中的箱子中找到。");
        tooltipAndJeiInfo(TCItems.FLASHLIGHT, "手电筒", "Immunity to Darkness", "对黑暗免疫",  "It can be discovered in chests found within Stronghold.", "它可以在要塞内的箱子中找到。");
        onlyJeiInfo(TCItems.FLESH_KNUCKLES, "血肉指虎", "It have a Chance to be dropped from Piglin Brute.", "它有几率从猪灵蛮兵身上掉落");
        onlyJeiInfo(TCItems.FLIPPER, "脚蹼", "It can be found in Chests in the Shipwreck.", "它出现在沉船宝箱中");
        tooltipAndJeiInfo(TCItems.FLOWER_BOOTS, "花靴", "Flowers grow on the grass you walk on", "你走过的草地上会长出花朵",  "It has a chance of falling from digging through Moss Block.", "它有几率从挖掘苔藓块时掉落。");
        onlyJeiInfo(TCItems.FLURRY_BOOTS, "疾风雪靴", "It can be found in Igloo's chest.", "它出现在雪地小屋中。");
        onlyZhName(TCItems.FROG_FLIPPER, "青蛙脚蹼");
        onlyTooltip(TCItems.FROG_GEAR, "青蛙装备", "'It ain't easy being green'", "“身为绿皮生物可太难了”");
        onlyJeiInfo(TCItems.FROG_LEG, "蛙腿", "The Frog Leg have a Chance to be dropped from Frog.", "它有几率从青蛙身上掉落。");
        onlyZhName(TCItems.FROG_WEBBING, "青蛙蹼");
        onlyTooltip(TCItems.FROSTSPARK_BOOTS, "霜花靴", "Allows flight, super fast running, and extra mobility on ice",  "可飞行、飞速奔跑、并提供额外冰面行动力");
        onlyTooltips(TCItems.FROZEN_SHIELD, "冰冻护盾",
                new String[]{
                        "Absorbs 25% of damage done to players on your team when above 25% life",
                        "Puts a shell around the owner when below 50% life that reduces damage by 25%"
                },
                new String[]{
                        "当生命值超过25%时，吸收对团队中玩家造成的25%伤害",
                        "当生命值低于50%时，在主人周围放置一个外壳，使伤害降低25%"
                }
        );
        tooltipAndJeiInfo(TCItems.FROZEN_TURTLE_SHELL, "冰冻海龟壳", "Puts a shell around the owner when below 50% life that reduces damage by 25%", "当生命值低于50%时，在主人周围放置一个外壳，使伤害降低25%",  "When the Turtle died for Freezing chance to dropped this item.", "当海龟因冰冻而死时，有几率掉落这个饰品。。");
        onlyZhName(TCItems.GOBLIN_TECH, "哥布林数据仪");
        onlyZhName(TCItems.GOLD_WATCH, "金表");
        onlyZhName(TCItems.GPS, "全球定位系统");
        tooltipsAndJeiInfo(TCItems.GRAVITY_GLOBE, "重力球",
                new String[]{
                        "Allows the holder to reverse gravity",
                        "Press Jump to change gravity"
                },
                new String[]{
                        "可让持有者反转重力",
                        "按跳跃键可改变重力"
                },
                "It have a Chance to be dropped from Shulker.",
                "它有几率从潜影贝身上掉落。"
        );
        onlyZhName(TCItems.GREEN_HORSESHOE_BALLOON, "绿马掌气球");
        tooltipAndJeiInfo(TCItems.HAND_DRILL, "手钻", "Immunity to Mining Fatigue", "对挖掘疲劳免疫",  "It have a Chance to be dropped from Elder Guardian.", "它有几率从远古守卫者身上掉落。");
        onlyTooltip(TCItems.HAND_OF_CREATION, "创造之手", "Decreased 'Right Click Delay' by 3, cannot stack the decrease of its material",  "右键点击延迟降低3，且降低效果不能与其材料的降低效果叠加");
        onlyTooltip(TCItems.HAND_WARMER, "暖手宝", "Provides immunity to chill and freezing effects",  "对寒冷和冰冻效果免疫");
        onlyJeiInfo(TCItems.HERMES_BOOTS, "赫尔墨斯靴", "It can be found in Chests in the Village Armorer.",  "它可以在村庄盔甲匠的宝箱中找到");
        onlyZhName(TCItems.HERO_SHIELD, "英雄护盾");
        tooltipAndJeiInfo(TCItems.HIVE_PACK, "蜂巢背包", "Increases the strength of friendly bees", "增加友好蜜蜂的力量",   "It have a Chance to be dropped from Bee.", "它有几率从蜜蜂身上掉落。");
        tooltipAndJeiInfo(TCItems.HOLY_WATER, "圣水", "Immunity to Wither", "对凋零免疫",   "It have a Chance to be dropped from Wither Skeleton.", "它有几率从凋零骷髅身上掉落。");
        onlyZhName(TCItems.HONEY_BALLOON, "蜂蜜气球");
        tooltipAndJeiInfo(TCItems.HONEY_COMB, "蜂窝", "Releases bees and douses the user in honey when damaged", "受到伤害后释放蜜蜂并将使用者浸入蜂蜜中",   "It have a Chance to be dropped from Bee.", "它有几率从蜜蜂身上掉落。");
        onlyTooltip(TCItems.ICE_SKATES, "溜冰鞋", "Provides extra mobility on ice",  "提供额外冰面行动力");
        onlyJeiInfo(TCItems.LAVA_CHARM, "熔岩护身符", "It have a Chance to be dropped from Blaze.",  "它有几率从烈焰人掉落");
        onlyTooltip(TCItems.LAVA_WADERS, "熔岩靴", "Grants immunity to fire blocks and 7 seconds of immunity to lava",  "对火块免疫、对岩浆免疫7秒");
        tooltipAndJeiInfo(TCItems.LIFE_FORM_ANALYZER, "生命体分析机", "Displays the name of rare creatures around you", "显示您周围稀有生物的名称",  "The Lifeform Analyzer have a Chance to be dropped from Glow Squid.", "它有几率从发光鱿鱼身上掉落。");
        onlyTooltip(TCItems.LIGHTNING_BOOTS, "闪电靴", "Allows flight, super fast running",  "可飞行、可飞速奔跑");
        tooltipAndJeiInfo(TCItems.LUCKY_HORSESHOE, "幸运马掌", "'Said to bring good fortune and keep evil spirits at bay'", "“据说能带来好运、驱除邪灵”",  "It can be discovered in chests found within Desert Pyramids, End City, Jungle Pyramids, or Ancient Cities.", "它可以在沙漠神殿，末地城，丛林遗迹或古代城市的箱子中找到。");
        onlyTooltip(TCItems.MAGIC_MIRROR, "魔镜", "Gaze in the mirror to return home",  "盯着镜子便可回家");
        tooltipAndJeiInfo(TCItems.MAGIC_QUIVER, "魔法箭袋", "20% chance to not consume arrows", "20%几率不消耗箭",  "This item have a Chance to be dropped from Skeleton.", "这件物品有几率从骷髅身上掉落");
        tooltipsAndJeiInfo(TCItems.MAGILUMINESCENCE, "魔光护符",
                new String[]{
                        "Increases movement speed and acceleration",
                        "'A brief light in my dark life.'"
                },
                new String[]{
                        "提高移动速度和加速度",
                        "我黑暗生命中的一道短暂曙光"
                },
                "It can be found in End City Chests.",
                "它可以在末地城的宝箱中找到。"
        );
        onlyTooltip(TCItems.MAGMA_SKULL, "岩浆骷髅头", "Immunity to fire blocks, melee attacks deal fire damage",  "对火块免疫、近战攻击造成火焰伤害");
        onlyJeiInfo(TCItems.MAGMA_STONE, "岩浆石", "It have a Chance to be dropped from Blaze.",  "它有几率从烈焰人掉落");
        onlyZhName(TCItems.MASTER_NINJA_GEAR, "忍者大师装备");
        onlyZhName(TCItems.MECHANICAL_GLOVE, "机械手套");
        tooltipAndJeiInfo(TCItems.METAL_DETECTOR, "金属探测器", "Displays the most valuable ore around you", "显示你周围最贵重的矿石",  "The Metal Detector have a Chance to be dropped from Warden.", "它必定从监守者身上掉落。");
        onlyZhName(TCItems.MOLTEN_CHARM, "熔火护身符");
        onlyTooltips(TCItems.MOLTEN_QUIVER, "熔火箭袋",
                new String[]{
                        "Lights wooden arrows ablaze",
                        "'Quiver in fear!'"
                },
                new String[]{
                        "点燃木箭，火光熊熊",
                        "在恐惧中颤抖吧！"
                }
        );
        onlyZhName(TCItems.MOLTEN_SKULL_ROSE, "熔火骷髅头玫瑰");
        tooltipAndJeiInfo(TCItems.MOON_STONE, "月亮石", "It have a Chance to be dropped from Phantom.", "在夜晚时略微增强属性值",  "It have a Chance to be dropped from Phantom.", "它有几率从幻翼身上掉落。");
        onlyTooltip(TCItems.NUTRIENT_SOLUTION, "营养液", "Immunity to Weakness and Hunger",  "对虚弱和饥饿免疫");
        onlyZhName(TCItems.OBSIDIAN_HORSESHOE, "黑曜石马掌");
        onlyJeiInfo(TCItems.OBSIDIAN_ROSE, "黑曜石玫瑰", "It can be discovered in chests found within Bastion Remnant.",  "它可以在堡垒遗迹中发现的箱子中找到");
        onlyZhName(TCItems.OBSIDIAN_SHIELD, "黑曜石护盾");
        onlyZhName(TCItems.OBSIDIAN_SKULL, "黑曜石骷髅头");
        onlyZhName(TCItems.OBSIDIAN_SKULL_ROSE, "黑曜石骷髅头玫瑰");
        onlyZhName(TCItems.OBSIDIAN_WATER_WALKING_BOOTS, "黑曜石水上漂靴");
        onlyJeiInfo(TCItems.PALADINS_SHIELD, "圣骑士护盾", "It to be dropped from Ender Dragon.",  "它必定从末影龙身上掉落。");
        tooltipAndJeiInfo(TCItems.PANIC_NECKLACE, "恐慌项链",  "Increases movement speed after taking damage", "受到伤害后增加移动速度",  "It can be found in Chests in the Dungeon.", "它可以在地牢中的宝箱中找到。");
        onlyZhName(TCItems.PDA, "个人数字助手");
        onlyZhName(TCItems.PINK_HORSESHOE_BALLOON, "粉马掌气球");
        onlyZhName(TCItems.PLATINUM_WATCH, "铂金表");
        tooltipAndJeiInfo(TCItems.PORTABLE_CEMENT_MIXER, "便携式水泥搅拌机",  "Decreased 'Right Click Delay' by 1", "右键点击延迟降低1",  "This item can be located in the chests found within Toolsmith Villagers' houses.", "这件物品可以在工具匠村民家中的箱子中找到。");
        onlyZhName(TCItems.POWER_GLOVE, "强力手套");
        tooltipAndJeiInfo(TCItems.PUTRID_SCENT, "腐香囊",  "Enemies are less likely to target you", "多人模式下，敌怪不太可能以你为目标",  "It can be found in Chests in the Dungeon.", "它可以在地牢中的宝箱中找到。");
        tooltipAndJeiInfo(TCItems.RADAR, "雷达",  "Detects enemies around you", "探测你周围的敌人",  "The Radar have a Chance to be dropped from Bats.", "它有几率从蝙蝠身上掉落。");
        onlyJeiInfo(TCItems.RANGER_EMBLEM, "游侠徽章", "It have a Chance to be dropped from Skeleton.",  "它有几率从凋零骷髅身上掉落。");
        onlyTooltip(TCItems.RECON_SCOPE, "侦察镜", "'Enemy spotted'",  "“发现敌人”");
        onlyZhName(TCItems.REK_3000, "R.E.K.3000");
        onlyJeiInfo(TCItems.RIFLE_SCOPE, "步枪瞄准镜", "It have a Chance to be dropped from Enderman.",  "它有几率从末影人身上掉落。");
        onlyZhName(TCItems.ROCKET_BOOTS, "火箭靴");
        onlyTooltip(TCItems.ROYAL_GEL, "皇家凝胶",  "Slimes become friendly",  "史莱姆将变成友好生物");
        onlyJeiInfo(TCItems.SAILFISH_BOOTS, "旗鱼靴", "It can be found in Shipwreck Chests.",  "它可以在沉船宝箱中找到。");
        onlyZhName(TCItems.SANDSTORM_IN_A_BALLOON, "沙暴气球");
        onlyJeiInfo(TCItems.SANDSTORM_IN_A_BOTTLE, "沙暴瓶", "It can be found in Chests in the Desert Pyramid.",  "它可以在沙漠神殿的宝箱中找到。");
        onlyTooltip(TCItems.SEARCHLIGHT, "探照灯", "Immunity to Blindness and Darkness",  "对失明和黑暗免疫");
        onlyJeiInfo(TCItems.SEXTANT, "六分仪", "This item can be located in the chests found within Fisherman Villagers' houses.",  "这件物品可以在渔夫村民家中的箱子中找到。");
        onlyJeiInfo(TCItems.SHACKLE, "脚镣", "It can be found in Chests in the Dungeon.",  "它可以在地牢中的宝箱中找到。");
        onlyJeiInfo(TCItems.SHARK_TOOTH_NECKLACE, "鲨牙项链", "It has a chance to drop from Drowned.",  "它可以在地牢中的宝箱中找到。");
        onlyZhName(TCItems.SHARKRON_BALLOON, "鲨鱼龙气球");
        tooltipAndJeiInfo(TCItems.SHIELD_OF_CTHULHU, "克苏鲁护盾", "Allows the player to dash into the enemy, sprinting to dsh",  "允许冲刺，疾跑以冲刺" ,"It have a Chance to be dropped from Ravager.", "它有几率从劫掠兽身上掉落。");
        onlyJeiInfo(TCItems.SHINY_RED_BALLOON, "闪亮红气球",  "When the player has the Hero of the Village effect, the librarian has a chance to give the player this item.It can be found in chests in plains villages.",  "它可以在平原村庄中的宝箱中找到。当玩家拥有村庄英雄效果时，图书管理员有几率将此物品交给玩家。");
        tooltipAndJeiInfo(TCItems.SHOE_SPIKES, "鞋钉", "Improved ability if combined with Climbing Claws",  "结合攀爬爪使用时能力还会有所提升" ,"It have a Chance to be dropped from Cave Spider.", "它有几率从蜘蛛身上掉落。");
        tooltipAndJeiInfo(TCItems.SHOT_PUT, "铅球", "Immunity to Levitation",  "对漂浮免疫" ,"It can be found in Chests in the Stronghold.", "它可以在要塞的宝箱中找到。");
        onlyZhName(TCItems.SILVER_WATCH, "银表");
        onlyZhName(TCItems.SNIPER_SCOPE, "狙击镜");
        onlyJeiInfo(TCItems.SORCERER_EMBLEM, "巫师徽章", "It have a Chance to be dropped from Evoker.",  "它有几率从唤魔者身上掉落。");
        onlyZhName(TCItems.SPECTRE_BOOTS, "幽灵靴");
        onlyZhName(TCItems.STALKERS_QUIVER, "潜行者箭袋");
        tooltipAndJeiInfo(TCItems.STAR_CLOAK, "星星斗篷", "Causes stars to fall after taking damage",  "受到伤害后会使星星坠落" ,"It can be discovered in chests found within Woodland Mansion.", "它可以在林地府邸内的箱子中找到。");
        onlyZhName(TCItems.STAR_VEIL, "星星面纱");
        tooltipsAndJeiInfo(TCItems.MAGILUMINESCENCE, "魔光护符",
                new String[]{
                        "Increases movement speed and acceleration",
                        "'A brief light in my dark life.'"
                },
                new String[]{
                        "提高移动速度和加速度",
                        "‘我黑暗生命中的一道短暂曙光’"
                },
                "It can be found in End City Chests.",
                "它可以在末地城的宝箱中找到。"
        );
        tooltipsAndJeiInfo(TCItems.STEP_STOOL, "梯凳",
            new String[]{
                "Press ↑ key to stand higher, and press Shift key to down",
                "Extra Step: %s"
            },
            new String[]{
                "按↑键站得更高，按Shift键下来",
                "额外高度：%s"
            },
            "You can use Step Stool to upgrade its Extra Step from Smithing Table.",
            "你可以在锻造台使用梯凳升级它的额外高度。"
        );
        onlyTooltip(TCItems.STINGER_NECKLACE, "毒刺项链", "Releases bees and douses the user in honey when damaged.", "受到伤害后释放蜜蜂并将使用者浸入蜂蜜中。");
        onlyTooltip(TCItems.STOPWATCH, "秒表", "Displays how fast the player is moving", "显示玩家的移动速度");
        tooltipsAndJeiInfo(TCItems.SUN_STONE, "太阳石",
            new String[]{
                "During daytime, grants minor increase."
            },
            new String[]{
                "在白天时略微增强属性值"
            },
            "It have a Chance to be dropped from Blaze.",
            "它有几率由烈焰人掉落。");
        onlyTooltip(TCItems.SWEETHEART_NECKLACE, "甜心项链", "When damaged, the bee is released and the user is immersed in honey and increases movement speed.", "受到伤害后释放蜜蜂并将使用者浸入蜂蜜中、并提高移动速度。");
        tooltipsAndJeiInfo(TCItems.TABI, "分趾厚底袜",
            new String[]{
                "Allows the ability to dash while double tap a direction"
            },
            new String[]{
                "双击一个方向可猛冲"
            },
            "It can be found in Chests in the Fortress.",
            "它可以在要塞的宝箱中找到。"
        );
        tooltipsAndJeiInfo(TCItems.TALLY_COUNTER, "杀怪计数器",
            new String[]{
                "Displays how many monsters have been killed"
            },
            new String[]{
                "显示怪物击杀数量"
            },
            "The Tally Counter a Chance to be dropped from Creeper.",
            "它有几率从爬行者（苦力怕）身上掉落。"
        );
        onlyTooltip(TCItems.TERRASPARK_BOOTS, "泰拉闪耀靴", "Grants immunity to fire blocks and 7 seconds of immunity to lava", "对火块免疫并在7秒内对熔岩免疫");
        onlyTooltip(TCItems.THE_PLAN, "计划书", "Immunity to Slow and Nausea", "对缓慢和反胃免疫");
        onlyJeiInfo(TCItems.TITAN_GLOVE, "泰坦手套", "It can be found in Chests in the Mineshaft.", "它可以在废弃矿井的宝箱中找到。");
        onlyJeiInfo(TCItems.TOOLBELT, "工具腰带", "It can be located in the chests found within Toolsmith Villagers' houses.", "它可以在工具匠村民家中的箱子中找到。");
        onlyJeiInfo(TCItems.TOOLBOX, "工具箱", "It can be located in the chests found within Toolsmith Villagers' houses.", "它可以在工具匠村民家中的箱子中找到");
        tooltipsAndJeiInfo(TCItems.TREASURE_MAGNET, "宝藏磁石",
            new String[]{
                "Expanded item pickup"
            },
            new String[]{
                "扩大物品拾取范围"
            },
            "It can be discovered in chests found within Desert Pyramids, End City, Jungle Pyramids, or Ancient Cities.",
            "它可以在沙漠神殿、末地城、丛林神庙或古代城内的宝箱中找到。"
        );
        tooltipsAndJeiInfo(TCItems.TRIFOLD_MAP, "三折地图",
            new String[]{
                "Immunity to Nausea"
            },
            new String[]{
                "对反胃免疫"
            },
            "It can be found in Chests in the Stronghold Library.",
            "它可以在要塞图书馆的宝箱中找到。"
        );
        onlyJeiInfo(TCItems.TSUNAMI_IN_A_BOTTLE, "海啸瓶", "It can be found in Shipwreck Chests.", "它可以在沉船宝箱中找到。");
        tooltipsAndJeiInfo(TCItems.VITAMINS, "维生素",
            new String[]{
                "Immunity to Weakness"
            },
            new String[]{
                "对虚弱免疫"
            },
            "It have a Chance to be dropped from Witch.",
            "它有几率从女巫身上掉落。"
        );
        onlyJeiInfo(TCItems.WARRIOR_EMBLEM, "战士徽章", "It have a Chance to be dropped from Vindicator.", "它有几率从卫道士身上掉落。");
        tooltipsAndJeiInfo(TCItems.WATER_WALKING_BOOTS, "水上漂靴",
            new String[]{
                "Provides the ability to walk on water & honey"
            },
            new String[]{
                "提供在水和蜂蜜上行走的能力"
            },
            "It can be found in Ocean Ruins Chests.",
            "它可以在海洋废墟宝箱中找到。"
        );
        tooltipsAndJeiInfo(TCItems.WEATHER_RADIO, "weather_radio",
            new String[]{
                "Displays the weather"
            },
            new String[]{
                "显示天气"
            },
            "This item can be located in the chests found within Fisherman Villagers' houses.",
            "这件物品可以在渔夫村民家中的箱子中找到。"
        );
        tooltipsAndJeiInfo(TCItems.WORM_SCARF, "蠕虫围巾",
            new String[]{
                "Reduces damage taken by 17%"
            },
            new String[]{
                "所受伤害减少17%"
            },
            "When a player has a tamed cat, the cat may give the player this item when they wake up.",
            "当玩家有一只被驯服的猫时，猫可能会在玩家醒来时给玩家这个物品。"
            );
        onlyZhName(TCItems.YELLOW_HORSESHOE_BALLOON, "黄马掌气球");

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
        add(TCBlocks.WORKSHOP.get().getDescriptionId(), "Workshop", "工匠作坊");
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