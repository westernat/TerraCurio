package org.confluence.terra_curio.common.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import org.confluence.terra_curio.TerraCurio;

import java.util.concurrent.CompletableFuture;

public class TCGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public TCGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, TerraCurio.MODID);
    }

    @Override
    protected void start() {
        lootTableId("archaeology", "desert_pyramid", "common");
        lootTableId("archaeology", "desert_well", "common");
        lootTableId("archaeology", "ocean_ruin_cold", "common");
        lootTableId("archaeology", "ocean_ruin_warm", "common");
        lootTableId("archaeology", "trail_ruins_common", "common");
        lootTableId("archaeology", "trail_ruins_rare", "common");

        lootTableId("blocks", "moss_block");

        lootTableId("chests/village", "village_armorer");
        lootTableId("chests/village", "village_fisher");
        lootTableId("chests/village", "village_plains_house");
        lootTableId("chests/village", "village_toolsmith");
        lootTableId("chests", "abandoned_mineshaft");
        lootTableId("chests", "ancient_city");
        lootTableId("chests", "ancient_city_ice_box");
        lootTableId("chests", "bastion_treasure");
        lootTableId("chests", "desert_pyramid");
        lootTableId("chests", "end_city_treasure");
        lootTableId("chests", "igloo_chest");
        lootTableId("chests", "jungle_temple");
        lootTableId("chests", "nether_bridge");
        lootTableId("chests", "shipwreck_supply");
        lootTableId("chests", "shipwreck_treasure");
        lootTableId("chests", "spawn_bonus_chest");
        lootTableId("chests", "stronghold_corridor");
        lootTableId("chests", "stronghold_crossing");
        lootTableId("chests", "stronghold_library");
        lootTableId("chests", "underwater_ruin_big");
        lootTableId("chests", "underwater_ruin_small");
        lootTableId("chests", "simple_dungeon");
        lootTableId("chests", "woodland_mansion");

        lootTableId("entities", "bat");
        lootTableId("entities", "bee");
        lootTableId("entities", "blaze");
        lootTableId("entities", "cave_spider");
        lootTableId("entities", "creeper");
        lootTableId("entities", "drowned");
        lootTableId("entities", "elder_guardian");
        lootTableId("entities", "enderman");
        lootTableId("entities", "ender_dragon");
        lootTableId("entities", "evoker");
        lootTableId("entities", "frog");
        lootTableId("entities", "ghast");
        lootTableId("entities", "glow_squid");
        lootTableId("entities", "guardian");
        lootTableId("entities", "husk");
        lootTableId("entities", "iron_golem");
        lootTableId("entities", "magma_cube");
        lootTableId("entities", "phantom");
        lootTableId("entities", "piglin_brute");
        lootTableId("entities", "ravager");
        lootTableId("entities", "shulker");
        lootTableId("entities", "skeleton");
        lootTableId("entities", "skeleton_horse");
        lootTableId("entities", "slime");
        lootTableId("entities", "spider");
        lootTableId("entities", "stray");
        lootTableId("entities", "turtle");
        lootTableId("entities", "vindicator");
        lootTableId("entities", "warden");
        lootTableId("entities", "witch");
        lootTableId("entities", "wither");
        lootTableId("entities", "wither_skeleton");
        lootTableId("entities", "zombie");
        lootTableId("entities", "zombie_villager");
        lootTableId("entities", "zombified_piglin");

        lootTableId("gameplay/hero_of_the_village", "librarian_gift");
        lootTableId("gameplay", "cat_morning_gift");
    }

    private void lootTableId(String group, String condition, String table) {
        String modifier = group + "/" + condition;
        LootItemCondition[] conditions = {LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace(modifier)).build()};
        add(modifier, new AddTableLootModifier(conditions, ResourceKey.create(Registries.LOOT_TABLE, TerraCurio.asResource("with/" + group + "/" + table))), TCDataGenerator.CONFLUENCE_NOT_LOADED);
    }

    private void lootTableId(String group, String table) {
        lootTableId(group, table, table);
    }
}
