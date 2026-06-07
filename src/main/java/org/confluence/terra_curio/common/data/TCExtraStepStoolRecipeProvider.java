package org.confluence.terra_curio.common.data;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.confluence.lib.common.data.gen.AbstractRecipeProvider;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCRecipes;

import java.util.function.Consumer;

public class TCExtraStepStoolRecipeProvider extends AbstractRecipeProvider {
    public TCExtraStepStoolRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        extraStepStool(writer, "extra_step_stool");
        extraStepStool(writer, "extra_hand_of_creation");
    }

    private void extraStepStool(Consumer<FinishedRecipe> writer, String name) {
        ResourceLocation id = TerraCurio.asResource(name);
        Advancement.Builder advancement = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);
        writer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                json.addProperty("type", "terra_curio:extra_step_stool");
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return TCRecipes.EXTRA_STEP_STOOL_SERIALIZER.get();
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
