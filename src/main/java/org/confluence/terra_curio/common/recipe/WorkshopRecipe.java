package org.confluence.terra_curio.common.recipe;

import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import org.confluence.lib.common.recipe.EnvironmentAmountRecipe;
import org.confluence.lib.common.recipe.EnvironmentLevelAccess;
import org.confluence.terra_curio.common.init.TCRecipes;

import java.util.List;
import java.util.Optional;

public class WorkshopRecipe extends EnvironmentAmountRecipe {
    public static final RecipeSerializer<WorkshopRecipe> SERIALIZER = new RecipeSerializer<>(WorkshopRecipe.environmentShapelessSerializerMapCodec(WorkshopRecipe::new), WorkshopRecipe.environmentShapelessSerializerSteamCodec(WorkshopRecipe::new));

    public WorkshopRecipe(ItemStackTemplate result, List<Optional<Ingredient>> ingredients, EnvironmentLevelAccess.Matcher environment) {
        super(result, ingredients, environment);
    }

    @Override
    protected int maxIngredientSize() {
        return 12;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "workshop";
    }

    @Override
    public RecipeSerializer<WorkshopRecipe> getSerializer() {
        return TCRecipes.WORKSHOP_SERIALIZER.get();
    }

    @Override
    public RecipeType<WorkshopRecipe> getType() {
        return TCRecipes.WORKSHOP_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
