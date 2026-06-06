package org.confluence.terra_curio;


import org.mesdag.portlib.config.PortConfigSpec;

public final class TCStartupConfigs {
    private static PortConfigSpec.BooleanValue SHOES_EXTRA_STEP_HEIGHT;

    public static void register() {
        PortConfigSpec.Builder builder = PortConfigSpec.builder(TerraCurio.MODID);
        SHOES_EXTRA_STEP_HEIGHT = builder.define("shoesExtraStepHeight", true);
        PortConfigSpec spec = builder.build();
        spec.load();
    }

    public static boolean shoesExtraStepHeight() {
        return SHOES_EXTRA_STEP_HEIGHT == null || SHOES_EXTRA_STEP_HEIGHT.get();
    }
}
