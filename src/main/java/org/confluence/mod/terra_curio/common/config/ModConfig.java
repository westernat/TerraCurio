package org.confluence.mod.terra_curio.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForgeConfig;

public class ModConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC = BUILDER.build();

    public static final ModConfigSpec.DoubleValue ANKH_SHIELD_RESISTANCE = BUILDER.pop().push("Attributes").push("Ankh Shield").defineInRange("knockbackResistance", 1.0, 0.0, 1.0);
    public static final ModConfigSpec.IntValue ANKH_SHIELD_ARMOR = BUILDER.defineInRange("armor", 4, 0, 1024);
}
