package org.confluence.terra_curio.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.Level;
import org.confluence.lib.util.LibStreamCodecUtils;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.init.TCRecipes;
import org.confluence.terra_curio.common.item.curio.movement.StepStool;

import java.util.Optional;

public class ExtraStepStoolRecipe extends SmithingTransformRecipe {
    public static final int MAX_STEP = 8;
    public static final RecipeSerializer<ExtraStepStoolRecipe> SERIALIZER = new RecipeSerializer<>(MapCodec.unit(ExtraStepStoolRecipe::getInstance), LibStreamCodecUtils.unit(ExtraStepStoolRecipe::getInstance));
    private static ExtraStepStoolRecipe INSTANCE;

    private ExtraStepStoolRecipe() {
        super(
                new CommonInfo(false),
                Optional.empty(),
                Ingredient.of(TCItems.STEP_STOOL.get(), TCItems.HAND_OF_CREATION.get()),
                Optional.of(Ingredient.of(TCItems.STEP_STOOL.get())),
                new ItemStackTemplate(TCItems.STEP_STOOL)
        );
    }

    @Override
    public RecipeSerializer<SmithingTransformRecipe> getSerializer() {
        return (RecipeSerializer) TCRecipes.EXTRA_STEP_STOOL_SERIALIZER.get();
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        if (input.base().getItem() instanceof StepStool &&
                LibUtils.getItemStackNbtNoCopy(input.base()).getIntOr("extraStep", 0) < MAX_STEP &&
                Ingredient.testOptionalIngredient(this.additionIngredient(), input.addition())
        ) {
            CompoundTag tag = LibUtils.getItemStackNbtNoCopy(input.base());
            return tag.getIntOr("extraStep", 0) + tag.getIntOr("extraStep", 0) < MAX_STEP;
        }
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input) {
        ItemStack base = input.getItem(1).copyWithCount(1);
        int additional = LibUtils.getItemStackNbtNoCopy(input.getItem(2)).getIntOr("extraStep", 0);
        LibUtils.updateItemStackNbt(base, nbt -> nbt.putInt("extraStep", nbt.getIntOr("extraStep", 0) + additional + 1));
        return base;
    }

    public static ExtraStepStoolRecipe getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExtraStepStoolRecipe();
        }
        return INSTANCE;
    }
}
