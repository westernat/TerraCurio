package org.confluence.terra_curio.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terra_curio.TerraCurio;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TerraCurio.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TCDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean client = event.includeClient();
        generator.addProvider(client, new TCItemModelProvider(output, helper));
        generator.addProvider(client, new TCLanguageProvider(output, true));
        generator.addProvider(client, new TCLanguageProvider(output, false));

        boolean server = event.includeServer();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        TCBlockTagsProvider blockTagsProvider = generator.addProvider(server, new TCBlockTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TCItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(server, new TCFluidTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TCRecipeProvider(output));
        generator.addProvider(server, new TCLootTableProvider(output));
        generator.addProvider(server, new TCGlobalLootModifierProvider(output));
        generator.addProvider(server, new TCDataMapProvider(output, lookup));
    }
}
