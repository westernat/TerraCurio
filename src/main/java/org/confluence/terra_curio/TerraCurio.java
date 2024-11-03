package org.confluence.terra_curio;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import org.confluence.terra_curio.common.TCCommonConfigs;
import org.confluence.terra_curio.common.init.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@Mod(TerraCurio.MODID)
public class TerraCurio {
    public static final String MODID = "terra_curio";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Curio");
    public static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("confluence");
    private static Boolean isConfluenceLoaded;

    public TerraCurio(IEventBus modEventBus, ModContainer modContainer) {
        TCCommonConfigs.register(modContainer);
        TCSoundEvents.SOUNDS.register(modEventBus);
        TCEffects.EFFECTS.register(modEventBus);
        TCAttributes.ATTRIBUTES.register(modEventBus);
        TCEntities.ENTITIES.register(modEventBus);
        TCDataComponentTypes.TYPES.register(modEventBus);
        TCItems.ITEMS.register(modEventBus);
        TCTabs.TABS.register(modEventBus);
        TCAttachments.TYPES.register(modEventBus);
        TCTriggers.TYPES.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static boolean isConfluenceLoaded() {
        if (isConfluenceLoaded == null) {
            isConfluenceLoaded = ModList.get().isLoaded("confluence");
        }
        return isConfluenceLoaded;
    }
}
