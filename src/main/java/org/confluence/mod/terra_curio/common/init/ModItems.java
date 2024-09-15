package org.confluence.mod.terra_curio.common.init;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.item.curio.compat.*;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.mod.terra_curio.common.misc.ModRarity;

import java.util.List;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TerraCurio.MOD_ID);
    public static final BiMap<ResourceLocation,Supplier<Item>> CURIO_ITEMS = HashBiMap.create();

    public static final Supplier<Item> ANKH_CHARM = register("ankh_charm", AnkhCharm::new);
    public static final Supplier<Item> ANKH_SHIELD = register("ankh_shield", AnkhShield::new);
    public static final Supplier<Item> BEZOAR = simpleImmunityItem("bezoar", MobEffects.POISON);
    public static final Supplier<Item> BLACKBELT = register("blackbelt", BlackBelt::new);
    public static final Supplier<Item> BLINDFOLD = simpleImmunityItem("blindfold", MobEffects.BLINDNESS);
    public static final Supplier<Item> COBALT_SHIELD = register("cobalt_shield", CobaltShield::new);
    public static final Supplier<Item> DESTROYER_EMBLEM = register("destroyer_emblem", DestroyerEmblem::new);
    public static final Supplier<Item> EYE_OF_THE_DESTROYER = register("eye_of_the_destroyer", EyeOfTheGolem::new);
    public static final Supplier<Item> FAST_CLOCK = simpleImmunityItem("fast_clock", MobEffects.MOVEMENT_SLOWDOWN);
    public static final Supplier<Item> HOLY_WATER = simpleImmunityItem("holy_water", MobEffects.WITHER);
    public static final Supplier<Item> DETOXIFICATION_CAPSULE = simpleImmunityItem("detoxification_capsule", MobEffects.POISON,MobEffects.WITHER);
    public static final Supplier<Item> VITAMINS = simpleImmunityItem("vitamins", MobEffects.WEAKNESS);
    public static final Supplier<Item> ENERGY_BAR = simpleImmunityItem("energy_bar", MobEffects.HUNGER);
    public static final Supplier<Item> NUTRIENT_SOLUTION = simpleImmunityItem("nutrient_solution", MobEffects.WEAKNESS,MobEffects.HUNGER);
    public static final Supplier<Item> FLASHLIGHT = simpleImmunityItem("flashlight", MobEffects.DARKNESS);
    public static final Supplier<Item> SEARCHLIGHT = simpleImmunityItem("searchlight", MobEffects.BLINDNESS,MobEffects.DARKNESS);
    public static final Supplier<Item> TRIFOLD_MAP = simpleImmunityItem("trifold_map", MobEffects.CONFUSION);
    public static final Supplier<Item> THE_PLAN = simpleImmunityItem("the_plan", MobEffects.MOVEMENT_SLOWDOWN,MobEffects.CONFUSION);
    public static final Supplier<Item> HAND_DRILL = simpleImmunityItem("hand_drill", MobEffects.DIG_SLOWDOWN);
    public static final Supplier<Item> SHOT_PUT = simpleImmunityItem("shot_put", MobEffects.LEVITATION);
    public static final Supplier<Item> EXPLORERS_EQUIPMENT = simpleImmunityItem("explorers_equipment", MobEffects.DIG_SLOWDOWN,MobEffects.LEVITATION);
    public static final Supplier<Item> AVENGER_EMBLEM = register("avenger_emblem", AvengerEmblem::new);



    private static Supplier<Item> register(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }

    //带有颜色的 displayName
    @SafeVarargs
    public static Supplier<Item> simpleImmunityItem(String name, ModRarity rarity, Holder<MobEffect>... effect) {
        return simpleImmunityItem(name,2,rarity,effect);
    }
    @SafeVarargs
    public static Supplier<Item> simpleImmunityItem(String name, int jeiInformationCount, ModRarity rarity, Holder<MobEffect>... effect) {
        return ITEMS.register(name, () -> new BaseCurioItem(List.of(effect),jeiInformationCount,rarity));
    }


    //默认白色 displayName
    @SafeVarargs
    public static Supplier<Item> simpleImmunityItem(String name, Holder<MobEffect>... effect) {
        return simpleImmunityItem(name,2,effect);
    }
    @SafeVarargs
    public static Supplier<Item> simpleImmunityItem(String name, int jeiInformationCount, Holder<MobEffect>... effect) {
        return ITEMS.register(name, () -> new BaseCurioItem(List.of(effect),jeiInformationCount,ModRarity.WHITE));
    }


    public static class Tab {
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraCurio.MOD_ID);

        public static final Supplier<CreativeModeTab> JEWELRY = CREATIVE_MODE_TAB.register("terra_curio_tab",
                () -> CreativeModeTab.builder().icon(() -> Items.DIAMOND.getDefaultInstance())
                        .title(Component.translatable("creativetab.terra_curio"))
                        .displayItems((parameters, output) -> {
                            ITEMS.getEntries().forEach(entry -> output.accept(entry.get()));
                        })
                        .build()
        );
    }
}
