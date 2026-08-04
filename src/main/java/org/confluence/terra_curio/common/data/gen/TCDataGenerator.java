package org.confluence.terra_curio.common.data.gen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.confluence.terra_curio.common.init.TCDamageTypes;

import static org.confluence.terra_curio.TerraCurio.MODID;

@EventBusSubscriber(modid = MODID)
public final class TCDataGenerator {
    @SubscribeEvent
    public static void gatherData$Client(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        event.createDatapackRegistryObjects(new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, TCDamageTypes::bootstrap)
        );

        event.createProvider(TCModelProvider::new);
        event.createProvider(TCBlockTagsProvider::new);
        event.createProvider(TCItemTagsProvider::new);
        event.createProvider(TCRecipeProvider.Runner::new);
        event.createProvider(TCDataMapProvider::new);
        event.createProvider(TCFluidTagsProvider::new);
        event.createProvider(TCLootTableProvider::new);
        event.createProvider(TCGlobalLootModifierProvider::new);

        generator.addProvider(true, new TCLanguageProvider(output, "en_us"));
        generator.addProvider(true, new TCLanguageProvider(output, "zh_cn"));
    }
}
