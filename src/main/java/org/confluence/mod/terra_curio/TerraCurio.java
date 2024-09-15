package org.confluence.mod.terra_curio;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.confluence.mod.terra_curio.common.config.ModConfig;
import org.confluence.mod.terra_curio.common.data.pack.CurioItemManager;
import org.confluence.mod.terra_curio.common.effect.ModEffects;
import org.confluence.mod.terra_curio.common.entity.ModEntities;
import org.confluence.mod.terra_curio.common.init.ModDataComponentTypes;
import org.confluence.mod.terra_curio.common.init.ModItems;
import org.confluence.mod.terra_curio.common.misc.ModAttributes;
import org.confluence.mod.terra_curio.common.misc.ModDamageTypes;
import org.confluence.mod.terra_curio.common.misc.ModSoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@Mod(TerraCurio.MOD_ID)
public class TerraCurio {
    public static final String MOD_ID = "terra_curio";
    public static final Logger LOGGER = LoggerFactory.getLogger("Confluence");
    public static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("confluence");
    public static ResourceLocation SPACE(String path) {return ResourceLocation.fromNamespaceAndPath(MOD_ID,path);}

    public TerraCurio(IEventBus modEventBus, ModContainer modContainer) {
//      NeoForge.EVENT_BUS.register(this);
        //

        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);

        NeoForge.EVENT_BUS.addListener(this::onDataPackLoad);

        ModSoundEvents.SOUNDS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModDataComponentTypes.DATA_COMPONENT_TYPE.register(modEventBus);

        ModItems.ITEMS.register(modEventBus);

        ModItems.Tab.CREATIVE_MODE_TAB.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public void onDataPackLoad(AddReloadListenerEvent event) {
        event.addListener(CurioItemManager.INSTANCE);
    }
}
