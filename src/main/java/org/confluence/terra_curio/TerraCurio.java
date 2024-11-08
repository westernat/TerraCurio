package org.confluence.terra_curio;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.confluence.terra_curio.common.init.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraCurio.MODID)
public class TerraCurio {
    public static final String MODID = "terra_curio";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Curio");
    private static Boolean isConfluenceLoaded;

    public TerraCurio(IEventBus eventBus, ModContainer modContainer) {
        TCStartupConfigs.register(modContainer);
        TCCommonConfigs.register(modContainer);
        TCSoundEvents.SOUNDS.register(eventBus);
        TCEffects.EFFECTS.register(eventBus);
        TCAttributes.ATTRIBUTES.register(eventBus);
        TCEntities.ENTITIES.register(eventBus);
        TCDataComponentTypes.TYPES.register(eventBus);
        TCItems.register(eventBus);
        TCTabs.TABS.register(eventBus);
        TCAttachments.TYPES.register(eventBus);
        TCTriggers.TYPES.register(eventBus);
        TCRecipes.register(eventBus);
        TCBlocks.BLOCKS.register(eventBus);
        TCMenus.TYPES.register(eventBus);
        eventBus.addListener(TCDataMaps::registerDataMapTypes);
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
