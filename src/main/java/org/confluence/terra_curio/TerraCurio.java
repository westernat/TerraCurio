package org.confluence.terra_curio;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.client.TCClientConfigs;
import org.confluence.terra_curio.client.event.TCGameClientEvents;
import org.confluence.terra_curio.client.event.TCModClientEvent;
import org.confluence.terra_curio.common.TCCommonConfigs;
import org.confluence.terra_curio.common.attachment.AccessoriesValueCommand;
import org.confluence.terra_curio.common.event.TCGameEvents;
import org.confluence.terra_curio.common.event.TCModEvents;
import org.confluence.terra_curio.common.init.*;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraCurio.MODID)
public class TerraCurio {
    public static final String MODID = "terra_curio";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Curio");
    public static final String CURIO_SLOT = "accessory";
    @Diff
    public static final PortNetworkHandler NETWORK_HANDLER = new PortNetworkHandler(MODID, "1");

    public TerraCurio(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();
        TCStartupConfigs.register();
        TCCommonConfigs.register(context);
        TCModEvents.init();
        TCGameEvents.init();
        if (LibUtils.isPhysicalClient()) {
            TCModClientEvent.init();
            TCGameClientEvents.init();
            TCClientConfigs.register(context);
//            container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, ConfigurationScreen::new);
        }
        TCAttachments.init();
        TCDataComponentTypes.init();
        TCArmorMaterials.init();
        TCSoundEvents.SOUNDS.register(eventBus);
        TCEffects.EFFECTS.register(eventBus);
        TCEntities.ENTITIES.register(eventBus);
        TCItems.init();
        TCTabs.TABS.register(eventBus);
        TCRecipes.register(eventBus);
        TCBlocks.BLOCKS.register(eventBus);
        TCMenus.TYPES.register(eventBus);
        AccessoriesValueCommand.INFOS.register(eventBus);
        eventBus.addListener(TCDataMaps::registerDataMapTypes);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
