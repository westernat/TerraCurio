package org.confluence.terra_curio;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.confluence.terra_curio.block.ModBlocks;
import org.confluence.terra_curio.client.ClientConfigs;
import org.confluence.terra_curio.client.particle.ModParticles;
import org.confluence.terra_curio.effect.ModEffects;
import org.confluence.terra_curio.entity.ModEntities;
import org.confluence.terra_curio.item.ModItems;
import org.confluence.terra_curio.item.ModTabs;
import org.confluence.terra_curio.loot.ModLootModifiers;
import org.confluence.terra_curio.menu.ModMenus;
import org.confluence.terra_curio.misc.ModAttributes;
import org.confluence.terra_curio.misc.ModConfigs;
import org.confluence.terra_curio.misc.ModSoundEvents;
import org.confluence.terra_curio.recipe.ModRecipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@SuppressWarnings("unused")
@Mod(TerraCurio.MODID)
public final class TerraCurio {
    public static final String MODID = "terra_curio";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Curio");
    public static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("terra_curio");

    public TerraCurio() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfigs.SPEC);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(bus);
        ModRecipes.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModTabs.TABS.register(bus);
        ModAttributes.ATTRIBUTES.register(bus);
        ModEffects.EFFECTS.register(bus);
        ModSoundEvents.SOUNDS.register(bus);
        ModLootModifiers.MODIFIERS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModParticles.PARTICLES.register(bus);
        ModMenus.TYPES.register(bus);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
