package org.confluence.mod.terra_curio.common.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.confluence.mod.terra_curio.TerraCurio;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TerraCurio.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        boolean client = event.includeClient();
//        generator.addProvider(client, new ModChineseProvider(output));
//        generator.addProvider(client, new ModEnglishProvider(output));
        generator.addProvider(client, new ModItemModelProvider(output, helper));

        boolean server = event.includeServer();
        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, lookup, helper);
        generator.addProvider(server, blockTagsProvider);
        generator.addProvider(server, new ModItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(server, new ModLanguageProvider(output, "en_us"));
        generator.addProvider(server, new ModLanguageProvider(output, "zh_cn"));
    }
}