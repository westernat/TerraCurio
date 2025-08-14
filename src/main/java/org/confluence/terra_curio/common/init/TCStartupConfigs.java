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

        ATTRIBUTE_REPLACE = BUILDER.defineListAllowEmpty("attributeReplacements", () -> List.of(
                "crit_chance = terra_curio:generic.crit_chance",
                "ranged_damage = terra_curio:generic.ranged_damage",
                "dodge_chance = terra_curio:generic.dodge_chance",
                "magic_damage = terra_curio:generic.magic_damage",
                "armor_penetration = terra_curio:generic.armor_penetration"
        ), () -> "", o -> true);

        container.registerConfig(ModConfig.Type.STARTUP, BUILDER.build());
        onLoad();
    }
}
