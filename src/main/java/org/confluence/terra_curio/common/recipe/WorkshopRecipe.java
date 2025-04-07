package org.confluence.terra_curio.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import org.confluence.lib.common.recipe.AmountIngredient;
import org.confluence.terra_curio.common.init.TCBlocks;
import org.confluence.terra_curio.common.init.TCRecipes;
import org.confluence.terra_curio.util.TCUtils;

public class WorkshopRecipe extends AbstractAmountRecipe<RecipeInput> {
    public WorkshopRecipe(ItemStack pResult, NonNullList<Ingredient> pIngredients) {
        super(pResult, pIngredients);
    }

    @Override
    public boolean matches(RecipeInput input, Level pLevel) {
        return TCUtils.forConfluence$ModifyExpression(super.matches(input, pLevel));
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
        public static final MapCodec<WorkshopRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                INGREDIENTS_CODEC.forGetter(recipe -> recipe.ingredients)
        ).apply(instance, WorkshopRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, WorkshopRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<WorkshopRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WorkshopRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static WorkshopRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(size, AmountIngredient.EMPTY);
            nonnulllist.replaceAll(ignore -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            return new WorkshopRecipe(itemstack, nonnulllist);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, WorkshopRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }
}
