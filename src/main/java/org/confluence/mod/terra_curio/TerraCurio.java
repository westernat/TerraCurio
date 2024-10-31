package org.confluence.mod.terra_curio;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import org.confluence.mod.terra_curio.common.CommonConfigs;
import org.confluence.mod.terra_curio.common.advancement.ModTriggers;
import org.confluence.mod.terra_curio.common.effect.ModEffects;
import org.confluence.mod.terra_curio.common.entity.ModEntities;
import org.confluence.mod.terra_curio.common.init.*;
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
        CommonConfigs.register(modContainer);
        ModSoundEvents.SOUNDS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        TCDataComponentTypes.TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModTabs.TABS.register(modEventBus);
        ModAttachments.TYPES.register(modEventBus);
        ModTriggers.TYPES.register(modEventBus);
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
