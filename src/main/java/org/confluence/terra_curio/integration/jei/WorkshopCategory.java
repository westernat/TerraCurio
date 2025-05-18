package org.confluence.terra_curio.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.recipe.WorkshopRecipe;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WorkshopCategory implements IRecipeCategory<RecipeHolder<WorkshopRecipe>> {
    public static final RecipeType<RecipeHolder<WorkshopRecipe>> TYPE = RecipeType.createRecipeHolderType(TerraCurio.asResource("workshop"));
    private static final Component TITLE = Component.translatable("title.terra_curio.workshop");
    private final IDrawable icon;

    public WorkshopCategory(IJeiHelpers jeiHelpers) {
        this.icon = jeiHelpers.getGuiHelper().createDrawableItemStack(new ItemStack(TCItems.WORKSHOP.get()));
    }

    @Override
    public RecipeType<RecipeHolder<WorkshopRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @SuppressWarnings("removal")
    @Override
    public IDrawable getBackground() {
        return ModJeiPlugin.HALF_BACKGROUND;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<WorkshopRecipe> recipe, IFocusGroup focusGroup) {
        // input
        int size = recipe.value().getIngredients().size();
        int line = size / 3;
        boolean remain = size % 3 != 0;
        int x = 0;
        int y = 0;
        if (line == 0) {
            y = 24;
        } else if (line == 1) {
            y = remain ? 16 : 24;
        } else if (line == 2) {
            y = remain ? 8 : 16;
        } else if (line == 3) {
            y = remain ? 0 : 8;
        }
        for (Ingredient ingredient : recipe.value().getIngredients()) {
            ModJeiPlugin.addInput(builder, x, y, ingredient);
            x += 16;
            if (x == 48) {
                x = 0;
                y += 16;
            }
        }
        // output
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 24).addItemStack(recipe.value().getResultItem(null));
    }

    @Override
    public void draw(RecipeHolder<WorkshopRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        ModJeiPlugin.drawArrowRight(guiGraphics, 50, 22, true);
    }
}
