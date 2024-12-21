package org.confluence.terra_curio.common.recipe;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.confluence.terra_curio.common.init.TCBlocks;
import org.confluence.terra_curio.common.init.TCRecipes;
import org.jetbrains.annotations.NotNull;

public class WorkshopRecipe extends AbstractAmountRecipe {
    public WorkshopRecipe(ItemStack pResult, NonNullList<Ingredient> pIngredients) {
        super(pResult, pIngredients);
    }

    @Override
    protected int maxIngredientSize() {
        return 12;
    }

    @Override
    public @NotNull String getGroup() {
        return "workshop";
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return TCBlocks.WORKSHOP.get().asItem().getDefaultInstance();
    }

    @Override
    public @NotNull RecipeSerializer<WorkshopRecipe> getSerializer() {
        return TCRecipes.WORKSHOP_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<WorkshopRecipe> getType() {
        return TCRecipes.WORKSHOP_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<WorkshopRecipe> {
        public static final MapCodec<WorkshopRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(list -> {
                    Ingredient[] ingredients = list.toArray(Ingredient[]::new);
                    if (ingredients.length == 0) {
                        return DataResult.error(() -> "No ingredients for workshop recipe");
                    } else {
                        return DataResult.success(NonNullList.of(AmountIngredient.EMPTY, ingredients));
                    }
                }, DataResult::success).forGetter(recipe -> recipe.ingredients)
        ).apply(instance, WorkshopRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, WorkshopRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public @NotNull MapCodec<WorkshopRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, WorkshopRecipe> streamCodec() {
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
