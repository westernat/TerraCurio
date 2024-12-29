package org.confluence.terra_curio.common.data.gen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractRecipeProvider implements DataProvider {
    protected PackOutput output;
    private  final List<tuple> jsons = new ArrayList<>();
    private final List<CompletableFuture<?>> futures = new ArrayList<>();
    public AbstractRecipeProvider(PackOutput output) {
        this.output = output;
    }
    private record tuple(JsonObject json, ItemStack result, String suffix) {}
    abstract protected void run();

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        run();
        jsons.forEach(pair -> {
            var obj = pair.json;
            var result = pair.result;
            var suffix = pair.suffix;
            ResourceLocation loc = result.getItemHolder().getKey().location();
            Path path = getPath(loc, suffix);
            futures.add(DataProvider.saveStable(cachedOutput, obj, path));
        });
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }
    protected void addJson(JsonObject json, ItemStack result, String suffix) {
        int sameCount = 0;
        for(var pair : jsons){
            if(pair.result.getItem() == result.getItem() && pair.suffix.equals(suffix)){
                sameCount++;
            }
        }
        if(sameCount > 0){
            suffix = suffix + "_" + sameCount;
        }
        jsons.add(new tuple(json, result, suffix));
    }
    protected Path getPath(ResourceLocation loc, String nameSuffix) {
        return getRoot(loc).resolve(loc.getPath() + nameSuffix+"_gen"+pathSuffix()+".json");
    }

    protected String pathSuffix(){
        return "";
    }

    protected Path getRoot(ResourceLocation loc){
        return this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(loc.getNamespace()).resolve("recipe");
    }
}
