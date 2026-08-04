package org.confluence.terra_curio;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class TCStartupConfigs {
    private static ModConfigSpec.BooleanValue SHOES_EXTRA_STEP_HEIGHT;

    public static void register(ModContainer container) {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        SHOES_EXTRA_STEP_HEIGHT = builder.define("shoesExtraStepHeight", true);
        container.registerConfig(ModConfig.Type.STARTUP, builder.build());
    }

    public static boolean shoesExtraStepHeight() {
        return SHOES_EXTRA_STEP_HEIGHT == null || SHOES_EXTRA_STEP_HEIGHT.get();
    }
}
