package org.confluence.terra_curio.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.Level;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.init.TCRecipes;
import org.confluence.terra_curio.common.item.curio.movement.StepStool;
import org.mesdag.portlib.diff.Diff;

public class ExtraStepStoolRecipe extends SmithingTransformRecipe {
    @Diff
    public static final ResourceLocation ID = TerraCurio.asResource("extra_step_stool");
    public static final int MAX_STEP = 8;
    private static ExtraStepStoolRecipe INSTANCE;

    private ExtraStepStoolRecipe() {
        super(ID, Ingredient.EMPTY, Ingredient.of(TCItems.STEP_STOOL.get(), TCItems.HAND_OF_CREATION.get()), Ingredient.of(TCItems.STEP_STOOL.get()), TCItems.STEP_STOOL.get().getDefaultInstance());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TCRecipes.EXTRA_STEP_STOOL_SERIALIZER.get();
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack base = container.getItem(1);
        ItemStack addition = container.getItem(2);
        if (isBaseIngredient(base) && isAdditionIngredient(addition)) {
            CompoundTag tag = LibUtils.getItemStackNbtNoCopy(base);
            return tag.getInt("extraStep") + tag.getInt("extraStep") < MAX_STEP;
        }
        return false;
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return stack.getItem() instanceof StepStool && LibUtils.getItemStackNbtNoCopy(stack).getInt("extraStep") < MAX_STEP;
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(Container input, RegistryAccess registryAccess) {
        ItemStack base = input.getItem(1).copyWithCount(1);
        int additional = LibUtils.getItemStackNbtNoCopy(input.getItem(2)).getInt("extraStep");
        LibUtils.updateItemStackNbt(base, nbt -> nbt.putInt("extraStep", nbt.getInt("extraStep") + additional + 1));
        return base;
    }

    public static ExtraStepStoolRecipe getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExtraStepStoolRecipe();
        }
        return INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<ExtraStepStoolRecipe> {
        @Override
        public ExtraStepStoolRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            return getInstance();
        }

        @Override
        public ExtraStepStoolRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return getInstance();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ExtraStepStoolRecipe recipe) {}
    }
}
