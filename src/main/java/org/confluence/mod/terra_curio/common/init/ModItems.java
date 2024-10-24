package org.confluence.mod.terra_curio.common.init;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;

import java.util.function.Consumer;
import java.util.function.Supplier;

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
            ANKH_CHARM = registerCurio("ankh_charm", builder -> builder.effectImmunities(MobEffects.POISON, MobEffects.WITHER, MobEffects.WEAKNESS, MobEffects.HUNGER, MobEffects.BLINDNESS, MobEffects.DARKNESS, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION, MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION)), // 十字章护身符
            ANKH_SHIELD = registerCurio("ankh_shield", builder -> builder.effectImmunities(MobEffects.POISON, MobEffects.WITHER, MobEffects.WEAKNESS, MobEffects.HUNGER, MobEffects.BLINDNESS, MobEffects.DARKNESS, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.CONFUSION, MobEffects.DIG_SLOWDOWN, MobEffects.LEVITATION)); // 十字章护盾

    public static Supplier<Item> register(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }

    public static Supplier<BaseCurioItem> registerCurio(String name, Consumer<BaseCurioItem.Builder> consumer) {
        BaseCurioItem.Builder builder = BaseCurioItem.builder(name);
        consumer.accept(builder);
        return ITEMS.register(name, builder::build);
    }
}
