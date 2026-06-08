package org.confluence.terra_curio.common.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.confluence.lib.common.data.gen.AbstractRecipeProvider;
import org.confluence.lib.common.recipe.AmountIngredient;
import org.confluence.lib.common.recipe.EnvironmentLevelAccess;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.init.TCRecipes;
import org.confluence.terra_curio.common.recipe.WorkshopRecipe;

import java.util.Map;
import java.util.function.Consumer;

public class WorkshopProvider extends AbstractRecipeProvider {
    public WorkshopProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        recipe(writer, TCItems.AMBER_HORSESHOE_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.HONEY_BALLOON.get()), Ingredient.of(TCItems.LUCKY_HORSESHOE.get()));
        recipe(writer, "ambhipian_boots", TCItems.AMBHIPIAN_BOOTS.get().getDefaultInstance(), Ingredient.of(TCItems.SAILFISH_BOOTS.get()), Ingredient.of(TCItems.FROG_LEG.get()));
        recipe(writer, "ankh_charm", TCItems.ANKH_CHARM.get().getDefaultInstance(),
                Ingredient.of(TCItems.DETOXIFICATION_CAPSULE.get()),
                Ingredient.of(TCItems.NUTRIENT_SOLUTION.get()),
                Ingredient.of(TCItems.SEARCHLIGHT.get()),
                Ingredient.of(TCItems.THE_PLAN.get()),
                Ingredient.of(TCItems.EXPLORERS_EQUIPMENT.get()));
        recipe(writer, "ankh_shield", TCItems.ANKH_SHIELD.get().getDefaultInstance(), Ingredient.of(TCItems.ANKH_CHARM.get()), Ingredient.of(TCItems.OBSIDIAN_SHIELD.get()));
        recipe(writer, "architect_gizmo_pack", TCItems.ARCHITECT_GIZMO_PACK.get().getDefaultInstance(),
                Ingredient.of(TCItems.BRICK_LAYER.get()),
                Ingredient.of(TCItems.EXTENDO_GRIP.get()),
                Ingredient.of(TCItems.PORTABLE_CEMENT_MIXER.get()));
        recipe(writer, "arctic_diving_gear", TCItems.ARCTIC_DIVING_GEAR.get().getDefaultInstance(), Ingredient.of(TCItems.JELLYFISH_DIVING_GEAR.get()), Ingredient.of(TCItems.ICE_SKATES.get()));
        recipe(writer, "avenger_emblem", TCItems.AVENGER_EMBLEM.get().getDefaultInstance(),
                Ingredient.of(TCItems.WARRIOR_EMBLEM.get()),
                Ingredient.of(TCItems.SORCERER_EMBLEM.get()),
                Ingredient.of(TCItems.RANGER_EMBLEM.get()));
        recipe(writer, "balloon_pufferfish", TCItems.BALLOON_PUFFERFISH.get().getDefaultInstance(), Ingredient.of(Items.PUFFERFISH), Ingredient.of(TCItems.SHINY_RED_BALLOON.get()));
        recipe(writer, "bee_cloak", TCItems.BEE_CLOAK.get().getDefaultInstance(), Ingredient.of(TCItems.STAR_CLOAK.get()), Ingredient.of(TCItems.HONEY_COMB.get()));
        recipe(writer, "berserkers_glove", TCItems.BERSERKERS_GLOVE.get().getDefaultInstance(), Ingredient.of(TCItems.POWER_GLOVE.get()), Ingredient.of(TCItems.FLESH_KNUCKLES.get()));
        recipe(writer, "blizzard_in_a_balloon", TCItems.BLIZZARD_IN_A_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.BLIZZARD_IN_A_BOTTLE.get()), Ingredient.of(TCItems.SHINY_RED_BALLOON.get()));
        recipe(writer, "blue_horseshoe_balloon", TCItems.BLUE_HORSESHOE_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.CLOUD_IN_A_BALLOON.get()), Ingredient.of(TCItems.LUCKY_HORSESHOE.get()));
        recipe(writer, "bundle_of_balloons", TCItems.BUNDLE_OF_BALLOONS.get().getDefaultInstance(),
                Ingredient.of(TCItems.CLOUD_IN_A_BALLOON.get()),
                Ingredient.of(TCItems.SANDSTORM_IN_A_BALLOON.get()),
                Ingredient.of(TCItems.BLIZZARD_IN_A_BALLOON.get()));
        recipe(writer, "bundle_of_horseshoe_balloons_0", TCItems.BUNDLE_OF_HORSESHOE_BALLOONS.get().getDefaultInstance(), Ingredient.of(TCItems.BUNDLE_OF_BALLOONS.get()), Ingredient.of(TCItems.LUCKY_HORSESHOE.get()));
        recipe(writer, "bundle_of_horseshoe_balloons_1", TCItems.BUNDLE_OF_HORSESHOE_BALLOONS.get().getDefaultInstance(),
                Ingredient.of(TCItems.YELLOW_HORSESHOE_BALLOON.get()),
                Ingredient.of(ItemTags.create(TerraCurio.asResource("any_blizzard_balloons"))),
                Ingredient.of(ItemTags.create(TerraCurio.asResource("any_cloud_balloons"))));
        recipe(writer, "bundle_of_horseshoe_balloons_2", TCItems.BUNDLE_OF_HORSESHOE_BALLOONS.get().getDefaultInstance(),
                Ingredient.of(ItemTags.create(TerraCurio.asResource("any_sandstorm_balloons"))),
                Ingredient.of(TCItems.WHITE_HORSESHOE_BALLOON.get()),
                Ingredient.of(ItemTags.create(TerraCurio.asResource("any_cloud_balloons"))));
        recipe(writer, "bundle_of_horseshoe_balloons_3", TCItems.BUNDLE_OF_HORSESHOE_BALLOONS.get().getDefaultInstance(),
                Ingredient.of(ItemTags.create(TerraCurio.asResource("any_sandstorm_balloons"))),
                Ingredient.of(ItemTags.create(TerraCurio.asResource("any_blizzard_balloons"))),
                Ingredient.of(TCItems.BLUE_HORSESHOE_BALLOON.get()));
        recipe(writer, "bundle_of_horseshoe_balloons_4", TCItems.BUNDLE_OF_HORSESHOE_BALLOONS.get().getDefaultInstance(),
                Ingredient.of(TCItems.SANDSTORM_IN_A_BALLOON.get()),
                Ingredient.of(TCItems.BLIZZARD_IN_A_BALLOON.get()),
                Ingredient.of(TCItems.CLOUD_IN_A_BALLOON.get()),
                Ingredient.of(TCItems.LUCKY_HORSESHOE.get()));
        recipe(writer, "celestial_shell", TCItems.CELESTIAL_SHELL.get().getDefaultInstance(), Ingredient.of(TCItems.MOON_SHELL.get()), Ingredient.of(TCItems.CELESTIAL_STONE.get()));
        recipe(writer, "celestial_stone", TCItems.CELESTIAL_STONE.get().getDefaultInstance(), Ingredient.of(TCItems.SUN_STONE.get()), Ingredient.of(TCItems.MOON_STONE.get()));
        recipe(writer, "cell_phone", TCItems.CELL_PHONE.get().getDefaultInstance(), Ingredient.of(TCItems.PDA.get()), Ingredient.of(TCItems.MAGIC_MIRROR.get()));
        recipe(writer, "cloud_in_a_balloon", TCItems.CLOUD_IN_A_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.CLOUD_IN_A_BOTTLE.get()), Ingredient.of(TCItems.SHINY_RED_BALLOON.get()));
        recipe(writer, "destroyer_emblem", TCItems.DESTROYER_EMBLEM.get().getDefaultInstance(), Ingredient.of(TCItems.AVENGER_EMBLEM.get()), Ingredient.of(TCItems.EYE_OF_THE_GOLEM.get()));
        recipe(writer, "detoxification_capsule", TCItems.DETOXIFICATION_CAPSULE.get().getDefaultInstance(), Ingredient.of(TCItems.BEZOAR.get()), Ingredient.of(TCItems.HOLY_WATER.get()));
        recipe(writer, "diving_gear", TCItems.DIVING_GEAR.get().getDefaultInstance(), Ingredient.of(TCItems.DIVING_HELMET.get()), Ingredient.of(TCItems.FLIPPER.get()));
        recipe(writer, "explorers_equipment", TCItems.EXPLORERS_EQUIPMENT.get().getDefaultInstance(), Ingredient.of(TCItems.HAND_DRILL.get()), Ingredient.of(TCItems.SHOT_PUT.get()));
        recipe(writer, "fairy_boots", TCItems.FAIRY_BOOTS.get().getDefaultInstance(), Ingredient.of(TCItems.SPECTRE_BOOTS.get()), Ingredient.of(TCItems.FLOWER_BOOTS.get()));
        recipe(writer, "fart_in_a_balloon", TCItems.FART_IN_A_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.FART_IN_A_JAR.get()), Ingredient.of(TCItems.SHINY_RED_BALLOON.get()));
        recipe(writer, "fire_gauntlet", TCItems.FIRE_GAUNTLET.get().getDefaultInstance(), Ingredient.of(TCItems.MAGMA_STONE.get()), Ingredient.of(TCItems.MECHANICAL_GLOVE.get()));
        recipe(writer, "fish_finder", TCItems.FISH_FINDER.get().getDefaultInstance(),
                Ingredient.of(TCItems.FISHERMANS_POCKET_GUIDE.get()),
                Ingredient.of(TCItems.WEATHER_RADIO.get()),
                Ingredient.of(TCItems.SEXTANT.get()));
        recipe(writer, "frog_flipper", TCItems.FROG_FLIPPER.get().getDefaultInstance(), Ingredient.of(TCItems.FROG_LEG.get()), Ingredient.of(TCItems.FLIPPER.get()));
        recipe(writer, "frog_gear", TCItems.FROG_GEAR.get().getDefaultInstance(), Ingredient.of(TCItems.TIGER_CLIMBING_GEAR.get()), Ingredient.of(TCItems.FROG_FLIPPER.get()));
        recipe(writer, "frog_gear_from_flipper", TCItems.FROG_GEAR.get().getDefaultInstance(), Ingredient.of(TCItems.FROG_WEBBING.get()), Ingredient.of(TCItems.FLIPPER.get()));
        recipe(writer, "frog_webbing", TCItems.FROG_WEBBING.get().getDefaultInstance(), Ingredient.of(TCItems.TIGER_CLIMBING_GEAR.get()), Ingredient.of(TCItems.FROG_LEG.get()));
        recipe(writer, "frostspark_boots", TCItems.FROSTSPARK_BOOTS.get().getDefaultInstance(), Ingredient.of(TCItems.LIGHTNING_BOOTS.get()), Ingredient.of(TCItems.ICE_SKATES.get()));
        recipe(writer, "frozen_shield", TCItems.FROZEN_SHIELD.get().getDefaultInstance(), Ingredient.of(TCItems.PALADINS_SHIELD.get()), Ingredient.of(TCItems.FROZEN_TURTLE_SHELL.get()));
        recipe(writer, "goblin_tech", TCItems.GOBLIN_TECH.get().getDefaultInstance(),
                Ingredient.of(TCItems.STOPWATCH.get()),
                Ingredient.of(TCItems.METAL_DETECTOR.get()),
                Ingredient.of(TCItems.DPS_METER.get()));
        recipe(writer, "gps_from_gold_watch", TCItems.GPS.get().getDefaultInstance(),
                Ingredient.of(TCItems.GOLD_WATCH.get()),
                Ingredient.of(TCItems.DEPTH_METER.get()),
                Ingredient.of(TCItems.COMPASS.get()));
        recipe(writer, "gps_from_platinum_watch", TCItems.GPS.get().getDefaultInstance(),
                Ingredient.of(TCItems.PLATINUM_WATCH.get()),
                Ingredient.of(TCItems.DEPTH_METER.get()),
                Ingredient.of(TCItems.COMPASS.get()));
        recipe(writer, "green_horseshoe_balloon", TCItems.GREEN_HORSESHOE_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.FART_IN_A_BALLOON.get()), Ingredient.of(TCItems.LUCKY_HORSESHOE.get()));
        recipe(writer, "hand_of_creation", TCItems.HAND_OF_CREATION.get().getDefaultInstance(),
                Ingredient.of(TCItems.STEP_STOOL.get()),
                Ingredient.of(TCItems.ARCHITECT_GIZMO_PACK.get()),
                Ingredient.of(TCItems.ANCIENT_CHISEL.get()),
                Ingredient.of(TCItems.TREASURE_MAGNET.get()));
        recipe(writer, "hand_warmer", TCItems.HAND_WARMER.get().getDefaultInstance(),
                Ingredient.of(Items.RED_WOOL),
                Ingredient.of(Items.WHITE_WOOL),
                Ingredient.of(Items.LEATHER));
        recipe(writer, "hero_shield", TCItems.HERO_SHIELD.get().getDefaultInstance(), Ingredient.of(TCItems.PALADINS_SHIELD.get()), Ingredient.of(TCItems.FLESH_KNUCKLES.get()));
        recipe(writer, "honey_balloon", TCItems.HONEY_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.HONEY_COMB.get()), Ingredient.of(TCItems.SHINY_RED_BALLOON.get()));
        recipe(writer, "jellyfish_diving_gear", TCItems.JELLYFISH_DIVING_GEAR.get().getDefaultInstance(), Ingredient.of(TCItems.DIVING_GEAR.get()), Ingredient.of(TCItems.JELLYFISH_NECKLACE.get()));
        recipe(writer, "lava_waders_from_lava_charm", TCItems.LAVA_WADERS.get().getDefaultInstance(),
                Ingredient.of(TCItems.OBSIDIAN_WATER_WALKING_BOOTS.get()),
                Ingredient.of(TCItems.OBSIDIAN_ROSE.get()),
                Ingredient.of(TCItems.LAVA_CHARM.get()));
        recipe(writer, "lava_waders_from_molten_charm", TCItems.LAVA_WADERS.get().getDefaultInstance(),
                Ingredient.of(TCItems.OBSIDIAN_WATER_WALKING_BOOTS.get()),
                Ingredient.of(TCItems.OBSIDIAN_ROSE.get()),
                Ingredient.of(TCItems.MOLTEN_CHARM.get()));
        recipe(writer, "lava_waders_from_molten_skull_rose", TCItems.LAVA_WADERS.get().getDefaultInstance(), Ingredient.of(TCItems.WATER_WALKING_BOOTS.get()), Ingredient.of(TCItems.MOLTEN_SKULL_ROSE.get()));
        recipe(writer, "lava_waders_from_obsidian_water_walking_boots", TCItems.LAVA_WADERS.get().getDefaultInstance(), Ingredient.of(TCItems.OBSIDIAN_WATER_WALKING_BOOTS.get()), Ingredient.of(TCItems.MOLTEN_SKULL_ROSE.get()));
        recipe(writer, "lava_waders_from_water_walking_boots", TCItems.LAVA_WADERS.get().getDefaultInstance(),
                Ingredient.of(TCItems.WATER_WALKING_BOOTS.get()),
                Ingredient.of(TCItems.OBSIDIAN_ROSE.get()),
                Ingredient.of(TCItems.MOLTEN_CHARM.get()));
        recipe(writer, "lightning_boots", TCItems.LIGHTNING_BOOTS.get().getDefaultInstance(),
                Ingredient.of(TCItems.SPECTRE_BOOTS.get()),
                Ingredient.of(TCItems.AGLET.get()),
                Ingredient.of(TCItems.ANKLET_OF_THE_WIND.get()));
        recipe(writer, "magma_skull", TCItems.MAGMA_SKULL.get().getDefaultInstance(), Ingredient.of(TCItems.LAVA_CHARM.get()), Ingredient.of(TCItems.OBSIDIAN_SKULL.get()));
        recipe(writer, "master_ninja_gear", TCItems.MASTER_NINJA_GEAR.get().getDefaultInstance(),
                Ingredient.of(TCItems.BLACK_BELT.get()),
                Ingredient.of(TCItems.TIGER_CLIMBING_GEAR.get()),
                Ingredient.of(TCItems.TABI.get()));
        recipe(writer, "mechanical_glove", TCItems.MECHANICAL_GLOVE.get().getDefaultInstance(), Ingredient.of(TCItems.POWER_GLOVE.get()), Ingredient.of(TCItems.AVENGER_EMBLEM.get()));
        recipe(writer, "molten_charm", TCItems.MOLTEN_CHARM.get().getDefaultInstance(), Ingredient.of(TCItems.LAVA_CHARM.get()), Ingredient.of(TCItems.OBSIDIAN_SKULL.get()));
        recipe(writer, "molten_quiver", TCItems.MOLTEN_QUIVER.get().getDefaultInstance(), Ingredient.of(TCItems.MAGIC_QUIVER.get()), Ingredient.of(TCItems.MAGMA_STONE.get()));
        recipe(writer, "molten_skull_rose_from_lava_charm", TCItems.MOLTEN_SKULL_ROSE.get().getDefaultInstance(), Ingredient.of(TCItems.OBSIDIAN_SKULL_ROSE.get()), Ingredient.of(TCItems.LAVA_CHARM.get()));
        recipe(writer, "molten_skull_rose_from_obsidian_rose", TCItems.MOLTEN_SKULL_ROSE.get().getDefaultInstance(), Ingredient.of(TCItems.MAGMA_SKULL.get()), Ingredient.of(TCItems.OBSIDIAN_ROSE.get()));
        recipe(writer, "molten_skull_rose_from_obsidian_skull_rose", TCItems.MOLTEN_SKULL_ROSE.get().getDefaultInstance(), Ingredient.of(TCItems.MAGMA_SKULL.get()), Ingredient.of(TCItems.OBSIDIAN_SKULL_ROSE.get()));
        recipe(writer, "moon_shell", TCItems.MOON_SHELL.get().getDefaultInstance(), Ingredient.of(TCItems.MOON_CHARM.get()), Ingredient.of(TCItems.NEPTUNES_SHELL.get()));
        recipe(writer, "nutrient_solution", TCItems.NUTRIENT_SOLUTION.get().getDefaultInstance(), Ingredient.of(TCItems.VITAMINS.get()), Ingredient.of(TCItems.ENERGY_BAR.get()));
        recipe(writer, "obsidian_horseshoe", TCItems.OBSIDIAN_HORSESHOE.get().getDefaultInstance(), Ingredient.of(TCItems.LUCKY_HORSESHOE.get()), Ingredient.of(TCItems.OBSIDIAN_SKULL.get()));
        recipe(writer, "obsidian_shield", TCItems.OBSIDIAN_SHIELD.get().getDefaultInstance(), Ingredient.of(TCItems.COBALT_SHIELD.get()), Ingredient.of(TCItems.OBSIDIAN_SKULL.get()));
        recipe(writer, "obsidian_skull_rose", TCItems.OBSIDIAN_SKULL_ROSE.get().getDefaultInstance(), Ingredient.of(TCItems.OBSIDIAN_ROSE.get()), Ingredient.of(TCItems.OBSIDIAN_SKULL.get()));
        recipe(writer, "obsidian_water_walking_boots", TCItems.OBSIDIAN_WATER_WALKING_BOOTS.get().getDefaultInstance(), Ingredient.of(TCItems.WATER_WALKING_BOOTS.get()), Ingredient.of(TCItems.OBSIDIAN_SKULL.get()));
        recipe(writer, "pda", TCItems.PDA.get().getDefaultInstance(),
                Ingredient.of(TCItems.FISH_FINDER.get()),
                Ingredient.of(TCItems.GOBLIN_TECH.get()),
                Ingredient.of(TCItems.REK_3000.get()),
                Ingredient.of(TCItems.GPS.get()));
        recipe(writer, "pink_horseshoe_balloon", TCItems.PINK_HORSESHOE_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.SHARKRON_BALLOON.get()), Ingredient.of(TCItems.LUCKY_HORSESHOE.get()));
        recipe(writer, "power_glove", TCItems.POWER_GLOVE.get().getDefaultInstance(), Ingredient.of(TCItems.FERAL_CLAWS.get()), Ingredient.of(TCItems.TITAN_GLOVE.get()));
        recipe(writer, "recon_scope", TCItems.RECON_SCOPE.get().getDefaultInstance(), Ingredient.of(TCItems.SNIPER_SCOPE.get()), Ingredient.of(TCItems.PUTRID_SCENT.get()));
        recipe(writer, "rek_3000", TCItems.REK_3000.get().getDefaultInstance(),
                Ingredient.of(TCItems.RADAR.get()),
                Ingredient.of(TCItems.LIFE_FORM_ANALYZER.get()),
                Ingredient.of(TCItems.TALLY_COUNTER.get()));
        recipe(writer, "sandstorm_in_a_balloon", TCItems.SANDSTORM_IN_A_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.SANDSTORM_IN_A_BOTTLE.get()), Ingredient.of(TCItems.SHINY_RED_BALLOON.get()));
        recipe(writer, "searchlight", TCItems.SEARCHLIGHT.get().getDefaultInstance(), Ingredient.of(TCItems.BLINDFOLD.get()), Ingredient.of(TCItems.FLASHLIGHT.get()));
        recipe(writer, "sharkron_balloon", TCItems.SHARKRON_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.TSUNAMI_IN_A_BOTTLE.get()), Ingredient.of(TCItems.BALLOON_PUFFERFISH.get()));
        recipe(writer, "sniper_scope", TCItems.SNIPER_SCOPE.get().getDefaultInstance(), Ingredient.of(TCItems.RIFLE_SCOPE.get()), Ingredient.of(TCItems.DESTROYER_EMBLEM.get()));
        recipe(writer, "spectre_boots_from_dunerider_boots", TCItems.SPECTRE_BOOTS.get().getDefaultInstance(), Ingredient.of(TCItems.DUNERIDER_BOOTS.get()), Ingredient.of(TCItems.ROCKET_BOOTS.get()));
        recipe(writer, "spectre_boots_from_flurry_boots", TCItems.SPECTRE_BOOTS.get().getDefaultInstance(), Ingredient.of(TCItems.FLURRY_BOOTS.get()), Ingredient.of(TCItems.ROCKET_BOOTS.get()));
        recipe(writer, "spectre_boots_from_hermes_boots", TCItems.SPECTRE_BOOTS.get().getDefaultInstance(), Ingredient.of(TCItems.HERMES_BOOTS.get()), Ingredient.of(TCItems.ROCKET_BOOTS.get()));
        recipe(writer, "spectre_boots_from_sailfish_boots", TCItems.SPECTRE_BOOTS.get().getDefaultInstance(), Ingredient.of(TCItems.SAILFISH_BOOTS.get()), Ingredient.of(TCItems.ROCKET_BOOTS.get()));
        recipe(writer, "stalkers_quiver", TCItems.STALKERS_QUIVER.get().getDefaultInstance(), Ingredient.of(TCItems.PUTRID_SCENT.get()), Ingredient.of(TCItems.MAGIC_QUIVER.get()));
        recipe(writer, "star_veil", TCItems.STAR_VEIL.get().getDefaultInstance(), Ingredient.of(TCItems.STAR_CLOAK.get()), Ingredient.of(TCItems.CROSS_NECKLACE.get()));
        recipe(writer, "step_stool", TCItems.STEP_STOOL.get().getDefaultInstance(), AmountIngredient.of(2, Items.STICK), AmountIngredient.of(5, Items.SCAFFOLDING));
        recipe(writer, "stinger_necklace", TCItems.STINGER_NECKLACE.get().getDefaultInstance(), Ingredient.of(TCItems.HONEY_COMB.get()), Ingredient.of(TCItems.SHARK_TOOTH_NECKLACE.get()));
        recipe(writer, "sweetheart_necklace", TCItems.SWEETHEART_NECKLACE.get().getDefaultInstance(), Ingredient.of(TCItems.PANIC_NECKLACE.get()), Ingredient.of(TCItems.HONEY_COMB.get()));
        recipe(writer, "terraspark_boots", TCItems.TERRASPARK_BOOTS.get().getDefaultInstance(), Ingredient.of(TCItems.FROSTSPARK_BOOTS.get()), Ingredient.of(TCItems.LAVA_WADERS.get()));
        recipe(writer, "the_plan", TCItems.THE_PLAN.get().getDefaultInstance(), Ingredient.of(TCItems.FAST_CLOCK.get()), Ingredient.of(TCItems.TRIFOLD_MAP.get()));
        recipe(writer, "tiger_climbing_gear", TCItems.TIGER_CLIMBING_GEAR.get().getDefaultInstance(), Ingredient.of(TCItems.CLIMBING_CLAWS.get()), Ingredient.of(TCItems.SHOE_SPIKES.get()));
        recipe(writer, "white_horseshoe_balloon", TCItems.WHITE_HORSESHOE_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.BLIZZARD_IN_A_BALLOON.get()), Ingredient.of(TCItems.LUCKY_HORSESHOE.get()));
        recipe(writer, "yellow_horseshoe_balloon", TCItems.YELLOW_HORSESHOE_BALLOON.get().getDefaultInstance(), Ingredient.of(TCItems.SANDSTORM_IN_A_BALLOON.get()), Ingredient.of(TCItems.LUCKY_HORSESHOE.get()));
    }

    protected void recipe(Consumer<FinishedRecipe> writer, ItemStack result, Ingredient... ingredients) {
        recipe(writer, result, EnvironmentLevelAccess.Matcher.EMPTY, ingredients);
    }

    protected void recipe(Consumer<FinishedRecipe> writer, ItemStack result, EnvironmentLevelAccess.Matcher environment, Ingredient... ingredients) {
        WorkshopRecipe recipe = new WorkshopRecipe(result, NonNullList.of(Ingredient.EMPTY, ingredients), environment);
        ResourceLocation id = TerraCurio.asResource(getItemName(result.getItem()));
        Advancement.Builder advancement = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);
        writer.accept(new FinishedRecipe() {
            private static final Codec<WorkshopRecipe> CODEC = WorkshopRecipe.Serializer.CODEC.codec();

            @Override
            public void serializeRecipeData(JsonObject json) {
                CODEC.encodeStart(JsonOps.INSTANCE, recipe).result().ifPresent(element -> {
                    if (element.isJsonObject()) {
                        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                            json.add(entry.getKey(), entry.getValue());
                        }
                    }
                });
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return TCRecipes.WORKSHOP_SERIALIZER.get();
            }

            @Override
            public JsonObject serializeAdvancement() {
                return advancement.serializeToJson();
            }

            @Override
            public ResourceLocation getAdvancementId() {
                return id.withPrefix("recipes/terra_curio/");
            }
        });
    }

    private void recipe(Consumer<FinishedRecipe> writer, String name, ItemStack result, Ingredient... ingredients) {
        recipe(writer, name, result, EnvironmentLevelAccess.Matcher.EMPTY, ingredients);
    }

    private void recipe(Consumer<FinishedRecipe> writer, String name, ItemStack result, EnvironmentLevelAccess.Matcher environment, Ingredient... ingredients) {
        WorkshopRecipe recipe = new WorkshopRecipe(result, NonNullList.of(Ingredient.EMPTY, ingredients), environment);
        ResourceLocation id = TerraCurio.asResource(name);
        Advancement.Builder advancement = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);
        writer.accept(new FinishedRecipe() {
            private static final Codec<WorkshopRecipe> CODEC = WorkshopRecipe.Serializer.CODEC.codec();

            @Override
            public void serializeRecipeData(JsonObject json) {
                CODEC.encodeStart(JsonOps.INSTANCE, recipe).result().ifPresent(element -> {
                    if (element.isJsonObject()) {
                        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                            json.add(entry.getKey(), entry.getValue());
                        }
                    }
                });
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return TCRecipes.WORKSHOP_SERIALIZER.get();
            }

            @Override
            public JsonObject serializeAdvancement() {
                return advancement.serializeToJson();
            }

            @Override
            public ResourceLocation getAdvancementId() {
                return id.withPrefix("recipes/terra_curio/");
            }
        });
    }
}
