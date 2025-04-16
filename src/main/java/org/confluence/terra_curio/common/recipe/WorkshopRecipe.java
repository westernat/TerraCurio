package org.confluence.terra_curio.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.confluence.lib.common.recipe.AbstractAmountRecipe;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.common.init.TCBlocks;
import org.confluence.terra_curio.common.init.TCRecipes;

public class WorkshopRecipe extends AbstractAmountRecipe<RecipeInput> {
    public WorkshopRecipe(ItemStack pResult, NonNullList<Ingredient> pIngredients) {
        super(pResult, pIngredients);
    }

    @Override
    public boolean matches(RecipeInput input, Level pLevel) {
        return LibUtils.forConfluence$ModifyExpression(super.matches(input, pLevel));
    }

    @Override
    protected int maxIngredientSize() {
        return 12;
    }

    @Override
    public String getGroup() {
        return "workshop";
    }

    @Override
    public ItemStack getToastSymbol() {
        return TCBlocks.WORKSHOP.toStack();
    }

    @Override
    public RecipeSerializer<WorkshopRecipe> getSerializer() {
        return TCRecipes.WORKSHOP_SERIALIZER.get();
    }

    @Override
    public RecipeType<WorkshopRecipe> getType() {
        return TCRecipes.WORKSHOP_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<WorkshopRecipe> {
        public static final MapCodec<WorkshopRecipe> CODEC = shapelessSerializerMapCodec(WorkshopRecipe::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, WorkshopRecipe> STREAM_CODEC = shapelessSerializerSteamCodec(WorkshopRecipe::new);

        @Override
        public MapCodec<WorkshopRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WorkshopRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
