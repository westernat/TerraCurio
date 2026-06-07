package org.confluence.terra_curio.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.lib.common.data.gen.CollectRecipeProvider;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCDamageTypes;

import java.util.Set;
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

        boolean server = event.includeServer();
        CompletableFuture<HolderLookup.Provider> lookup = generator.addProvider(server, new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, TCDamageTypes::bootstrap), Set.of(TerraCurio.MODID))).getRegistryProvider();
        TCBlockTagsProvider blockTagsProvider = generator.addProvider(server, new TCBlockTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TCItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(server, new TCEntityTypeTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TCFluidTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TCLanguageProvider(output, "en_us"));
        generator.addProvider(server, new TCLanguageProvider(output, "zh_cn"));
        generator.addProvider(server, new CollectRecipeProvider(TerraCurio.MODID, output,
                WorkshopProvider::new,
                TCShapedRecipeProvider::new,
                TCExtraStepStoolRecipeProvider::new
        ));
        generator.addProvider(server, new TCLootTableProvider(output));
        generator.addProvider(server, new TCGlobalLootModifierProvider(output));
    }
}
