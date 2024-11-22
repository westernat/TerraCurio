package org.confluence.terra_curio.common.init;

import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.EntityTypesValue;
import org.confluence.terra_curio.api.primitive.MayFlyAbilityValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.item.*;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.common.item.curio.RequiresModLoadedCurioItem;
import org.confluence.terra_curio.common.item.curio.combat.*;
import org.confluence.terra_curio.common.item.curio.health.BandOfRegeneration;
import org.confluence.terra_curio.common.item.curio.master.BasePoint;
import org.confluence.terra_curio.common.item.curio.master.Everlasting;
import org.confluence.terra_curio.common.item.curio.movement.BaseSpeedBoots;
import org.confluence.terra_curio.common.item.curio.movement.DuneriderBoots;
import org.confluence.terra_curio.common.item.curio.movement.StepStool;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static org.confluence.terra_curio.common.component.AccessoriesComponent.*;
import static org.confluence.terra_curio.common.component.ModRarity.*;

@SuppressWarnings("all")
public final class TCItems { // todo 全换成data map
    public static final DeferredRegister.Items OTHERS = DeferredRegister.createItems(TerraCurio.MODID);
    public static final DeferredRegister.Items CURIOS = DeferredRegister.createItems(TerraCurio.MODID);

    public static final Supplier<MasterItem> STAR = OTHERS.register("star", MasterItem::new);
    public static final Supplier<MasterItem> ICON = OTHERS.register("icon", MasterItem::new);
    public static final Supplier<BasePoint> BASE_POINT = OTHERS.register("base_point", BasePoint::new);
    public static final Supplier<Everlasting> EVERLASTING = OTHERS.register("everlasting", Everlasting::new);
    public static final Supplier<BaseCurioItem> MECHANICAL_LENS = OTHERS.register("mechanical_lens", () -> BaseCurioItem.builder("mechanical_lens").rarity(ORANGE).accessories(units(ValueType.MECHANICAL$LENS)).build()); //机械晶状体

    public static final Supplier<BlockItem> WORKSHOP = OTHERS.register("workshop", () -> new BlockItem(TCBlocks.WORKSHOP.get(), new Item.Properties()));
    public static final Supplier<DemonHeart> DEMON_HEART = OTHERS.register("demon_heart", DemonHeart::new);
    public static final Supplier<MagicMirror> MAGIC_MIRROR = OTHERS.register("magic_mirror", () -> new MagicMirror(BLUE));
    public static final Supplier<CellPhone> CELL_PHONE = OTHERS.register("cell_phone", CellPhone::new);
    public static final Supplier<DivingHelmet> DIVING_HELMET = OTHERS.register("diving_helmet", DivingHelmet::new);

    public static final Supplier<BaseCurioItem> BEZOAR = registerCurio("bezoar", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.POISON)))), // 牛黄 中毒
            HOLY_WATER = registerCurio("holy_water", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.WITHER)))), // 圣水 凋零
            DETOXIFICATION_CAPSULE = registerCurio("detoxification_capsule", builder -> builder.rarity(PINK).jeiInfos(0).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.POISON, MobEffects.WITHER)))), // 解毒囊
            VITAMINS = registerCurio("vitamins", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.WEAKNESS)))), // 维生素 虚弱
            ENERGY_BAR = registerCurio("energy_bar", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.HUNGER)))), // 能量棒 饥饿
            NUTRIENT_SOLUTION = registerCurio("nutrient_solution", builder -> builder.rarity(PINK).jeiInfos(0).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.WEAKNESS, MobEffects.HUNGER)))), // 营养液
            BLINDFOLD = registerCurio("blindfold", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.BLINDNESS)))), // 蒙眼布 失明
            FLASHLIGHT = registerCurio("flashlight", builder -> builder.rarity(ORANGE).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.DARKNESS)))), // 手电筒 黑暗
            SEARCHLIGHT = registerCurio("searchlight", builder -> builder.rarity(PINK).jeiInfos(0).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.BLINDNESS, MobEffects.DARKNESS)))), // 探照灯
            FAST_CLOCK = registerCurio("fast_clock", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.MOVEMENT_SLOWDOWN)))), // 快走时钟 缓慢
            TRIFOLD_MAP = registerCurio("trifold_map", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.CONFUSION)))), // 三折地图 反胃
            THE_PLAN = registerCurio("the_plan", builder -> builder.rarity(PINK).jeiInfos(0).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION)))), // 计划书
            HAND_DRILL = registerCurio("hand_drill", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.DIG_SLOWDOWN)))), // 手钻 挖掘疲劳
            SHOT_PUT = registerCurio("shot_put", builder -> builder.rarity(GREEN).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.LEVITATION)))), // 铅球 漂浮
            EXPLORERS_EQUIPMENT = registerCurio("explorers_equipment", builder -> builder.jeiInfos(0).rarity(PINK).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION)))), // 探险家宝具
            ANKH_CHARM = registerCurio("ankh_charm", builder -> builder.jeiInfos(0).rarity(LIGHT_PURPLE).accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(
                    MobEffects.POISON, MobEffects.WITHER,
                    MobEffects.WEAKNESS, MobEffects.HUNGER,
                    MobEffects.BLINDNESS, MobEffects.DARKNESS,
                    MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION,
                    MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION)))), // 十字章护身符
            ANKH_SHIELD = registerCurio("ankh_shield", builder -> builder.jeiInfos(0).tooltips(1).rarity(LIME)
                    .accessories(of(ValueType.EFFECT$IMMUNITIES, Set.of(MobEffects.POISON, MobEffects.WITHER, MobEffects.WEAKNESS, MobEffects.HUNGER, MobEffects.BLINDNESS, MobEffects.DARKNESS, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION, MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION)))
                    .attribute(Attributes.KNOCKBACK_RESISTANCE, 1.0, ADD_VALUE)
                    .attribute(Attributes.ARMOR, 4.0, ADD_VALUE)), // 十字章护盾
            STAR_CLOAK = registerCurio("star_cloak", builder -> builder.jeiInfos(0).rarity(LIGHT_RED).accessories(of(ValueType.STAR$CLOCK, false))), // 星星斗篷
            STAR_VEIL = registerCurio("star_veil", builder -> builder.rarity(LIGHT_PURPLE).jeiInfos(0).accessories(of(ValueType.STAR$CLOCK, false), of(ValueType.INVULNERABLE$TICKS$MULTIPLIER, 2.0F)).tooltips(1)), // 星星面纱
            BEE_CLOAK = registerCurio("bee_cloak", builder -> builder.jeiInfos(0).rarity(LIGHT_RED).accessories(units(ValueType.HONEY$COMB), of(ValueType.STAR$CLOCK, false), of(ValueType.INVULNERABLE$TICKS$MULTIPLIER, 2.0F)).tooltips(1)), // 蜜蜂斗篷
            BLACK_BELT = registerCurio("black_belt", builder -> builder.rarity(LIME).attribute(TCAttributes.getDodgeChance(), 0.1, ADD_VALUE)), // 黑腰带
            /* 月光护身符 */
            SUN_STONE = registerCurio("sun_stone", SunStone::new), // 太阳石
            MOON_STONE = registerCurio("moon_stone", MoonStone::new), // 月亮石
            CELESTIAL_STONE = registerCurio("celestial_stone", CelestialStone::new), // 天界石
            /* 月亮贝壳 */
            /* 天界贝壳 */
            COBALT_SHIELD = registerCurio("cobalt_shield", builder -> builder.rarity(GREEN).attribute(Attributes.KNOCKBACK_RESISTANCE, 1.0, ADD_VALUE)), // 钴护盾
            CROSS_NECKLACE = registerCurio("cross_necklace", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.INVULNERABLE$TICKS$MULTIPLIER, 2.0F))), // 十字项链
            RANGER_EMBLEM = registerCurio("ranger_emblem", builder -> builder.rarity(LIGHT_RED).noTooltip().attribute(TCAttributes.getRangedDamage(), 0.15, ADD_MULTIPLIED_TOTAL)), // 游侠徽章
            WARRIOR_EMBLEM = registerCurio("warrior_emblem", builder -> builder.rarity(LIGHT_RED).noTooltip().attribute(Attributes.ATTACK_DAMAGE, 0.15, ADD_MULTIPLIED_TOTAL)), // 战士徽章
            SORCERER_EMBLEM = registerCurio("sorcerer_emblem", builder -> builder.rarity(LIGHT_RED).noTooltip().attribute(TCAttributes.getMagicDamage(), 0.15, ADD_MULTIPLIED_TOTAL)), // 巫士徽章
            AVENGER_EMBLEM = registerCurio("avenger_emblem", builder -> builder.jeiInfos(0).rarity(PINK).noTooltip()
                    .attribute(Attributes.ATTACK_DAMAGE, 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedDamage(), 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getMagicDamage(), 0.12, ADD_MULTIPLIED_TOTAL)), // 复仇者勋章
            EYE_OF_THE_GOLEM = registerCurio("eye_of_the_golem", builder -> builder.rarity(LIME).noTooltip().attribute(TCAttributes.getCriticalChance(), 0.1, ADD_VALUE)), // 石巨人之眼
            DESTROYER_EMBLEM = registerCurio("destroyer_emblem", builder -> builder.jeiInfos(0).rarity(LIME).noTooltip()
                    .attribute(Attributes.ATTACK_DAMAGE, 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedDamage(), 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getMagicDamage(), 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getCriticalChance(), 0.08, ADD_VALUE)), // 毁灭者勋章
            FERAL_CLAWS = registerCurio("feral_claws", builder -> builder.rarity(ORANGE)
                    .accessories(units(ValueType.AUTO$ATTACK))
                    .attribute(Attributes.ATTACK_SPEED, 0.12, ADD_MULTIPLIED_TOTAL)), // 狂爪手套
            TITAN_GLOVE = registerCurio("titan_glove", builder -> builder.rarity(LIGHT_RED).noTooltip()
                    .accessories(units(ValueType.AUTO$ATTACK))
                    .attribute(Attributes.ATTACK_KNOCKBACK, 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, 0.1, ADD_MULTIPLIED_TOTAL)), // 泰坦手套
            POWER_GLOVE = registerCurio("power_glove", builder -> builder.jeiInfos(0).rarity(PINK)
                    .accessories(units(ValueType.AUTO$ATTACK))
                    .attribute(Attributes.ATTACK_SPEED, 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, 0.1, ADD_MULTIPLIED_TOTAL)), // 强力手套
            MECHANICAL_GLOVE = registerCurio("mechanical_glove", builder -> builder.jeiInfos(0).rarity(LIGHT_PURPLE)
                    .accessories(units(ValueType.AUTO$ATTACK))
                    .attribute(Attributes.ATTACK_DAMAGE, 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_SPEED, 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, 0.1, ADD_MULTIPLIED_TOTAL)), // 机械手套
            FIRE_GAUNTLET = registerCurio("fire_gauntlet", builder -> builder.jeiInfos(0).rarity(LIME).tooltips(1)
                    .accessories(units(ValueType.AUTO$ATTACK, ValueType.FIRE$ATTACK))
                    .attribute(Attributes.ATTACK_DAMAGE, 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_SPEED, 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, 0.1, ADD_MULTIPLIED_TOTAL)), // 烈火手套
            FLESH_KNUCKLES = registerCurio("flesh_knuckles", builder -> builder.rarity(PINK)
                    .attribute(Attributes.ARMOR, 8.0, ADD_VALUE)
                    .attribute(TCAttributes.AGGRO, 400, ADD_VALUE)), // 血肉指虎
            BERSERKERS_GLOVE = registerCurio("berserkers_glove", builder -> builder.jeiInfos(0).noTooltip().rarity(PINK)
                    .accessories(units(ValueType.AUTO$ATTACK))
                    .attribute(Attributes.ARMOR, 8.0, ADD_VALUE)
                    .attribute(Attributes.ATTACK_SPEED, 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.AGGRO, 400, ADD_VALUE)), // 狂战士手套
            PALADINS_SHIELD = registerDirectly("paladins_shield", name -> new PaladinsShield(BaseCurioItem.builder(name).rarity(ModRarity.YELLOW)
                    .attribute(Attributes.ARMOR, 6.0, ADD_VALUE)
                    .attribute(Attributes.KNOCKBACK_RESISTANCE, 1.0, ADD_VALUE))), // 圣骑士护盾
            HERO_SHIELD = registerDirectly("hero_shield", name -> new PaladinsShield(BaseCurioItem.builder(name).jeiInfos(0).rarity(ModRarity.PINK)
                    .attribute(Attributes.ARMOR, 10.0, ADD_VALUE)
                    .attribute(Attributes.KNOCKBACK_RESISTANCE, 1.0, ADD_VALUE)
                    .attribute(TCAttributes.AGGRO, 400, ADD_VALUE))), // 英雄护盾
            FROZEN_TURTLE_SHELL = registerCurio("frozen_turtle_shell", builder -> builder.rarity(PINK).accessories(units(ValueType.FROZEN$TURTLE$SHELL))), // 冰冻海龟壳
            FROZEN_SHIELD = registerDirectly("frozen_shield", name -> new PaladinsShield(BaseCurioItem.builder(name).jeiInfos(0).rarity(PINK).tooltips(1)
                    .accessories(units(ValueType.FROZEN$TURTLE$SHELL)).attribute(Attributes.ARMOR, 6.0, ADD_VALUE)
                    .attribute(Attributes.KNOCKBACK_RESISTANCE, 1.0, ADD_VALUE))), // 冰冻护盾
            HONEY_COMB = registerCurio("honey_comb", builder -> builder.rarity(GREEN).accessories(units(ValueType.HONEY$COMB))), // 蜂窝
            SHARK_TOOTH_NECKLACE = registerCurio("shark_tooth_necklace", builder -> builder.noTooltip().attribute(TCAttributes.getArmorPass(), 5.0, ADD_VALUE)), // 鲨牙项链
            STINGER_NECKLACE = registerCurio("stinger_necklace", builder -> builder.rarity(PINK).jeiInfos(0).accessories(units(ValueType.HONEY$COMB)).attribute(TCAttributes.getArmorPass(), 5.0, ADD_VALUE)), // 毒刺项链
            PANIC_NECKLACE = registerDirectly("panic_necklace", name -> new PanicNecklace(BaseCurioItem.builder(name))), // 恐慌项链
            SWEETHEART_NECKLACE = registerDirectly("sweetheart_necklace", name -> new PaladinsShield(BaseCurioItem.builder(name).jeiInfos(0).rarity(ORANGE).accessories(units(ValueType.HONEY$COMB)))), // 甜心项链
            MAGIC_QUIVER = registerCurio("magic_quiver", builder -> builder.rarity(LIGHT_RED).accessories(units(ValueType.MAGIC$QUIVER))
                    .attribute(TCAttributes.getRangedDamage(), 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedVelocity(), 0.2, ADD_MULTIPLIED_TOTAL)), // 魔法箭袋
            MOLTEN_QUIVER = registerCurio("molten_quiver", builder -> builder.rarity(PINK).jeiInfos(0).accessories(units(ValueType.MAGIC$QUIVER, ValueType.IGNITE$ARROW)).tooltips(2)
                    .attribute(TCAttributes.getRangedDamage(), 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedVelocity(), 0.2, ADD_MULTIPLIED_TOTAL)), // 熔火箭袋
            STALKERS_QUIVER = registerCurio("stalkers_quiver", builder -> builder.rarity(PINK).jeiInfos(0).accessories(units(ValueType.MAGIC$QUIVER))
                    .tooltips(1)
                    .attribute(TCAttributes.getRangedDamage(), 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getRangedVelocity(), 0.2, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.AGGRO, -400, ADD_VALUE)), // 潜行者箭袋
            RIFLE_SCOPE = registerCurio("rifle_scope", builder -> builder.rarity(LIGHT_RED).accessories(units(ValueType.SCOPE)).tooltips(1)), // 步枪瞄准镜
            SNIPER_SCOPE = registerCurio("sniper_scope", builder -> builder.rarity(LIME).jeiInfos(0).accessories(units(ValueType.SCOPE)).tooltips(1)
                    .attribute(TCAttributes.getCriticalChance(), 0.1, ADD_VALUE)
                    .attribute(TCAttributes.getRangedDamage(), 0.1, ADD_MULTIPLIED_TOTAL)), // 狙击镜
            RECON_SCOPE = registerCurio("recon_scope", builder -> builder.rarity(PINK).jeiInfos(0).accessories(units(ValueType.SCOPE)).tooltips(3)
                    .attribute(TCAttributes.getCriticalChance(), 0.1, ADD_VALUE)
                    .attribute(TCAttributes.getRangedDamage(), 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.AGGRO, -400, ADD_VALUE)), // 侦察镜
            MAGMA_STONE = registerCurio("magma_stone", ORANGE), // 岩浆石
            OBSIDIAN_ROSE = registerCurio("obsidian_rose", builder -> builder.rarity(ORANGE).accessories(of(ValueType.LAVA$HURT$REDUCE, 0.5F))), // 黑曜石玫瑰
            OBSIDIAN_SHIELD = registerCurio("obsidian_shield", builder -> builder.jeiInfos(0).rarity(LIGHT_RED).accessories(units(ValueType.FIRE$IMMUNE))
                    .attribute(Attributes.KNOCKBACK_RESISTANCE, 1.0, ADD_VALUE)
                    .attribute(Attributes.ARMOR, 2.0, ADD_VALUE)), // 黑曜石护盾
            OBSIDIAN_SKULL = registerCurio("obsidian_skull", builder -> builder.jeiInfos(0).rarity(GREEN).accessories(units(ValueType.FIRE$IMMUNE))), // 黑曜石骷髅头
            MOLTEN_SKULL_ROSE = registerCurio("molten_skull_rose", builder -> builder.rarity(LIGHT_PURPLE).jeiInfos(0).tooltips(2)
                    .accessories(units(ValueType.FIRE$IMMUNE), of(ValueType.LAVA$IMMUNE$TICKS, 140), of(ValueType.LAVA$HURT$REDUCE, 0.5F))), // 熔火骷髅头玫瑰
            OBSIDIAN_SKULL_ROSE = registerCurio("obsidian_skull_rose", builder -> builder.jeiInfos(0).rarity(PINK).accessories(units(ValueType.FIRE$IMMUNE), of(ValueType.LAVA$HURT$REDUCE, 0.5F)).tooltips(1)), // 黑曜石骷髅头玫瑰
            HAND_WARMER = registerCurio("hand_warmer", builder -> builder.jeiInfos(0).rarity(GREEN).accessories(units(ValueType.FROZEN$IMMUNE))), // 暖手宝
            PUTRID_SCENT = registerCurio("putrid_scent", builder -> builder.rarity(LIGHT_PURPLE)
                    .attribute(Attributes.ATTACK_DAMAGE, 0.05, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.getCriticalChance(), 0.05, ADD_VALUE)
                    .attribute(TCAttributes.AGGRO, -400, ADD_VALUE)), // 腐香囊
            SHACKLE = registerCurio("shackle",builder -> builder.rarity(BLUE).noTooltip()); // 脚镣

    public static final Supplier<BaseCurioItem> TOOLBELT = registerCurio("toolbelt", builder -> builder.noTooltip().rarity(ORANGE).attribute(Attributes.BLOCK_INTERACTION_RANGE, 1.0, ADD_VALUE)), // 工具腰带
            TOOLBOX = registerCurio("toolbox", builder -> builder.noTooltip().rarity(GREEN).attribute(Attributes.BLOCK_INTERACTION_RANGE, 1.0, ADD_VALUE)), // 工具箱
            EXTENDO_GRIP = registerCurio("extendo_grip", builder -> builder.noTooltip().rarity(ORANGE).attribute(Attributes.BLOCK_INTERACTION_RANGE, 3.0, ADD_VALUE)), // 加长握爪
            PORTABLE_CEMENT_MIXER = registerCurio("portable_cement_mixer", builder -> builder.rarity(ORANGE).accessories(of(ValueType.RIGHT$CLICK$DELAY$SUBSTRACTOR, 1))), // 便携式水泥搅拌机
            BRICK_LAYER = registerCurio("brick_layer", builder -> builder.rarity(ORANGE).accessories(of(ValueType.RIGHT$CLICK$DELAY$SUBSTRACTOR, 1))), // 砌砖刀
            ARCHITECT_GIZMO_PACK = registerCurio("architect_gizmo_pack", builder -> builder.jeiInfos(0).rarity(PINK)
                    .accessories(of(ValueType.RIGHT$CLICK$DELAY$SUBSTRACTOR, 2))
                    .attribute(Attributes.BLOCK_INTERACTION_RANGE, 3.0, ADD_VALUE)), // 建筑师发明背包
            ANCIENT_CHISEL = registerCurio("ancient_chisel", builder -> builder.attribute(Attributes.BLOCK_BREAK_SPEED, 0.25, ADD_MULTIPLIED_TOTAL)), // 远古凿子
            HAND_OF_CREATION = registerDirectly("hand_of_creation", name -> new StepStool(BaseCurioItem.builder(name).tooltips(1).jeiInfos(0).rarity(LIGHT_PURPLE)
                    .accessories(of(ValueType.RIGHT$CLICK$DELAY$SUBSTRACTOR, 3))
                    .attribute(Attributes.BLOCK_INTERACTION_RANGE, 3.0, ADD_VALUE)
                    .attribute(Attributes.BLOCK_BREAK_SPEED, 0.25, ADD_MULTIPLIED_TOTAL)
                    .attribute(TCAttributes.PICKUP_RANGE, 6.25, ADD_VALUE))); // 创造之手

    public static final Supplier<BaseCurioItem> BAND_OF_REGENERATION = registerDirectly("band_of_regeneration", name -> new BandOfRegeneration(BaseCurioItem.builder(name)));

    public static final Supplier<BaseCurioItem> COPPER_WATCH = registerCurio("copper_watch", builder -> builder.rarity(WHITE).jeiInfos(0).accessories(units(ValueType.HOUR$WATCH))), // 铜表
            TIN_WATCH = registerCurio("tin_watch", builder -> builder.jeiInfos(0).rarity(WHITE).accessories(units(ValueType.HOUR$WATCH))), // 锡表
            SILVER_WATCH = registerCurio("silver_watch", builder -> builder.jeiInfos(0).rarity(WHITE).accessories(units(ValueType.HALF$HOUR$WATCH))), // 银表
            TUNGSTEN_WATCH = registerCurio("tungsten_watch", builder -> builder.jeiInfos(0).rarity(WHITE).accessories(units(ValueType.HALF$HOUR$WATCH))), // 钨表
            GOLD_WATCH = registerCurio("gold_watch", builder -> builder.makesPiglinsNeutral().jeiInfos(0).accessories(units(ValueType.MINUTE$WATCH))), // 金表
            PLATINUM_WATCH = registerCurio("platinum_watch", builder -> builder.jeiInfos(0).accessories(units(ValueType.MINUTE$WATCH))), // 铂金表
            DEPTH_METER = registerCurio("depth_meter", builder -> builder.accessories(units(ValueType.DEPTH$METER))), // 深度计
            COMPASS = registerCurio("compass", builder -> builder.accessories(units(ValueType.COMPASS))), // 罗盘
            RADAR = registerCurio("radar", builder -> builder.accessories(units(ValueType.RADAR))), // 雷达
            LIFE_FORM_ANALYZER = registerCurio("life_form_analyzer", builder -> builder.accessories(units(ValueType.LIFE$FORM$ANALYZER))), // 生命体分析机
            TALLY_COUNTER = registerCurio("tally_counter", builder -> builder.accessories(units(ValueType.TALLY$COUNTER))), // 杀怪计数器
            METAL_DETECTOR = registerCurio("metal_detector", builder -> builder.accessories(units(ValueType.METAL$DETECTOR))), // 金属探测器
            STOPWATCH = registerCurio("stopwatch", builder -> builder.jeiInfos(0).accessories(units(ValueType.STOPWATCH))), // 秒表
            DPS_METER = registerCurio("dps_meter", builder -> builder.accessories(units(ValueType.DPS$METER))), // 每秒伤害计数器
            FISHERMANS_POCKET_GUIDE = registerCurio("fishermans_pocket_guide", builder -> builder.accessories(units(ValueType.FISHERMANS$POCKET$GUIDE))), // 渔民袖珍宝典
            WEATHER_RADIO = registerCurio("weather_radio", builder -> builder.accessories(units(ValueType.WEATHER$RADIO))), // 天气收音机
            SEXTANT = registerCurio("sextant", builder -> builder.accessories(units(ValueType.SEXTANT))), // 六分仪
            GPS = registerCurio("gps", builder -> builder.rarity(ORANGE).jeiInfos(0).tooltips(2).accessories(units(ValueType.MINUTE$WATCH, ValueType.DEPTH$METER, ValueType.COMPASS))), // 全球定位系统
            REK_3000 = registerCurio("rek_3000", builder -> builder.rarity(ORANGE).jeiInfos(0).tooltips(2).accessories(units(ValueType.RADAR, ValueType.LIFE$FORM$ANALYZER, ValueType.TALLY$COUNTER))), // R.E.K.3000
            GOBLIN_TECH = registerCurio("goblin_tech", builder -> builder.rarity(ORANGE).jeiInfos(0).tooltips(2).accessories(units(ValueType.METAL$DETECTOR, ValueType.STOPWATCH, ValueType.DPS$METER))), // 哥布林数据仪
            FISH_FINDER = registerCurio("fish_finder", builder -> builder.rarity(ORANGE).jeiInfos(0).tooltips(2).accessories(units(ValueType.FISHERMANS$POCKET$GUIDE, ValueType.WEATHER$RADIO, ValueType.SEXTANT))), // 探鱼器
            PDA = registerCurio("pda", builder -> builder.rarity(PINK).jeiInfos(0).tooltips(11).accessories(units(ValueType.FULL$INFORMATION))); // 个人数字助手

    public static final Supplier<BaseCurioItem> STEP_STOOL = registerDirectly("step_stool", name -> new StepStool(BaseCurioItem.builder(name))), // 梯凳
            FLYING_CARPET = registerCurio("flying_carpet", builder -> builder.rarity(GREEN).accessories(of(ValueType.MAY$FLY, new MayFlyAbilityValue.Storage(0.5625F, 100, false, true)))), // 飞毯
            AGLET = registerCurio("aglet", builder -> builder.noTooltip().attribute(Attributes.MOVEMENT_SPEED, 0.05, ADD_MULTIPLIED_TOTAL)), // 金属带扣
            ANKLET_OF_THE_WIND = registerCurio("anklet_of_the_wind", builder -> builder.jeiInfos(0).noTooltip().attribute(Attributes.MOVEMENT_SPEED, 0.1, ADD_MULTIPLIED_TOTAL)), // 疾风脚镯
            MAGILUMINESCENCE = registerCurio("magiluminescence", builder -> builder
                    .accessories(units(ValueType.MAGILUMINESCENCE)).tooltips(1)
                    .attribute(Attributes.MOVEMENT_SPEED, 0.15, ADD_MULTIPLIED_TOTAL)), // 魔光护符
            LAVA_CHARM = registerCurio("lava_charm", builder -> builder.rarity(ORANGE).accessories(of(ValueType.LAVA$IMMUNE$TICKS, 140))), // 熔岩护身符
            MAGMA_SKULL = registerCurio("magma_skull", builder -> builder.jeiInfos(0).tooltips(1).rarity(PINK).accessories(
                    units(ValueType.FIRE$IMMUNE),
                    of(ValueType.LAVA$IMMUNE$TICKS, 140))), // 岩浆骷髅头
            MOLTEN_CHARM = registerCurio("molten_charm", builder -> builder.tooltips(1).jeiInfos(0).rarity(PINK).accessories(
                    units(ValueType.FIRE$IMMUNE),
                    of(ValueType.LAVA$IMMUNE$TICKS, 140))), // 熔火护身符
            CLIMBING_CLAWS = registerCurio("climbing_claws", builder -> builder.tooltips(1).accessories(of(ValueType.WALL$CLIMB, (byte) 1))), // 攀爬爪
            SHOE_SPIKES = registerCurio("shoe_spikes", builder -> builder.tooltips(1).accessories(of(ValueType.WALL$CLIMB, (byte) 1))), // 鞋钉
            TIGER_CLIMBING_GEAR = registerCurio("tiger_climbing_gear", builder -> builder.jeiInfos(0).rarity(GREEN).accessories(of(ValueType.WALL$CLIMB, (byte) 2))), // 猛虎攀爬装备
            TABI = registerCurio("tabi", builder -> builder.rarity(LIME).accessories(units(ValueType.SPRINTING))), // 分趾厚底袜
            MASTER_NINJA_GEAR = registerCurio("master_ninja_gear", builder -> builder.tooltips(2).jeiInfos(0).rarity(YELLOW)
                    .accessories(units(ValueType.SPRINTING), of(ValueType.WALL$CLIMB, (byte) 2))
                    .attribute(TCAttributes.getDodgeChance(), 0.1, ADD_VALUE)), // 忍者大师装备
            ICE_SKATES = registerCurio("ice_skates", BLUE), // 溜冰鞋
            HERMES_BOOTS = registerDirectly("hermes_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name))), // 赫尔墨斯靴
            FLURRY_BOOTS = registerDirectly("flurry_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name).jeiInfos(0))), // 疾风雪靴
            SAILFISH_BOOTS = registerDirectly("sailfish_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name))), // 旗鱼靴
            DUNERIDER_BOOTS = registerDirectly("dunerider_boots", DuneriderBoots::new), // 沙丘行者靴
            ROCKET_BOOTS = registerDirectly("rocket_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name).jeiInfos(0).accessories(of(ValueType.MAY$FLY, new MayFlyAbilityValue.Storage(0.3F, 36, false, false))))), // 火箭靴
            SPECTRE_BOOTS = registerDirectly("spectre_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name).rarity(LIGHT_RED).jeiInfos(0).accessories(of(ValueType.MAY$FLY, new MayFlyAbilityValue.Storage(0.3F, 36, false, false))))), // 幽灵靴
            FAIRY_BOOTS = registerDirectly("fairy_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name).jeiInfos(0).tooltips(1).rarity(PINK).accessories(units(ValueType.FLOWER$BOOTS), of(ValueType.MAY$FLY, new MayFlyAbilityValue.Storage(0.3F, 36, false, false))))), // 仙灵靴
            LIGHTNING_BOOTS = registerDirectly("lightning_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name).jeiInfos(0).rarity(PINK)
                    .accessories(of(ValueType.MAY$FLY, new MayFlyAbilityValue.Storage(0.3F, 36, false, false)))
                    .attribute(Attributes.MOVEMENT_SPEED, 0.08, ADD_MULTIPLIED_TOTAL))), // 闪电靴
            FROSTSPARK_BOOTS = registerDirectly("frostspark_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name).jeiInfos(0).rarity(LIME)
                    .accessories(units(ValueType.ICE$SPEED), of(ValueType.MAY$FLY, new MayFlyAbilityValue.Storage(0.3F, 40, false, false)))
                    .attribute(Attributes.MOVEMENT_SPEED, 0.08, ADD_MULTIPLIED_TOTAL))), // 霜花靴
            WATER_WALKING_BOOTS = registerCurio("water_walking_boots", builder -> builder.rarity(LIGHT_RED).accessories(of(ValueType.FLUID$WALK, Set.of(TCTags.WATER_LIKE_WALK)))), // 水上漂靴
            OBSIDIAN_WATER_WALKING_BOOTS = registerCurio("obsidian_water_walking_boots", builder -> builder.jeiInfos(0).rarity(LIGHT_RED).tooltips(1).accessories(
                    units(ValueType.FIRE$IMMUNE),
                    of(ValueType.FLUID$WALK, Set.of(TCTags.WATER_LIKE_WALK, TCTags.LAVA_LIKE_WALK)))), // 黑曜石水上漂靴
            LAVA_WADERS = registerCurio("lava_waders", builder -> builder.rarity(LIME).tooltips(1).jeiInfos(0).accessories(
                    units(ValueType.FIRE$IMMUNE),
                    of(ValueType.FLUID$WALK, Set.of(TCTags.WATER_LIKE_WALK, TCTags.LAVA_LIKE_WALK)),
                    of(ValueType.LAVA$IMMUNE$TICKS, 140),
                    of(ValueType.LAVA$HURT$REDUCE, 0.5F))), // 熔岩靴
            TERRASPARK_BOOTS = registerDirectly("terraspark_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name).rarity(LIME)
                    .tooltips(3)
                    .jeiInfos(0)
                    .accessories(
                            units(ValueType.ICE$SPEED, ValueType.FIRE$IMMUNE),
                            of(ValueType.MAY$FLY, new MayFlyAbilityValue.Storage(0.3F, 40, false, false)),
                            of(ValueType.FLUID$WALK, Set.of(TCTags.WATER_LIKE_WALK, TCTags.LAVA_LIKE_WALK)),
                            of(ValueType.LAVA$IMMUNE$TICKS, 140),
                            of(ValueType.LAVA$HURT$REDUCE, 0.5F)
                    ).attribute(Attributes.MOVEMENT_SPEED, 0.08, ADD_MULTIPLIED_TOTAL))), // 泰拉闪耀靴
            CLOUD_IN_A_BOTTLE = registerCurio("cloud_in_a_bottle", builder -> builder.accessories(of(ValueType.CLOUD, 1.3F))), // 云朵瓶
            BLIZZARD_IN_A_BOTTLE = registerCurio("blizzard_in_a_bottle", builder -> builder.jeiInfos(0).accessories(of(ValueType.BLIZZARD, new Tuple<>(0.4F, 14)))), // 暴雪瓶
            SANDSTORM_IN_A_BOTTLE = registerCurio("sandstorm_in_a_bottle", builder -> builder.rarity(GREEN).accessories(of(ValueType.SAND$STORM, new Tuple<>(0.45F, 17)))), // 沙暴瓶
            FART_IN_A_JAR = registerCurio("fart_in_a_jar", builder -> builder.rarity(GREEN).accessories(of(ValueType.FART, 1.7F))), // 罐中臭屁
            TSUNAMI_IN_A_BOTTLE = registerCurio("tsunami_in_a_bottle", builder -> builder.accessories(of(ValueType.TSUNAMI, 1.5F))), // 海啸瓶
            SHINY_RED_BALLOON = registerCurio("shiny_red_balloon", builder -> builder.attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)), // 闪亮红气球
            BALLOON_PUFFERFISH = registerCurio("balloon_pufferfish", builder -> builder.jeiInfos(0).attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)), // 气球河豚鱼
            CLOUD_IN_A_BALLOON = registerCurio("cloud_in_a_balloon", builder -> builder.rarity(LIGHT_RED)
                    .tooltips(1)
                    .jeiInfos(0)
                    .accessories(of(ValueType.CLOUD, 1.3F))
                    .attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)), // 云朵气球
            BLIZZARD_IN_A_BALLOON = registerCurio("blizzard_in_a_balloon", builder -> builder.jeiInfos(0).rarity(LIGHT_RED)
                    .tooltips(1)
                    .jeiInfos(0)
                    .accessories(of(ValueType.BLIZZARD, new Tuple<>(0.4F, 14)))
                    .attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)), // 暴雪气球
            SANDSTORM_IN_A_BALLOON = registerCurio("sandstorm_in_a_balloon", builder -> builder.rarity(LIGHT_RED)
                    .tooltips(1)
                    .jeiInfos(0)
                    .accessories(of(ValueType.SAND$STORM, new Tuple<>(0.45F, 17)))
                    .attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)), // 沙暴气球
            FART_IN_A_BALLOON = registerCurio("fart_in_a_balloon", builder -> builder.rarity(LIGHT_RED)
                    .tooltips(1)
                    .jeiInfos(0)
                    .accessories(of(ValueType.FART, 1.1F))
                    .attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)), // 臭屁气球
            SHARKRON_BALLOON = registerCurio("sharkron_balloon", builder -> builder.jeiInfos(0).accessories(of(ValueType.TSUNAMI, 1.3F)).attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)), // 鲨鱼龙气球
            HONEY_BALLOON = registerCurio("honey_balloon", builder -> builder.rarity(GREEN)
                    .tooltips(1)
                    .jeiInfos(0)
                    .accessories(units(ValueType.HONEY$COMB))
                    .attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)), // 蜂蜜气球
            BUNDLE_OF_BALLOONS = registerCurio("bundle_of_balloons", builder -> builder.rarity(YELLOW)
                    .tooltips(1)
                    .jeiInfos(0)
                    .accessories(
                            of(ValueType.SAND$STORM, new Tuple<>(0.45F, 17)),
                            of(ValueType.BLIZZARD, new Tuple<>(0.4F, 14)),
                            of(ValueType.CLOUD, 1.3F)
                    ).attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)), // 气球束
            LUCKY_HORSESHOE = registerCurio("lucky_horseshoe", builder -> builder
                    .tooltips(1)
                    .attribute(Attributes.LUCK, 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -100.0, ADD_VALUE)), // 幸运马掌
            OBSIDIAN_HORSESHOE = registerCurio("obsidian_horseshoe", builder -> builder.jeiInfos(0).rarity(LIGHT_RED)
                    .tooltips(1)
                    .jeiInfos(0)
                    .accessories(units(ValueType.FIRE$IMMUNE))
                    .attribute(Attributes.LUCK, 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -100.0, ADD_VALUE)), // 黑曜石马掌
            WHITE_HORSESHOE_BALLOON = registerCurio("white_horseshoe_balloon", builder -> builder.rarity(LIGHT_RED)
                    .tooltips(2)
                    .jeiInfos(0)
                    .accessories(of(ValueType.BLIZZARD, new Tuple<>(0.4F, 14)))
                    .attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.LUCK, 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -100.0, ADD_VALUE)), // 白马掌气球
            BLUE_HORSESHOE_BALLOON = registerCurio("blue_horseshoe_balloon", builder -> builder.rarity(LIGHT_RED)
                    .tooltips(2)
                    .jeiInfos(0)
                    .accessories(of(ValueType.CLOUD, 1.3F))
                    .attribute(Attributes.JUMP_STRENGTH, 0.75, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.LUCK, 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -100.0, ADD_VALUE)), // 蓝马掌气球
            YELLOW_HORSESHOE_BALLOON = registerCurio("yellow_horseshoe_balloon", builder -> builder.tooltips(2).jeiInfos(0).rarity(LIGHT_RED)
                    .jeiInfos(0).accessories(of(ValueType.SAND$STORM, new Tuple<>(0.45F, 17)))
                    .attribute(Attributes.JUMP_STRENGTH, 0.75, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.LUCK, 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -100.0, ADD_VALUE)), // 黄马掌气球
            GREEN_HORSESHOE_BALLOON = registerCurio("green_horseshoe_balloon", builder -> builder.rarity(LIGHT_RED)
                    .tooltips(2)
                    .jeiInfos(0)
                    .accessories(of(ValueType.FART, 1.1F))
                    .attribute(Attributes.JUMP_STRENGTH, 0.75, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.LUCK, 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -100.0, ADD_VALUE)), // 绿马掌气球
            PINK_HORSESHOE_BALLOON = registerCurio("pink_horseshoe_balloon", builder -> builder.rarity(LIGHT_RED)
                    .tooltips(2)
                    .jeiInfos(0)
                    .accessories(of(ValueType.TSUNAMI, 1.3F))
                    .attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.LUCK, 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -100.0, ADD_VALUE)), // 粉马掌气球
            AMBER_HORSESHOE_BALLOON = registerCurio("amber_horseshoe_balloon", builder -> builder.rarity(LIGHT_RED)
                    .tooltips(2)
                    .jeiInfos(0)
                    .accessories(units(ValueType.HONEY$COMB))
                    .attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.LUCK, 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -100.0, ADD_VALUE)), // 琥珀马掌气球
            BUNDLE_OF_HORSESHOE_BALLOONS = registerCurio("bundle_of_horseshoe_balloons", builder -> builder.rarity(YELLOW)
                    .tooltips(2)
                    .jeiInfos(0)
                    .accessories(
                            of(ValueType.SAND$STORM, new Tuple<>(0.45F, 17)),
                            of(ValueType.BLIZZARD, new Tuple<>(0.4F, 14)),
                            of(ValueType.CLOUD, 1.3F)
                    ).attribute(Attributes.JUMP_STRENGTH, 0.43, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.LUCK, 0.05, ADD_VALUE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -100.0, ADD_VALUE)), // 马掌气球束
            INNER_TUBE = registerCurio("inner_tube", builder -> builder.rarity(WHITE).accessories(units(ValueType.FLOAT$ON$LIQUID$SURFACE))),
            FLIPPER = registerCurio("flipper", builder -> builder.noTooltip().attribute(NeoForgeMod.SWIM_SPEED, 0.5, ADD_VALUE)), // 脚蹼
            DIVING_GEAR = registerCurio("diving_gear", builder -> builder.jeiInfos(0).rarity(LIGHT_RED).equipable(EquipmentSlot.HEAD).accessories(units(ValueType.DIVING)).attribute(NeoForgeMod.SWIM_SPEED, 0.5, ADD_VALUE)), // 潜水装备
            JELLYFISH_NECKLACE = registerDirectly("jellyfish_necklace", name -> new RequiresModLoadedCurioItem(BaseCurioItem.builder(name).rarity(GREEN).accessories(of(ValueType.LUMINANCE, 12)), "sodiumdynamiclights")), // 水母项链
            JELLYFISH_DIVING_GEAR = registerDirectly("jellyfish_diving_gear", name -> new RequiresModLoadedCurioItem(BaseCurioItem.builder(name).rarity(PINK).tooltips(1).jeiInfos(0)
                    .accessories(units(ValueType.DIVING), of(ValueType.LUMINANCE, 12), of(ValueType.EFFECT$IMMUNITIES, Set.of()))
                    .attribute(NeoForgeMod.SWIM_SPEED, 0.5, ADD_VALUE), "sodiumdynamiclights")), // 水母潜水装备
            ARCTIC_DIVING_GEAR = registerDirectly("arctic_diving_gear", name -> new RequiresModLoadedCurioItem(BaseCurioItem.builder(name).rarity(LIGHT_PURPLE).tooltips(2)
                    .accessories(units(ValueType.DIVING, ValueType.ICE$SPEED, ValueType.FROZEN$IMMUNE), of(ValueType.LUMINANCE, 12))
                    .attribute(NeoForgeMod.SWIM_SPEED, 0.5, ADD_VALUE), "sodiumdynamiclights")), // 北极潜水装备
            FROG_LEG = registerCurio("frog_leg", builder -> builder
                    .tooltips(1)
                    .attribute(Attributes.SAFE_FALL_DISTANCE, 7.0, ADD_VALUE)
                    .attribute(Attributes.JUMP_STRENGTH, 0.6, ADD_MULTIPLIED_TOTAL)), // 蛙腿
            FROG_FLIPPER = registerCurio("frog_flipper", builder -> builder
                    .tooltips(1)
                    .jeiInfos(0)
                    .attribute(NeoForgeMod.SWIM_SPEED, 0.5, ADD_VALUE)
                    .attribute(Attributes.SAFE_FALL_DISTANCE, 7.0, ADD_VALUE)
                    .attribute(Attributes.JUMP_STRENGTH, 0.6, ADD_MULTIPLIED_TOTAL)), // 青蛙脚蹼
            FROG_WEBBING = registerCurio("frog_webbing", builder -> builder.rarity(PINK)
                    .tooltips(2)
                    .jeiInfos(0)
                    .accessories(of(ValueType.WALL$CLIMB, (byte) 2))
                    .attribute(Attributes.SAFE_FALL_DISTANCE, 7.0, ADD_VALUE)
                    .attribute(Attributes.JUMP_STRENGTH, 0.6, ADD_MULTIPLIED_TOTAL)), // 青蛙蹼
            FROG_GEAR = registerCurio("frog_gear", builder -> builder.rarity(PINK)
                    .tooltips(3)
                    .jeiInfos(0)
                    .accessories(of(ValueType.WALL$CLIMB, (byte) 2))
                    .attribute(NeoForgeMod.SWIM_SPEED, 0.5, ADD_VALUE)
                    .attribute(Attributes.SAFE_FALL_DISTANCE, 7.0, ADD_VALUE)
                    .attribute(Attributes.JUMP_STRENGTH, 0.6, ADD_MULTIPLIED_TOTAL)), // 青蛙装备
            AMBHIPIAN_BOOTS = registerDirectly("ambhipian_boots", name -> new BaseSpeedBoots(1, 40, BaseCurioItem.builder(name)
                    .jeiInfos(0)
                    .attribute(Attributes.SAFE_FALL_DISTANCE, 7.0, ADD_VALUE)
                    .attribute(Attributes.JUMP_STRENGTH, 0.6, ADD_MULTIPLIED_TOTAL))); // 水陆两用靴

    public static final Supplier<BaseCurioItem> TREASURE_MAGNET = registerCurio("treasure_magnet", builder -> builder.attribute(TCAttributes.PICKUP_RANGE, 6.25, ADD_VALUE)), // 宝藏磁石
            FLOWER_BOOTS = registerCurio("flower_boots", builder -> builder.rarity(LIME).accessories(units(ValueType.FLOWER$BOOTS))); // 花靴

    public static final Supplier<BaseCurioItem> ANGLER_EARRING = registerCurio("angler_earring", builder -> builder.noTooltip()); // 渔夫耳环

    public static final Supplier<BaseCurioItem> ROYAL_GEL = registerCurio("royal_gel", builder -> builder.rarity(ModRarity.EXPERT).accessories(entry(ValueType.MOB$IGNORE, new EntityTypesValue(EntityType.SLIME)))), // 皇家凝胶
            SHIELD_OF_CTHULHU = registerCurio("shield_of_cthulhu", builder -> builder.rarity(ModRarity.EXPERT).accessories(units(ValueType.SHIELD$OF$CTHULHU))), // 克苏鲁护盾
            WORM_SCARF = registerCurio("worm_scarf", builder -> builder.rarity(ModRarity.EXPERT).accessories(of(ValueType.INJURY$FREE, 0.17F))), // 蠕虫围巾
            BRAIN_OF_CONFUSION = registerCurio("brain_of_confusion", builder -> builder.rarity(ModRarity.EXPERT).accessories(units(ValueType.BRAIN$OF$CONFUSION)).tooltips(2)), // 混乱之脑
            HIVE_PACK = registerCurio("hive_pack", builder -> builder.rarity(ModRarity.EXPERT).accessories(units(ValueType.HIVE$PACK))), // 蜂巢背包
            /* 骨头手套 */
            /* 骸骨头盔 */
            /* 挥发明胶 */
            /* 孢子囊 */
            /* 闪亮石 */
            /* 翱翔徽章 */
            GRAVITY_GLOBE = registerCurio("gravity_globe", builder -> builder.rarity(ModRarity.EXPERT).accessories(units(ValueType.GRAVITY$GLOBE)).tooltips(1)); // 重力球

    public static Supplier<BaseCurioItem> registerCurio(String name, Consumer<BaseCurioItem.Builder> consumer) {
        return CURIOS.register(name, () -> {
            BaseCurioItem.Builder builder = BaseCurioItem.builder(name);
            consumer.accept(builder);
            return builder.build();
        });
    }

    public static Supplier<BaseCurioItem> registerCurio(String name, ModRarity rarity) {
        return CURIOS.register(name, () -> {
            Item.Properties properties = new Item.Properties().component(TCDataComponentTypes.MOD_RARITY, rarity);
            if (rarity != WHITE && rarity != GRAY) properties.fireResistant();
            return new BaseCurioItem(properties);
        });
    }

    public static Supplier<BaseCurioItem> registerCurio(String name, Supplier<BaseCurioItem> supplier) {
        return CURIOS.register(name, supplier);
    }

    public static Supplier<BaseCurioItem> registerDirectly(String name, Function<String, BaseCurioItem> function) {
        return CURIOS.register(name, () -> function.apply(name));
    }

    public static void register(IEventBus eventBus) {
        CURIOS.register(eventBus);
        OTHERS.register(eventBus);
    }
}
