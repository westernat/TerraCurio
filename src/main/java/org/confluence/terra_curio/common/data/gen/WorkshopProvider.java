package org.confluence.terra_curio.common.data.gen;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.confluence.lib.common.data.gen.AbstractRecipeProvider;
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
    }

    protected void recipe(Consumer<FinishedRecipe> writer, ItemStack result, Ingredient... ingredients) {
        recipe(writer, result, EnvironmentLevelAccess.Matcher.EMPTY, ingredients);
    }

    protected void recipe(Consumer<FinishedRecipe> writer, ItemStack result, EnvironmentLevelAccess.Matcher environment, Ingredient... ingredients) {
        WorkshopRecipe recipe = new WorkshopRecipe(result, NonNullList.of(Ingredient.EMPTY, ingredients), EnvironmentLevelAccess.Matcher.EMPTY);
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
}
