package org.confluence.terra_curio.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.confluence.lib.common.data.gen.AbstractRecipeProvider;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCItems;

import java.util.function.Consumer;

public class TCShapedRecipeProvider extends AbstractRecipeProvider implements IConditionBuilder {
    public TCShapedRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        anklet_of_the_wind(writer);
        blizzard_in_a_bottle(writer);
        copper_watch(writer);
        dps_meter(writer);
        gold_watch(writer);
        ice_skates(writer);
        magic_mirror_from_gold_ingot(writer);
        magic_mirror_from_platinum_ingot(writer);
        obsidian_skull(writer);
        platinum_watch(writer);
        rocket_boots(writer);
        silver_watch(writer);
        stopwatch(writer);
        tin_watch(writer);
        tungsten_watch(writer);
        workshop(writer);
    }

    private static void anklet_of_the_wind(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.ANKLET_OF_THE_WIND.get())
                .pattern(" # ")
                .pattern("#S#")
                .pattern(" # ")
                .define('#', Items.VINE)
                .define('S', Items.SPORE_BLOSSOM)
                .unlockedBy("has", has(TCItems.ANKLET_OF_THE_WIND.get()))
                .save(writer, TerraCurio.asResource("anklet_of_the_wind"));
    }

    private static void blizzard_in_a_bottle(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.BLIZZARD_IN_A_BOTTLE.get())
                .pattern(" # ")
                .pattern("#S#")
                .pattern(" # ")
                .define('#', Items.BLUE_ICE)
                .define('S', TCItems.CLOUD_IN_A_BOTTLE.get())
                .unlockedBy("has", has(TCItems.BLIZZARD_IN_A_BOTTLE.get()))
                .save(writer, TerraCurio.asResource("blizzard_in_a_bottle"));
    }

    private static void copper_watch(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.COPPER_WATCH.get())
                .pattern(" # ")
                .pattern("cRc")
                .pattern(" c ")
                .define('#', Items.CHAIN)
                .define('c', Items.COPPER_INGOT)
                .define('R', Items.REDSTONE)
                .unlockedBy("has", has(TCItems.COPPER_WATCH.get()))
                .save(writer, TerraCurio.asResource("copper_watch"));
    }

    private static void dps_meter(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.DPS_METER.get())
                .pattern(" i ")
                .pattern("iti")
                .pattern(" i ")
                .define('i', Items.IRON_INGOT)
                .define('t', Items.TARGET)
                .unlockedBy("has", has(TCItems.DPS_METER.get()))
                .save(writer, TerraCurio.asResource("dps_meter"));
    }

    private static void gold_watch(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.GOLD_WATCH.get())
                .pattern(" # ")
                .pattern("cRc")
                .pattern(" c ")
                .define('#', Items.CHAIN)
                .define('c', Items.GOLD_INGOT)
                .define('R', Items.REDSTONE)
                .unlockedBy("has", has(TCItems.GOLD_WATCH.get()))
                .save(writer, TerraCurio.asResource("gold_watch"));
    }

    private static void ice_skates(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.ICE_SKATES.get())
                .pattern(" # ")
                .pattern("#S#")
                .pattern(" # ")
                .define('#', Items.BLUE_ICE)
                .define('S', TCItems.HERMES_BOOTS.get())
                .unlockedBy("has", has(TCItems.ICE_SKATES.get()))
                .save(writer, TerraCurio.asResource("ice_skates"));
    }

    private static void magic_mirror_from_gold_ingot(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.MAGIC_MIRROR.get())
                .pattern("gGg")
                .pattern("GDG")
                .pattern("gGg")
                .define('g', Items.GOLD_INGOT)
                .define('G', Items.GLASS)
                .define('D', Items.DIAMOND)
                .unlockedBy("has", has(TCItems.MAGIC_MIRROR.get()))
                .save(writer, TerraCurio.asResource("magic_mirror_from_gold_ingot"));
    }

    private static void magic_mirror_from_platinum_ingot(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.MAGIC_MIRROR.get())
                .pattern("gGg")
                .pattern("GDG")
                .pattern("gGg")
                .define('g', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/platinum")))
                .define('G', Items.GLASS)
                .define('D', Items.DIAMOND)
                .unlockedBy("has", has(TCItems.MAGIC_MIRROR.get()))
                .save(writer, TerraCurio.asResource("magic_mirror_from_platinum_ingot"));
    }

    private static void obsidian_skull(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.OBSIDIAN_SKULL.get())
                .pattern("###")
                .pattern("#W#")
                .pattern("###")
                .define('#', Items.OBSIDIAN)
                .define('W', Items.WITHER_SKELETON_SKULL)
                .unlockedBy("has", has(TCItems.OBSIDIAN_SKULL.get()))
                .save(writer, TerraCurio.asResource("obsidian_skull"));
    }

    private static void platinum_watch(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.PLATINUM_WATCH.get())
                .pattern(" # ")
                .pattern("cRc")
                .pattern(" c ")
                .define('#', Items.CHAIN)
                .define('c', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/platinum")))
                .define('R', Items.REDSTONE)
                .unlockedBy("has", has(TCItems.PLATINUM_WATCH.get()))
                .save(writer, TerraCurio.asResource("platinum_watch"));
    }

    private static void rocket_boots(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.ROCKET_BOOTS.get())
                .pattern("#I#")
                .pattern("i#i")
                .define('#', Items.FIRE_CHARGE)
                .define('I', Items.IRON_BOOTS)
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has", has(TCItems.ROCKET_BOOTS.get()))
                .save(writer, TerraCurio.asResource("rocket_boots"));
    }

    private static void silver_watch(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.SILVER_WATCH.get())
                .pattern(" # ")
                .pattern("cRc")
                .pattern(" c ")
                .define('#', Items.CHAIN)
                .define('c', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/silver")))
                .define('R', Items.REDSTONE)
                .unlockedBy("has", has(TCItems.SILVER_WATCH.get()))
                .save(writer, TerraCurio.asResource("silver_watch"));
    }

    private static void stopwatch(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.STOPWATCH.get())
                .pattern("ici")
                .pattern("iri")
                .pattern("iii")
                .define('c', Items.CHAIN)
                .define('r', Items.REDSTONE)
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has", has(TCItems.STOPWATCH.get()))
                .save(writer, TerraCurio.asResource("stopwatch"));
    }

    private static void tin_watch(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.TIN_WATCH.get())
                .pattern(" # ")
                .pattern("cRc")
                .pattern(" c ")
                .define('#', Items.CHAIN)
                .define('c', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/tin")))
                .define('R', Items.REDSTONE)
                .unlockedBy("has", has(TCItems.TIN_WATCH.get()))
                .save(writer, TerraCurio.asResource("tin_watch"));
    }

    private static void tungsten_watch(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.TUNGSTEN_WATCH.get())
                .pattern(" # ")
                .pattern("cRc")
                .pattern(" c ")
                .define('#', Items.CHAIN)
                .define('c', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/tungsten")))
                .define('R', Items.REDSTONE)
                .unlockedBy("has", has(TCItems.TUNGSTEN_WATCH.get()))
                .save(writer, TerraCurio.asResource("tungsten_watch"));
    }

    private static void workshop(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCItems.WORKSHOP.get())
                .pattern("aba")
                .pattern("ada")
                .pattern("c c")
                .define('a', Items.RED_WOOL)
                .define('b', Items.CRAFTING_TABLE)
                .define('c', Items.STICK)
                .define('d', Items.BOOKSHELF)
                .unlockedBy("has", has(TCItems.WORKSHOP.get()))
                .save(writer, TerraCurio.asResource("workshop"));
    }
}
