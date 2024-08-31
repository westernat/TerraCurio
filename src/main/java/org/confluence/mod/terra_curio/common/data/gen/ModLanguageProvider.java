package org.confluence.mod.terra_curio.common.data.gen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.init.ModItems;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModLanguageProvider extends LanguageProvider {
    private final Map<String, String> enData = new TreeMap<>();
    private final Map<String, String> cnData = new TreeMap<>();
    private final PackOutput output;
    private final String locale;

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, TerraCurio.MOD_ID, locale);
        this.output = output;
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.BEZOAR,"Bezoar","牛黄");
        addTooltips(ModItems.BEZOAR,"Immunity to Poison","对中毒免疫\nssss");
        addJeiTooltips(ModItems.BEZOAR, new String[]{"The Bezoar is an immunity accessory that grants the player immunity to the Poisoned debuff",
                        "It have a Chance to be dropped from Cave Spider."},
                new String[]{"牛黄是一种免疫配饰，可赋予玩家对中毒减益的免疫力", "它有几率从洞穴蜘蛛中掉落"});
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cache) {
        this.addTranslations();
        Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(TerraCurio.MOD_ID).resolve("lang");
        if (this.locale.equals("en_us") && !this.enData.isEmpty()) {
            return this.save(this.enData, cache, path.resolve("en_us.json"));
        }

        if (this.locale.equals("zh_cn") && !this.cnData.isEmpty()) {
            return this.save(this.cnData, cache, path.resolve("zh_cn.json"));
        }
        return CompletableFuture.allOf();
    }

    private CompletableFuture<?> save(Map<String, String> data, CachedOutput cache, Path target) {
        JsonObject json = new JsonObject();
        data.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, target);
    }

    private void addItem(Supplier<? extends Item> key, String en, String cn) {
        this.add(key.get().getDescriptionId(), en, cn);
    }

    private void addTooltips(Supplier<Item> key, String en, String cn) {
        this.add("tooltip." + key.get().getDescriptionId(), en, cn);
    }
    private void addJeiTooltips(Supplier<Item> key, String[] en, String[] cn) {
        if (en.length == cn.length){
            for (int i = 0; i < en.length; i++) {
                String enLang = en[i];
                String cnLang = cn[i];
                this.add("jei.tooltip." + key.get().getDescriptionId(), enLang, cnLang);
            }
        }
    }

    private void add(String key, String en, String cn) {
        if (this.locale.equals("en_us") && !this.enData.containsKey(key)) {
            this.enData.put(key, en);
        } else if (this.locale.equals("zh_cn") && !this.cnData.containsKey(key)) {
            this.cnData.put(key, cn);
        }
    }
}