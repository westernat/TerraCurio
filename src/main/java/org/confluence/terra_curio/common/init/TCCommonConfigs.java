package org.confluence.terra_curio.common.init;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.terra_curio.TerraCurio;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class TCCommonConfigs {
    public static final Supplier<String> STRING_SUPPLIER = () -> "";
    public static final Predicate<Object> FILTER_CONFLUENCE = o -> {
        if (o instanceof String s && s.startsWith("confluence:")) {
            return ConfluenceMagicLib.IS_CONFLUENCE_LOADED;
        }
        return true;
    };
    private static ModConfigSpec.ConfigValue<List<? extends String>> RARE_BLOCKS;
    private static ModConfigSpec.ConfigValue<List<? extends String>> RARE_CREATURES;
    public static ArrayList<BlockState> rareBlocks = new ArrayList<>();
    public static ArrayList<EntityType<?>> rareCreatures = new ArrayList<>();

    public static ModConfigSpec.BooleanValue RANDOM_ATTACK_DAMAGE;
    public static ModConfigSpec.DoubleValue RANDOM_ATTACK_DAMAGE_MIN;
    public static ModConfigSpec.DoubleValue RANDOM_ATTACK_DAMAGE_MAX;

    public static ModConfigSpec.IntValue MAX_ACCESSORIES;

    public static void onLoad() {
        RARE_BLOCKS.get().forEach(s -> {
            try {
                rareBlocks.add(BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), s, false).blockState());
            } catch (Exception e) {
                TerraCurio.LOGGER.error(e.getMessage());
            }
        });
        RARE_CREATURES.get().forEach(s -> rareCreatures.add(BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(s))));
    }

    public static void register(ModContainer container) {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        RARE_BLOCKS = BUILDER.comment(
                "In order for the block to be found by the Metal Detector",
                "You need to fill the list with string like 'modid:block[state1=true]' or 'modid:block'",
                "The higher the block in the list, the higher the value"
        ).defineListAllowEmpty("rareBlocks", List.of(
                "confluence:life_crystal_block",
                "confluence:opal_ore",
                "confluence:gelstone_ore",
                "confluence:cold_crystal_ore",
                "confluence:tr_crimson_ore",
                "confluence:deepslate_tr_crimson_ore",
                "confluence:demonite_ore",
                "confluence:deepslate_demonite_ore",
                "minecraft:ancient_debris",
                "minecraft:diamond_ore",
                "minecraft:deepslate_diamond_ore",
                "confluence:platinum_ore",
                "confluence:deepslate_platinum_ore",
                "minecraft:gold_ore",
                "minecraft:deepslate_gold_ore",
                "minecraft:chest",
                "confluence:forest_pot",
                "confluence:tundra_pot",
                "confluence:spider_nest_pot",
                "confluence:underground_desert_pot",
                "confluence:jungle_pot",
                "confluence:marble_cave_pot",
                "confluence:pyramid_pot",
                "confluence:corruption_pot",
                "confluence:tr_crimson_pot",
                "confluence:dungeon_pot",
                "confluence:underworld_pot",
                "confluence:lihzahrd_pot",
                "confluence:tungsten_ore",
                "confluence:deepslate_tungsten_ore",
                "confluence:silver_ore",
                "confluence:deepslate_silver_ore",
                "confluence:lead_ore",
                "confluence:deepslate_lead_ore",
                "minecraft:iron_ore",
                "minecraft:deepslate_iron_ore",
                "confluence:tin_ore",
                "confluence:deepslate_tin_ore",
                "minecraft:copper_ore",
                "minecraft:deepslate_copper_ore"
        ), STRING_SUPPLIER, FILTER_CONFLUENCE);
        RARE_CREATURES = BUILDER.comment(
                "In order for the creature to be found by the Life Form Analyzer",
                "You need to fill the list with string like 'modid:entity'",
                "The higher the creature in the list, the higher the value"
        ).defineListAllowEmpty("rareCreatures", List.of(
                "terra_entity:nymph",
                "terra_entity:wandering_eye_fish",
                "terra_entity:pink_slime",
                "minecraft:skeleton_horse",
                "minecraft:sniffer",
                "minecraft:allay",
                "minecraft:warden",
                "minecraft:mooshroom",
                "minecraft:panda"
        ), STRING_SUPPLIER, FILTER_CONFLUENCE);
        RANDOM_ATTACK_DAMAGE = BUILDER.push("Random Attack Damage").define("enable", false);
        RANDOM_ATTACK_DAMAGE_MIN = BUILDER.defineInRange("min", 0.8, 0.0, 1.0);
        RANDOM_ATTACK_DAMAGE_MAX = BUILDER.defineInRange("max", 1.2, 1.0, 2.0);
        MAX_ACCESSORIES = BUILDER.pop().defineInRange("Max Accessory Amount", 7, 6, 100);
        container.registerConfig(ModConfig.Type.COMMON, BUILDER.build());
    }
}
