package org.confluence.mod.terra_curio.common.init;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.component.AccessoriesComponent;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.component.primitive.EntityTypeValue;
import org.confluence.mod.terra_curio.common.component.primitive.FloatValue;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.mod.terra_curio.common.item.curio.combat.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static org.confluence.mod.terra_curio.common.component.AccessoriesComponent.*;

@SuppressWarnings("all")
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraCurio.MODID);

    public static final Supplier<BaseCurioItem> BEZOAR = registerCurio("bezoar", builder -> builder.effectImmunities(MobEffects.POISON)), // 牛黄 中毒
            HOLY_WATER = registerCurio("holy_water", builder -> builder.effectImmunities(MobEffects.WITHER)), // 圣水 凋零
            DETOXIFICATION_CAPSULE = registerCurio("detoxification_capsule", builder -> builder.effectImmunities(MobEffects.POISON, MobEffects.WITHER)), // 解毒囊
            VITAMINS = registerCurio("vitamins", builder -> builder.effectImmunities(MobEffects.WEAKNESS)), // 维生素 虚弱
            ENERGY_BAR = registerCurio("energy_bar", builder -> builder.effectImmunities(MobEffects.HUNGER)), // 能量棒 饥饿
            NUTRIENT_SOLUTION = registerCurio("nutrient_solution", builder -> builder.effectImmunities(MobEffects.WEAKNESS, MobEffects.HUNGER)), // 营养液
            BLINDFOLD = registerCurio("blindfold", builder -> builder.effectImmunities(MobEffects.BLINDNESS)), // 蒙眼布 失明
            FLASHLIGHT = registerCurio("flashlight", builder -> builder.effectImmunities(MobEffects.DARKNESS)), // 手电筒 黑暗
            SEARCHLIGHT = registerCurio("searchlight", builder -> builder.effectImmunities(MobEffects.BLINDNESS, MobEffects.DARKNESS)), // 探照灯
            FAST_CLOCK = registerCurio("fast_clock", builder -> builder.effectImmunities(MobEffects.MOVEMENT_SLOWDOWN)), // 快走时钟 缓慢
            TRIFOLD_MAP = registerCurio("trifold_map", builder -> builder.effectImmunities(MobEffects.CONFUSION)), // 三折地图 反胃
            THE_PLAN = registerCurio("the_plan", builder -> builder.effectImmunities(MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION)), // 计划书
            HAND_DRILL = registerCurio("hand_drill", builder -> builder.effectImmunities(MobEffects.DIG_SLOWDOWN)), // 手钻 挖掘疲劳
            SHOT_PUT = registerCurio("shot_put", builder -> builder.effectImmunities(MobEffects.LEVITATION)), // 铅球 漂浮
            EXPLORERS_EQUIPMENT = registerCurio("explorers_equipment", builder -> builder.effectImmunities(MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION)), // 探险家宝具
            ANKH_CHARM = registerCurio("ankh_charm", AnkhCharm::new), // 十字章护身符
            ANKH_SHIELD = registerCurio("ankh_shield", AnkhShield::new), // 十字章护盾
//            STAR_CLOAK = registerCurio("star_cloak", StarCloak::new), // 星星斗篷
//            STAR_VEIL = registerCurio("star_veil", StarVeil::new), // 星星面纱
//            BEE_CLOAK = registerCurio("bee_cloak", BeeCloak::new), // 蜜蜂斗篷
            BLACK_BELT = registerCurio("black_belt", builder -> builder.attribute(ModAttributes.getDodgeChance(), "dodge", 0.1, ADD_VALUE)), // 黑腰带
            /* 天界徽章 */
            /* 月光护身符 */
            SUN_STONE = registerCurio("sun_stone", SunStone::new), // 太阳石 (WIP)
            MOON_STONE = registerCurio("moon_stone", MoonStone::new), // 月亮石 (WIP)
            CELESTIAL_STONE = registerCurio("celestial_stone", CelestialStone::new), // 天界石
            /* 月亮贝壳 */
            /* 天界贝壳 */
            COBALT_SHIELD = registerCurio("cobalt_shield", builder -> builder.attribute(Attributes.KNOCKBACK_RESISTANCE, "knockback_resistance", 1.0, ADD_VALUE).attribute(Attributes.ARMOR, "armor", 1.0, ADD_VALUE)), // 钴护盾
//            CROSS_NECKLACE = registerCurio("cross_necklace", CrossNecklace::new), // 十字项链
            RANGER_EMBLEM = registerCurio("ranger_emblem", builder -> builder.noTooltip().attribute(ModAttributes.getRangedDamage(), "ranged_damage", 0.15, ADD_MULTIPLIED_TOTAL)), // 游侠徽章
            WARRIOR_EMBLEM = registerCurio("warrior_emblem", builder -> builder.noTooltip().attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.15, ADD_MULTIPLIED_TOTAL)), // 战士徽章
            SORCERER_EMBLEM = registerCurio("sorcerer_emblem", builder -> builder.noTooltip().attribute(ModAttributes.getMagicDamage(), "magic_damage", 0.15, ADD_MULTIPLIED_TOTAL)), // 巫士徽章
            AVENGER_EMBLEM = registerCurio("avenger_emblem", builder -> builder.noTooltip()
                    .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(ModAttributes.getRangedDamage(), "ranged_damage", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(ModAttributes.getMagicDamage(), "magic_damage", 0.12, ADD_MULTIPLIED_TOTAL)), // 复仇者勋章
            EYE_OF_THE_GOLEM = registerCurio("eye_of_the_golem", builder -> builder.noTooltip().attribute(ModAttributes.getCriticalChance(), "critical_chance", 0.1, ADD_VALUE)), // 石巨人之眼
            DESTROYER_EMBLEM = registerCurio("destroyer_emblem", builder -> builder.noTooltip()
                    .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(ModAttributes.getRangedDamage(), "ranged_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(ModAttributes.getMagicDamage(), "magic_damage", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(ModAttributes.getCriticalChance(), "critical_chance", 0.08, ADD_VALUE)), // 毁灭者勋章
            FERAL_CLAWS = registerCurio("feral_claws", builder -> builder.noTooltip()
                    .accessories(AccessoriesComponent.units(AUTO_ATTACK))
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)), // 狂爪手套
            TITAN_GLOVE = registerCurio("titan_glove", builder -> builder.noTooltip()
                    .accessories(AccessoriesComponent.units(AUTO_ATTACK))
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)), // 泰坦手套
            POWER_GLOVE = registerCurio("power_glove", builder -> builder.noTooltip()
                    .accessories(AccessoriesComponent.units(AUTO_ATTACK))
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)), // 强力手套
            MECHANICAL_GLOVE = registerCurio("mechanical_glove", builder -> builder.noTooltip()
                    .accessories(AccessoriesComponent.units(AUTO_ATTACK))
                    .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)), // 机械手套
            FIRE_GAUNTLET = registerCurio("fire_gauntlet", builder -> builder
                    .accessories(AccessoriesComponent.units(AUTO_ATTACK, FIRE_ATTACK))
                    .attribute(Attributes.ATTACK_DAMAGE, "attack_damage", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)), // 烈火手套
            FLESH_KNUCKLES = registerCurio("flesh_knuckles", builder -> builder
                    .attribute(Attributes.ARMOR, "armor", 8.0, ADD_VALUE)
                    .attribute(ModAttributes.getAggro(), "aggro", 400, ADD_VALUE)), // 血肉指虎
            BERSERKERS_GLOVE = registerCurio("berserkers_glove", builder -> builder
                    .accessories(AccessoriesComponent.units(AUTO_ATTACK))
                    .attribute(Attributes.ARMOR, "armor", 8.0, ADD_VALUE)
                    .attribute(Attributes.ATTACK_SPEED, "attack_speed", 0.12, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 1.0, ADD_MULTIPLIED_TOTAL)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, "entity_interaction_range", 0.1, ADD_MULTIPLIED_TOTAL)
                    .attribute(ModAttributes.getAggro(), "aggro", 400, ADD_VALUE)); // 狂战士手套
//            PALADINS_SHIELD = registerCurio("paladins_shield", PaladinsShield::new), // 圣骑士护盾
//            HERO_SHIELD = registerCurio("hero_shield", HeroShield::new), // 英雄护盾
//            FROZEN_TURTLE_SHELL = registerCurio("frozen_turtle_shell", FrozenTurtleShell::new), // 冰冻海龟壳
//            FROZEN_SHIELD = registerCurio("frozen_shield", FrozenShield::new), // 冰冻护盾
//            HONEY_COMB = registerCurio("honey_comb", HoneyComb::new), // 蜂窝
//            SHARK_TOOTH_NECKLACE = registerCurio("shark_tooth_necklace", SharkToothNecklace::new), // 鲨牙项链
//            STINGER_NECKLACE = registerCurio("stinger_necklace", StingerNecklace::new), // 毒刺项链
//            PANIC_NECKLACE = registerCurio("panic_necklace", PanicNecklace::new), // 恐慌项链
//            SWEETHEART_NECKLACE = registerCurio("sweetheart_necklace", SweetheartNecklace::new), // 甜心项链
//            MAGIC_QUIVER = registerCurio("magic_quiver", MagicQuiver::new), // 魔法箭袋
//            MOLTEN_QUIVER = registerCurio("molten_quiver", MoltenQuiver::new), // 熔火箭袋
//            STALKERS_QUIVER = registerCurio("stalkers_quiver", StalkersQuiver::new), // 潜行者箭袋
//            RIFLE_SCOPE = registerCurio("rifle_scope", RifleScope::new), // 步枪瞄准镜
//            SNIPER_SCOPE = registerCurio("sniper_scope", SniperScope::new), // 狙击镜
//            RECON_SCOPE = registerCurio("recon_scope", ReconScope::new), // 侦察镜
//            MAGMA_STONE = registerCurio("magma_stone", MagmaStone::new), // 岩浆石
//            OBSIDIAN_ROSE = registerCurio("obsidian_rose", ObsidianRose::new), // 黑曜石玫瑰
//            OBSIDIAN_SHIELD = registerCurio("obsidian_shield", ObsidianShield::new), // 黑曜石护盾
//            OBSIDIAN_SKULL = registerCurio("obsidian_skull", ObsidianSkull::new), // 黑曜石骷髅头
//            MOLTEN_SKULL_ROSE = registerCurio("molten_skull_rose", MoltenSkullRose::new), // 熔火骷髅头玫瑰
//            OBSIDIAN_SKULL_ROSE = registerCurio("obsidian_skull_rose", ObsidianSkullRose::new), // 黑曜石骷髅头玫瑰
//            HAND_WARMER = registerCurio("hand_warmer", HandWarmer::new), // 暖手宝
//            PUTRID_SCENT = registerCurio("putrid_scent", PutridScent::new), // 腐香囊
//            SHACKLE = registerCurio("shackle", Shackle::new); // 脚镣

    public static final Supplier<BaseCurioItem> ROYAL_GEL = registerCurio("royal_gel", builder -> builder.rarity(ModRarity.EXPERT).accessories(AccessoriesComponent.of(LIVING_IGNORE, new EntityTypeValue(EntityType.SLIME)))), // 皇家凝胶
            SHIELD_OF_CTHULHU = registerCurio("shield_of_cthulhu", builder -> builder.rarity(ModRarity.EXPERT).accessories(AccessoriesComponent.units(CTHULHU))), // 克苏鲁护盾
            WORM_SCARF = registerCurio("worm_scarf", builder -> builder.rarity(ModRarity.EXPERT).accessories(AccessoriesComponent.of(INJURY_FREE, new FloatValue(0.17F)))), // 蠕虫围巾
            BRAIN_OF_CONFUSION = registerCurio("brain_of_confusion", builder -> builder.rarity(ModRarity.EXPERT).accessories(AccessoriesComponent.units(BRAIN))), // 混乱之脑
            HIVE_PACK = registerCurio("hive_pack", builder -> builder.rarity(ModRarity.EXPERT).accessories(AccessoriesComponent.units(HIVE))), // 蜂巢背包
    /* 骨头手套 */
    /* 骸骨头盔 */
    /* 挥发明胶 */
    /* 孢子囊 */
    /* 闪亮石 */
    /* 翱翔徽章 */
    GRAVITY_GLOBE = registerCurio("gravity_globe", builder -> builder.accessories(AccessoriesComponent.units(GRAVITY))); // 重力球

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
