package org.confluence.terra_curio.common.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.confluence.lib.ConfluenceMagicLib;

import java.util.concurrent.CompletableFuture;

import static org.confluence.terra_curio.TerraCurio.MODID;

@EventBusSubscriber(modid = MODID)
public final class TCDataGenerator {
    static final ICondition CONFLUENCE_NOT_LOADED = new NotCondition(new ModLoadedCondition(ConfluenceMagicLib.CONFLUENCE_ID));

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        boolean client = event.includeClient();
        generator.addProvider(client, new TCItemModelProvider(output, helper));

        boolean server = event.includeServer();
        TCBlockTagsProvider blockTagsProvider = new TCBlockTagsProvider(output, lookup, helper);
        generator.addProvider(server, blockTagsProvider);
        generator.addProvider(server, new TCItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(server, new TCLanguageProvider(output, true));
        generator.addProvider(server, new TCLanguageProvider(output, false));
        generator.addProvider(server, new TCDataMapProvider(output, lookup));
        generator.addProvider(server, new TCFluidTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TCGlobalLootModifierProvider(output, lookup));
        generator.addProvider(server, new TCLootTableProvider(output, lookup));
        generator.addProvider(server, new TCRecipeProvider(output, lookup));
    }
}
