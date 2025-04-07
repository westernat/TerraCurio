package org.confluence.terra_curio.common.data.gen;

import com.google.gson.*;
import com.mojang.serialization.JavaOps;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.confluence.lib.common.recipe.AmountIngredient;
import org.confluence.terra_curio.TerraCurio;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class WorkshopRecipeProvider extends AbstractRecipeProvider {
    private final String modid;
    private final String recipeType;
    private final String amountIngredientType;

    public WorkshopRecipeProvider(String modid, String recipeType, String amountIngredientType, PackOutput output) {
        super(output);
        this.modid = modid;
        this.recipeType = recipeType;
        this.amountIngredientType = amountIngredientType;
        this.output = output;
    }

    public WorkshopRecipeProvider(PackOutput output) {
        this(TerraCurio.MODID, "workshop", "amount_ingredient", output);
    }

    protected void run() {

//         Example usage:
//        gen(TCItems.TERRASPARK_BOOTS)
//                .add(Items.GRASS_BLOCK,5)
//                .build();

    }

    protected String pathSuffix(){
        return "_workshop";
    }

    @Override
    public String getName() {
        return "Workshop Recipe Provider: " + modid;
    }

    protected void genRecipe(Supplier<ItemStack> result, List<AmountIngredient> ingredients, String suffix) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", modid + ":" + recipeType);

        JsonArray arr = new JsonArray();
        for (AmountIngredient i : ingredients) {
            JsonElement ingres;
            if (i.amount() > 1) {
                var ing = AmountIngredient.CODEC.encoder().encodeStart(JavaOps.INSTANCE, i).result().get();
                ingres = JsonParser.parseString(new Gson().toJson(ing));
                ingres.getAsJsonObject().addProperty("type", modid + ":" + amountIngredientType);
            } else if (i.amount() == 1) {
                var ing = Ingredient.CODEC.encodeStart(JavaOps.INSTANCE, i.ingredient()).result().get();
                ingres = JsonParser.parseString(new Gson().toJson(ing));
            } else continue;
            arr.add(ingres);
        }
        obj.add("ingredients", arr);

        var a = ItemStack.CODEC.encodeStart(JavaOps.INSTANCE, result.get()).result().get();
        JsonElement resit = JsonParser.parseString(new Gson().toJson(a));
        obj.add("result", resit);

        addJson(obj, result.get(),suffix);
//        futures.add(DataProvider.saveStable(cachedOutput,obj, getPath(result.get().getItemHolder().getKey().location())));
    }


    private AmountIngredientBuilder gen(Supplier<? extends Item> result, int count) {
        return new AmountIngredientBuilder(() -> new ItemStack(result.get(), count));
    }

    private AmountIngredientBuilder gen(Supplier<? extends Item> result) {
        return gen(result, 1);
    }

    private final class AmountIngredientBuilder {
        Supplier<ItemStack> result;
        List<AmountIngredient> ingredients = new ArrayList<>();

        public AmountIngredientBuilder(Supplier<ItemStack> result) {
            this.result = result;
        }


        public AmountIngredientBuilder add(Ingredient ingredient) {
            ingredients.add(new AmountIngredient(ingredient, 1));
            return this;
        }

        public AmountIngredientBuilder add(Ingredient ingredient, int amount) {
            ingredients.add(new AmountIngredient(ingredient, amount));
            return this;
        }

        public AmountIngredientBuilder add(ItemLike ingredient) {
            ingredients.add(new AmountIngredient(Ingredient.of(ingredient), 1));
            return this;
        }

        public AmountIngredientBuilder add(ItemLike ingredient, int amount) {
            ingredients.add(new AmountIngredient(Ingredient.of(ingredient), amount));
            return this;
        }

        public void build(String suffix) {
            genRecipe(result, ingredients, suffix);
        }
        public void build() {
            genRecipe(result, ingredients, "");
        }
    }
}
