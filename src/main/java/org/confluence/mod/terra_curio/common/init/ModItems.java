package org.confluence.mod.terra_curio.common.init;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.mod.terra_curio.common.item.curio.compat.AnkhShield;
import org.confluence.mod.terra_curio.common.misc.ModAttributes;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TerraCurio.MOD_ID);
    public static final BiMap<ResourceLocation,Supplier<Item>> CURIO_ITEMS = HashBiMap.create();

    public static final Supplier<Item> BEZOAR = simpleImmunityItem("bezoar",ModRarity.LIGHT_RED, MobEffects.POISON);
//    public static final Supplier<Item> BLACKBELT = register("black_belt", BlackBelt::new);
//    public static final Supplier<Item> COBALT_SHIELD = register("cobalt_shield", CobaltShield::new);
//    public static final Supplier<Item> DESTROYER_EMBLEM = register("destroyer_emblem", DestroyerEmblem::new);
//    public static final Supplier<Item> EYE_OF_THE_DESTROYER = register("eye_of_the_destroyer", EyeOfTheGolem::new);
    public static final Supplier<Item> HOLY_WATER = simpleImmunityItem("holy_water",ModRarity.LIGHT_RED, MobEffects.WITHER);
    public static final Supplier<Item> DETOXIFICATION_CAPSULE = simpleImmunityItem("detoxification_capsule",ModRarity.PINK, MobEffects.POISON,MobEffects.WITHER);
    public static final Supplier<Item> VITAMINS = simpleImmunityItem("vitamins",ModRarity.LIGHT_RED, MobEffects.WEAKNESS);
    public static final Supplier<Item> ENERGY_BAR = simpleImmunityItem("energy_bar",ModRarity.LIGHT_RED, MobEffects.HUNGER);
    public static final Supplier<Item> NUTRIENT_SOLUTION = simpleImmunityItem("nutrient_solution",ModRarity.PINK, MobEffects.WEAKNESS,MobEffects.HUNGER);
    public static final Supplier<Item> BLINDFOLD = simpleImmunityItem("blindfold",ModRarity.LIGHT_RED, MobEffects.BLINDNESS);
    public static final Supplier<Item> FLASHLIGHT = simpleImmunityItem("flashlight",ModRarity.LIGHT_RED, MobEffects.DARKNESS);
    public static final Supplier<Item> SEARCHLIGHT = simpleImmunityItem("searchlight",ModRarity.PINK, MobEffects.BLINDNESS,MobEffects.DARKNESS);
    public static final Supplier<Item> FAST_CLOCK = simpleImmunityItem("fast_clock",ModRarity.LIGHT_RED, MobEffects.MOVEMENT_SLOWDOWN);
    public static final Supplier<Item> TRIFOLD_MAP = simpleImmunityItem("trifold_map",ModRarity.LIGHT_RED, MobEffects.CONFUSION);
    public static final Supplier<Item> THE_PLAN = simpleImmunityItem("the_plan",ModRarity.PINK, MobEffects.MOVEMENT_SLOWDOWN,MobEffects.CONFUSION);
    public static final Supplier<Item> HAND_DRILL = simpleImmunityItem("hand_drill",ModRarity.LIGHT_RED, MobEffects.DIG_SLOWDOWN);
    public static final Supplier<Item> SHOT_PUT = simpleImmunityItem("shot_put",ModRarity.LIGHT_RED, MobEffects.LEVITATION);
    public static final Supplier<Item> EXPLORERS_EQUIPMENT = simpleImmunityItem("explorers_equipment",ModRarity.PINK, MobEffects.DIG_SLOWDOWN,MobEffects.LEVITATION);
    public static final Supplier<Item> ANKH_CHARM = simpleImmunityItem("ankh_charm",ModRarity.LIGHT_PURPLE, MobEffects.POISON,MobEffects.HUNGER,MobEffects.DIG_SLOWDOWN,
            MobEffects.WEAKNESS,MobEffects.LEVITATION,MobEffects.WITHER,MobEffects.DARKNESS,MobEffects.BLINDNESS,MobEffects.CONFUSION,MobEffects.MOVEMENT_SLOWDOWN);
    public static final Supplier<Item> ANKH_SHIELD = register("ankh_shield", AnkhShield::new);

        /** example 通用注册模式 **/
    public static final Supplier<Item> AVENGER_EMBLEM = register("avenger_emblem",builder-> builder
                .rarity(ModRarity.MASTER)
                .effectImmunities(MobEffects.FIRE_RESISTANCE,MobEffects.SLOW_FALLING,MobEffects.HARM,MobEffects.POISON)
                .addAttr(ModAttributes.MAGIC_DAMAGE,"magic_damage",1.2f, AttributeModifier.Operation.ADD_VALUE)
                .armor(10f, AttributeModifier.Operation.ADD_VALUE)
                .damage(5,AttributeModifier.Operation.ADD_VALUE)
                .tip("test1")
                .tip("test2")
                .build()
    );




    private static Supplier<Item> register(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }

    //通用注册格式
    private static Supplier<Item> register(String name,Function<BaseCurioItem.Builder, BaseCurioItem.Builder> properties) {
        return ITEMS.register(name, ()->new BaseCurioItem(name,properties));
    }


    @SafeVarargs
    public static Supplier<Item> simpleImmunityItem(String name, ModRarity rarity, Holder<MobEffect>... effect) {
        return simpleImmunityItem(name,2,rarity,effect);
    }
    @SafeVarargs
    public static Supplier<Item> simpleImmunityItem(String name, int jeiInformationCount, ModRarity rarity, Holder<MobEffect>... effect) {
        return ITEMS.register(name, () -> new BaseCurioItem(name,builder -> builder.rarity(rarity).effectImmunities(effect).build()));
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
