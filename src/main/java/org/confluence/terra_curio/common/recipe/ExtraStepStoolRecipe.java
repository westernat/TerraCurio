package org.confluence.terra_curio.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.Level;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.item.curio.movement.StepStool;
import org.confluence.terra_curio.mixin.accessor.SmithingTransformRecipeAccessor;

public class ExtraStepStoolRecipe extends SmithingTransformRecipe {
    public ExtraStepStoolRecipe(Ingredient base, Ingredient addition, ItemStack result) {
        super(Ingredient.EMPTY, Ingredient.of(TCItems.STEP_STOOL.get(), TCItems.HAND_OF_CREATION.get()), Ingredient.of(TCItems.STEP_STOOL.get()), TCItems.STEP_STOOL.get().getDefaultInstance());
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        ItemStack base = input.getItem(1);
        ItemStack addition = input.getItem(2);
        if (isBaseIngredient(base) && isAdditionIngredient(addition)) {
            CompoundTag tag = LibUtils.getItemStackNbt(base);
            return tag.getInt("extraStep") + tag.getInt("extraStep") < 15;
        }
        return false;
    }

    @Override
    public boolean isBaseIngredient(ItemStack pStack) {
        return pStack.getItem() instanceof StepStool && LibUtils.getItemStackNbt(pStack).getInt("extraStep") < 15;
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack base = input.getItem(1).copyWithCount(1);
        int additional = LibUtils.getItemStackNbt(input.getItem(2)).getInt("extraStep");
        LibUtils.updateItemStackNbt(base, nbt -> nbt.putInt("extraStep", nbt.getInt("extraStep") + additional + 1));
        return base;
    }

    public static class Serializer implements RecipeSerializer<ExtraStepStoolRecipe> {
        public static final MapCodec<ExtraStepStoolRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("base").forGetter(recipe -> ((SmithingTransformRecipeAccessor) recipe).getBase()),
                Ingredient.CODEC.fieldOf("addition").forGetter(recipe -> ((SmithingTransformRecipeAccessor) recipe).getAddition()),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> ((SmithingTransformRecipeAccessor) recipe).getResult())
        ).apply(instance, ExtraStepStoolRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, ExtraStepStoolRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<ExtraStepStoolRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ExtraStepStoolRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ExtraStepStoolRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient ingredient1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            return new ExtraStepStoolRecipe(ingredient1, ingredient2, itemstack);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ExtraStepStoolRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ((SmithingTransformRecipeAccessor) recipe).getBase());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ((SmithingTransformRecipeAccessor) recipe).getAddition());
            ItemStack.STREAM_CODEC.encode(buffer, ((SmithingTransformRecipeAccessor) recipe).getResult());
        }
    }
}
