package org.confluence.terra_curio.common.init;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class TCStartupConfigs {
    public static ModConfigSpec.ConfigValue<List<? extends String>> ATTRIBUTE_REPLACE;

    private static void onLoad() {}

    public static void register(ModContainer container) {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        ATTRIBUTE_REPLACE = BUILDER.defineListAllowEmpty("attributeReplacements", List.of(), TCCommonConfigs.STRING_SUPPLIER,  o -> true);

        container.registerConfig(ModConfig.Type.STARTUP, BUILDER.build());
        onLoad();
    }
}
