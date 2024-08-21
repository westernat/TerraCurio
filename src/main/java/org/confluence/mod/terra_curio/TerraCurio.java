package org.confluence.mod.terra_curio;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(TerraCurio.MOD_ID)
public class TerraCurio {
    public static final String MOD_ID = "terra_curio";
    private static final Logger LOGGER = LogUtils.getLogger();
    public TerraCurio(IEventBus modEventBus, ModContainer modContainer) {
//        NeoForge.EVENT_BUS.register(this);
    }
}
