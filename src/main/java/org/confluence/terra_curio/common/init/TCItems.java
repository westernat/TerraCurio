package org.confluence.terra_curio.common.init;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.component.primitive.EntityTypesValue;
import org.confluence.terra_curio.common.component.primitive.FloatValue;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.common.item.curio.combat.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static org.confluence.terra_curio.common.component.AccessoriesComponent.*;
import static org.confluence.terra_curio.common.component.ModRarity.*;

@SuppressWarnings("all")
public final class TCItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraCurio.MODID);

    public static final Supplier<Item> STAR = ITEMS.register("star", () -> new Item(new Item.Properties()));

    public static final Supplier<BaseCurioItem> BEZOAR = registerCurio("bezoar", builder -> builder.rarity(LIGHT_RED).effectImmunities(MobEffects.POISON)), // 牛黄 中毒
            HOLY_WATER = registerCurio("holy_water", builder -> builder.rarity(LIGHT_RED).effectImmunities(MobEffects.WITHER)), // 圣水 凋零
            DETOXIFICATION_CAPSULE = registerCurio("detoxification_capsule", builder -> builder.rarity(PINK).effectImmunities(MobEffects.POISON, MobEffects.WITHER)), // 解毒囊
            VITAMINS = registerCurio("vitamins", builder -> builder.rarity(LIGHT_RED).effectImmunities(MobEffects.WEAKNESS)), // 维生素 虚弱
            ENERGY_BAR = registerCurio("energy_bar", builder -> builder.rarity(LIGHT_RED).effectImmunities(MobEffects.HUNGER)), // 能量棒 饥饿
            NUTRIENT_SOLUTION = registerCurio("nutrient_solution", builder -> builder.rarity(PINK).effectImmunities(MobEffects.WEAKNESS, MobEffects.HUNGER)), // 营养液
            BLINDFOLD = registerCurio("blindfold", builder -> builder.rarity(LIGHT_RED).effectImmunities(MobEffects.BLINDNESS)), // 蒙眼布 失明
            FLASHLIGHT = registerCurio("flashlight", builder -> builder.rarity(ORANGE).effectImmunities(MobEffects.DARKNESS)), // 手电筒 黑暗
            SEARCHLIGHT = registerCurio("searchlight", builder -> builder.rarity(PINK).effectImmunities(MobEffects.BLINDNESS, MobEffects.DARKNESS)), // 探照灯
            FAST_CLOCK = registerCurio("fast_clock", builder -> builder.rarity(LIGHT_RED).effectImmunities(MobEffects.MOVEMENT_SLOWDOWN)), // 快走时钟 缓慢
            TRIFOLD_MAP = registerCurio("trifold_map", builder -> builder.rarity(LIGHT_RED).effectImmunities(MobEffects.CONFUSION)), // 三折地图 反胃
            THE_PLAN = registerCurio("the_plan", builder -> builder.rarity(PINK).effectImmunities(MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION)), // 计划书
            HAND_DRILL = registerCurio("hand_drill", builder -> builder.rarity(LIGHT_RED).effectImmunities(MobEffects.DIG_SLOWDOWN)), // 手钻 挖掘疲劳
            SHOT_PUT = registerCurio("shot_put", builder -> builder.rarity(GREEN).effectImmunities(MobEffects.LEVITATION)), // 铅球 漂浮
            EXPLORERS_EQUIPMENT = registerCurio("explorers_equipment", builder -> builder.rarity(PINK).effectImmunities(MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION)), // 探险家宝具
            ANKH_CHARM = registerCurio("ankh_charm", AnkhCharm::new), // 十字章护身符
            ANKH_SHIELD = registerCurio("ankh_shield", AnkhShield::new), // 十字章护盾
            STAR_CLOAK = registerCurio("star_cloak", builder -> builder.rarity(LIGHT_RED).accessories(units(STAR$CLOCK))), // 星星斗篷
            STAR_VEIL = registerCurio("star_veil", builder -> builder.rarity(LIGHT_PURPLE).accessories(units(STAR$CLOCK), of(INVULNERABLE$TICKS$MULTIPLIER, new FloatValue(2.0F)))), // 星星面纱
            BEE_CLOAK = registerCurio("bee_cloak", builder -> builder.rarity(LIGHT_RED).accessories(units(STAR$CLOCK, HONEY$COMB), of(INVULNERABLE$TICKS$MULTIPLIER, new FloatValue(2.0F)))), // 蜜蜂斗篷
            BLACK_BELT = registerCurio("black_belt", builder -> builder.rarity(LIME).attribute(TCAttributes.getDodgeChance(), "dodge", 0.1, ADD_VALUE)), // 黑腰带
            /* 天界徽章 */
            /* 月光护身符 */
            SUN_STONE = registerCurio("sun_stone", SunStone::new), // 太阳石
            MOON_STONE = registerCurio("moon_stone", MoonStone::new), // 月亮石
            CELESTIAL_STONE = registerCurio("celestial_stone", CelestialStone::new), // 天界石
            /* 月亮贝壳 */
            /* 天界贝壳 */
            COBALT_SHIELD = registerCurio("cobalt_shield", builder -> builder.rarity(GREEN).attribute(Attributes.KNOCKBACK_RESISTANCE, "knockback_resistance", 1.0, ADD_VALUE).attribute(Attributes.ARMOR, "armor", 1.0, ADD_VALUE)), // 钴护盾
            CROSS_NECKLACE = registerCurio("cross_necklace", builder -> builder.rarity(LIGHT_RED).accessories(of(INVULNERABLE$TICKS$MULTIPLIER, new FloatValue(2.0F)))), // 十字项链
            RANGER_EMBLEM = registerCurio("ranger_emblem", builder -> builder.rarity(LIGHT_RED).noTooltip().attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.15, ADD_MULTIPLIED_TOTAL)), // 游侠徽章
            WARRIOR_EMBLEM = registerCurio("warrior_emblem", builder -> builder.rarity(LIGHT_RED).noTooltip().attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.15, ADD_MULTIPLIED_TOTAL)), // 战士徽章
            SORCERER_EMBLEM = registerCurio("sorcerer_emblem", builder -> builder.rarity(LIGHT_RED).noTooltip().attribute(TCAttributes.getMagicDamage(), "magic_damage", 0.15, ADD_MULTIPLIED_TOTAL)), // 巫士徽章
            AVENGER_EMBLEM = registerCurio("avenger_emblem", builder -> builder.rarity(PINK).noTooltip()
                    .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getMagicDamage(), "magic_damage", 0.12, ADD_MULTIPLIED_TOTAL)), // 复仇者勋章
            EYE_OF_THE_GOLEM = registerCurio("eye_of_the_golem", builder -> builder.rarity(LIME).noTooltip().attribute(TCAttributes.getCriticalChance(), "critical_chance", 0.1, ADD_VALUE)), // 石巨人之眼
            DESTROYER_EMBLEM = registerCurio("destroyer_emblem", builder -> builder.rarity(LIME).noTooltip()
                    .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getMagicDamage(), "magic_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getCriticalChance(), "critical_chance", 0.08, ADD_VALUE)), // 毁灭者勋章
            FERAL_CLAWS = registerCurio("feral_claws", builder -> builder.rarity(ORANGE).noTooltip()
                    .accessories(units(AUTO_ATTACK))
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)), // 狂爪手套
            TITAN_GLOVE = registerCurio("titan_glove", builder -> builder.rarity(LIGHT_RED).noTooltip()
                    .accessories(units(AUTO_ATTACK))
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)), // 泰坦手套
            POWER_GLOVE = registerCurio("power_glove", builder -> builder.rarity(PINK).noTooltip()
                    .accessories(units(AUTO_ATTACK))
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)), // 强力手套
            MECHANICAL_GLOVE = registerCurio("mechanical_glove", builder -> builder.rarity(LIGHT_PURPLE).noTooltip()
                    .accessories(units(AUTO_ATTACK))
                    .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)), // 机械手套
            FIRE_GAUNTLET = registerCurio("fire_gauntlet", builder -> builder.rarity(LIME)
                    .accessories(units(AUTO_ATTACK, FIRE$ATTACK))
                    .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)), // 烈火手套
            FLESH_KNUCKLES = registerCurio("flesh_knuckles", builder -> builder.rarity(PINK)
                    .attribute(Attributes.ARMOR, "armor", 8.0, ADD_VALUE)
                    .attribute(TCAttributes.getAggro(), "aggro", 400, ADD_VALUE)), // 血肉指虎
            BERSERKERS_GLOVE = registerCurio("berserkers_glove", builder -> builder.rarity(PINK)
                    .accessories(units(AUTO_ATTACK))
                    .attribute(Attributes.ARMOR, "armor", 8.0, ADD_VALUE)
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getAggro(), "aggro", 400, ADD_VALUE)), // 狂战士手套
            PALADINS_SHIELD = registerCurio("paladins_shield", () -> new PaladinsShield(BaseCurioItem.builder("paladins_shield").rarity(ModRarity.YELLOW)
                    .attribute(Attributes.ARMOR, "armor", 6.0, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(Attributes.KNOCKBACK_RESISTANCE, "knockback_resistance", 1.0, AttributeModifier.Operation.ADD_VALUE))), // 圣骑士护盾
            HERO_SHIELD = registerCurio("hero_shield", () -> new PaladinsShield(BaseCurioItem.builder("hero_shield").rarity(ModRarity.PINK)
                    .attribute(Attributes.ARMOR, "armor", 10.0, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(Attributes.KNOCKBACK_RESISTANCE, "knockback_resistance", 1.0, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(TCAttributes.getAggro(), "aggro", 400, AttributeModifier.Operation.ADD_VALUE))), // 英雄护盾
            FROZEN_TURTLE_SHELL = registerCurio("frozen_turtle_shell", builder -> builder.rarity(PINK).accessories(units(FROZEN$TURTLE$SHELL))), // 冰冻海龟壳
            FROZEN_SHIELD = registerCurio("frozen_shield", () -> new PaladinsShield(BaseCurioItem.builder("frozen_shield").rarity(PINK)
                    .accessories(units(FROZEN$TURTLE$SHELL)).attribute(Attributes.ARMOR, "armor", 6.0, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(Attributes.KNOCKBACK_RESISTANCE, "knockback_resistance", 1.0, AttributeModifier.Operation.ADD_VALUE))), // 冰冻护盾
            HONEY_COMB = registerCurio("honey_comb", builder -> builder.rarity(GREEN).accessories(units(HONEY$COMB))), // 蜂窝
            SHARK_TOOTH_NECKLACE = registerCurio("shark_tooth_necklace", builder -> builder.attribute(TCAttributes.getArmorPass(), "armor_pass", 5.0, ADD_VALUE)), // 鲨牙项链
            STINGER_NECKLACE = registerCurio("stinger_necklace", builder -> builder.rarity(PINK).accessories(units(HONEY$COMB)).attribute(TCAttributes.getArmorPass(), "armor_pass", 5.0, ADD_VALUE)), // 毒刺项链
            PANIC_NECKLACE = registerCurio("panic_necklace", () -> new PanicNecklace(BaseCurioItem.builder("panic_necklace"))), // 恐慌项链
            SWEETHEART_NECKLACE = registerCurio("sweetheart_necklace", () -> new PaladinsShield(BaseCurioItem.builder("sweetheart_necklace").rarity(ORANGE).accessories(units(HONEY$COMB)))), // 甜心项链
            MAGIC_QUIVER = registerCurio("magic_quiver", builder -> builder.rarity(LIGHT_RED).accessories(units(MAGIC$QUIVER))
                    .attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedVelocity(), "ranged_velocity", 0.2, ADD_MULTIPLIED_TOTAL)), // 魔法箭袋
            MOLTEN_QUIVER = registerCurio("molten_quiver", builder -> builder.rarity(PINK).accessories(units(MAGIC$QUIVER, IGNITE$ARROW))
                    .attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedVelocity(), "ranged_velocity", 0.2, ADD_MULTIPLIED_TOTAL)), // 熔火箭袋
            STALKERS_QUIVER = registerCurio("stalkers_quiver", builder -> builder.rarity(PINK).accessories(units(MAGIC$QUIVER))
                    .attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedVelocity(), "ranged_velocity", 0.2, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getAggro(), "aggro", -400, ADD_VALUE)), // 潜行者箭袋
            RIFLE_SCOPE = registerCurio("rifle_scope", builder -> builder.rarity(LIGHT_RED).accessories(units(SCOPE))), // 步枪瞄准镜
            SNIPER_SCOPE = registerCurio("sniper_scope", builder -> builder.rarity(LIME).accessories(units(SCOPE))
                    .attribute(TCAttributes.getCriticalChance(), "critical_chance", 0.1, ADD_VALUE)
                    .attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.1, ADD_MULTIPLIED_TOTAL)), // 狙击镜
            RECON_SCOPE = registerCurio("recon_scope", builder -> builder.rarity(PINK).accessories(units(SCOPE))
                    .attribute(TCAttributes.getCriticalChance(), "critical_chance", 0.1, ADD_VALUE)
                    .attribute(TCAttributes.getRangedDamage(), "ranged_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getAggro(), "aggro", -400, ADD_VALUE)), // 侦察镜
            MAGMA_STONE = registerCurio("magma_stone", builder -> builder.rarity(ORANGE).accessories(units(FIRE$ATTACK))); // 岩浆石
//            OBSIDIAN_ROSE = registerCurio("obsidian_rose", ObsidianRose::new), // 黑曜石玫瑰
//            OBSIDIAN_SHIELD = registerCurio("obsidian_shield", ObsidianShield::new), // 黑曜石护盾
//            OBSIDIAN_SKULL = registerCurio("obsidian_skull", ObsidianSkull::new), // 黑曜石骷髅头
//            MOLTEN_SKULL_ROSE = registerCurio("molten_skull_rose", MoltenSkullRose::new), // 熔火骷髅头玫瑰
//            OBSIDIAN_SKULL_ROSE = registerCurio("obsidian_skull_rose", ObsidianSkullRose::new), // 黑曜石骷髅头玫瑰
//            HAND_WARMER = registerCurio("hand_warmer", HandWarmer::new), // 暖手宝
//            PUTRID_SCENT = registerCurio("putrid_scent", PutridScent::new), // 腐香囊
//            SHACKLE = registerCurio("shackle", Shackle::new); // 脚镣

    public static final Supplier<BaseCurioItem> STEP_STOOL = registerCurio("step_stool", builder -> builder.accessories(units(STEP$STOOL))), // 梯凳
    /* 飞毯 */
//    AGLET("aglet", Aglet::new), // 金属带扣
//    ANKLET_OF_THE_WIND("anklet_of_the_wind", AnkletOfTheWind::new), // 疾风脚镯
//    MAGILUMINESCENCE("magiluminescence", Magiluminescence::new), // 魔光护符
//    LAVA_CHARM("lava_charm", LavaCharm::new), // 熔岩护身符
//    MAGMA_SKULL("magma_skull", MagmaSkull::new), // 岩浆骷髅头
//    MOLTEN_CHARM("molten_charm", MoltenCharm::new), // 熔火护身符
//    CLIMBING_CLAWS("climbing_claws", ClimbingClaws::new), // 攀爬爪
//    SHOE_SPIKES("shoe_spikes", ShoeSpikes::new), // 鞋钉
//    TIGER_CLIMBING_GEAR("tiger_climbing_gear", TigerClimbingGear::new), // 猛虎攀爬装备
//    TABI("tabi", Tabi::new), // 分趾厚底袜
//    MASTER_NINJA_GEAR("master_ninja_gear", MasterNinjaGear::new), // 忍者大师装备
//    ICE_SKATES("ice_skates", IceSkates::new), // 溜冰鞋
//    HERMES_BOOTS("hermes_boots", HermesBoots::new), // 赫尔墨斯靴
//    FLURRY_BOOTS("flurry_boots", FlurryBoots::new), // 疾风雪靴
//    SAILFISH_BOOTS("sailfish_boots", SailfishBoots::new), // 旗鱼靴
//    DUNERIDER_BOOTS("dunerider_boots", DuneriderBoots::new), // 沙丘行者靴
//    ROCKET_BOOTS("rocket_boots", RocketBoots::new), // 火箭靴
//    SPECTRE_BOOTS("spectre_boots", SpectreBoots::new), // 幽灵靴
//    FAIRY_BOOTS("fairy_boots", FairyBoots::new), // 仙灵靴
//    LIGHTNING_BOOTS("lightning_boots", LightningBoots::new), // 闪电靴
//    FROSTSPARK_BOOTS("frostspark_boots", FrostsparkBoots::new), // 霜花靴
//    WATER_WALKING_BOOTS("water_walking_boots", WaterWalkingBoots::new), // 水上漂靴
//    OBSIDIAN_WATER_WALKING_BOOTS("obsidian_water_walking_boots", ObsidianWaterWalkingBoots::new), // 黑曜石水上漂靴
//    LAVA_WADERS("lava_waders", LavaWaders::new), // 熔岩靴
//    TERRASPARK_BOOTS("terraspark_boots", TerrasparkBoots::new), // 泰拉闪耀靴
//    CLOUD_IN_A_BOTTLE("cloud_in_a_bottle", CloudInABottle::new), // 云朵瓶
//    BLIZZARD_IN_A_BOTTLE("blizzard_in_a_bottle", BlizzardInABottle::new), // 暴雪瓶
//    SANDSTORM_IN_A_BOTTLE("sandstorm_in_a_bottle", SandstormInABottle::new), // 沙暴瓶
//    FART_IN_A_JAR("fart_in_a_jar", FartInAJar::new), // 罐中臭屁
//    TSUNAMI_IN_A_BOTTLE("tsunami_in_a_bottle", TsunamiInABottle::new), // 海啸瓶
//    SHINY_RED_BALLOON("shiny_red_balloon", ShinyRedBalloon::new), // 闪亮红气球
//    BALLOON_PUFFERFISH("balloon_pufferfish", BallonPuffefish::new), // 气球河豚鱼
//    CLOUD_IN_A_BALLOON("cloud_in_a_balloon", CloudInABalloon::new), // 云朵气球
//    BLIZZARD_IN_A_BALLOON("blizzard_in_a_balloon", BlizzardInABalloon::new), // 暴雪气球
//    SANDSTORM_IN_A_BALLOON("sandstorm_in_a_balloon", SandstormInABalloon::new), // 沙暴气球
//    FART_IN_A_BALLOON("fart_in_a_balloon", FartInABalloon::new), // 臭屁气球
//    SHARKRON_BALLOON("sharkron_balloon", SharkronBalloon::new), // 鲨鱼龙气球
//    HONEY_BALLOON("honey_balloon", HoneyBalloon::new), // 蜂蜜气球
//    BUNDLE_OF_BALLOONS("bundle_of_balloons", BundleOfBalloons::new), // 气球束
            LUCKY_HORSESHOE = registerCurio("lucky_horseshoe", builder -> builder
                    .attribute(Attributes.LUCK, "luck", 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, "fall_damage_multiplier", -100.0, ADD_VALUE)); // 幸运马掌
//    OBSIDIAN_HORSESHOE("obsidian_horseshoe", ObsidianHorseshoe::new), // 黑曜石马掌
//    WHITE_HORSESHOE_BALLOON("white_horseshoe_balloon", WhiteHorseshoeBalloon::new), // 白马掌气球
//    BLUE_HORSESHOE_BALLOON("blue_horseshoe_balloon", BlueHorseshoeBalloon::new), // 蓝马掌气球
//    YELLOW_HORSESHOE_BALLOON("yellow_horseshoe_balloon", YellowHorseshoeBalloon::new), // 黄马掌气球
//    GREEN_HORSESHOE_BALLOON("green_horseshoe_balloon", GreenHorseshoeBalloon::new), // 绿马掌气球
//    PINK_HORSESHOE_BALLOON("pink_horseshoe_balloon", PinkHorseshoeBalloon::new), // 粉马掌气球
//    AMBER_HORSESHOE_BALLOON("amber_horseshoe_balloon", AmberHorseshoeBalloon::new), // 琥珀马掌气球
//    BUNDLE_OF_HORSESHOE_BALLOONS("bundle_of_horseshoe_balloons", BundleOfHorseshoeBalloons::new), // 马掌气球束
    /* 浮游圈 */
//    FLIPPER("flipper", Flipper::new), // 脚蹼
    /* 潜水装备 */
    /* 水母潜水装备 */
    /* 北极潜水装备 */
//    FROG_LEG("frog_leg", FrogLeg::new), // 蛙腿
//    FROG_FLIPPER("frog_flipper", FrogFlipper::new), // 青蛙脚蹼
//    FROG_WEBBING("frog_webbing", FrogWebbing::new), // 青蛙蹼
//    FROG_GEAR("frog_gear", FrogGear::new), // 青蛙装备
//    AMBHIPIAN_BOOTS("ambhipian_boots", AmbhipianBoots::new), // 水陆两用靴

    public static final Supplier<BaseCurioItem> ROYAL_GEL = registerCurio("royal_gel", builder -> builder.rarity(ModRarity.EXPERT).accessories(of(MOB$IGNORE, new EntityTypesValue(EntityType.SLIME)))), // 皇家凝胶
            SHIELD_OF_CTHULHU = registerCurio("shield_of_cthulhu", builder -> builder.rarity(ModRarity.EXPERT).accessories(units(SHIELD$OF$CTHULHU))), // 克苏鲁护盾
            WORM_SCARF = registerCurio("worm_scarf", builder -> builder.rarity(ModRarity.EXPERT).accessories(of(INJURY$FREE, new FloatValue(0.17F)))), // 蠕虫围巾
            BRAIN_OF_CONFUSION = registerCurio("brain_of_confusion", builder -> builder.rarity(ModRarity.EXPERT).accessories(units(BRAIN$OF$CONFUSION))), // 混乱之脑
            HIVE_PACK = registerCurio("hive_pack", builder -> builder.rarity(ModRarity.EXPERT).accessories(units(HIVE$PACK))), // 蜂巢背包
            /* 骨头手套 */
            /* 骸骨头盔 */
            /* 挥发明胶 */
            /* 孢子囊 */
            /* 闪亮石 */
            /* 翱翔徽章 */
            GRAVITY_GLOBE = registerCurio("gravity_globe", builder -> builder.rarity(ModRarity.EXPERT).accessories(units(GRAVITY$GLOBE))); // 重力球

    public static Supplier<BaseCurioItem> registerCurio(String name, Consumer<BaseCurioItem.Builder> consumer) {
        return ITEMS.register(name, () -> {
            BaseCurioItem.Builder builder = BaseCurioItem.builder(name);
            consumer.accept(builder);
            return builder.build();
        });
    }

    public static Supplier<BaseCurioItem> registerCurio(String name, Supplier<BaseCurioItem> supplier) {
        return ITEMS.register(name, supplier);
    }
}
