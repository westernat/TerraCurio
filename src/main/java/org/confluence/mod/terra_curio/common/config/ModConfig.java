package org.confluence.mod.terra_curio.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue ANKH_SHIELD_RESISTANCE = BUILDER.push("Attributes").push("Ankh Shield").defineInRange("knockbackResistance", 1.0, 0.0, 1.0);
    public static final ModConfigSpec.IntValue ANKH_SHIELD_ARMOR = BUILDER.defineInRange("armor", 4, 0, 1024);

    public static final ModConfigSpec.DoubleValue CELESTIAL_STONE_SPEED = BUILDER.push("Attributes").push("Celestial Stone").defineInRange("attackSpeed", 0.1, 0.0, 10.0);
    public static final ModConfigSpec.DoubleValue CELESTIAL_STONE_DAMAGE = BUILDER.defineInRange("attackDamage", 0.1, 0.0, 10.0);
    public static final ModConfigSpec.IntValue CELESTIAL_STONE_ARMOR = BUILDER.defineInRange("armor", 4, 0, 1024);
    public static final ModConfigSpec.DoubleValue CELESTIAL_STONE_CRITICAL_CHANCE = BUILDER.defineInRange("criticalChance", 0.02, 0.0, 1.0);
    public static final ModConfigSpec.DoubleValue CELESTIAL_STONE_MINING = BUILDER.defineInRange("miningSpeed", 0.15, 0.0, 10.0);
    public static final ModConfigSpec.DoubleValue CELESTIAL_STONE_RANGED = BUILDER.defineInRange("rangedDamage", 0.1, 0.0, 10.0);
    public static final ModConfigSpec.DoubleValue CELESTIAL_STONE_MAGIC = BUILDER.defineInRange("magicDamage", 0.1, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue COBALT_SHIELD_RESISTANCE = BUILDER.push("Attributes").push("Cobalt Shield").defineInRange("knockbackResistance", 1.0, 0.0, 1.0);
    public static final ModConfigSpec.IntValue COBALT_SHIELD_ARMOR = BUILDER.defineInRange("armor", 1, 0, 1024);

    public static final ModConfigSpec.DoubleValue DESTROYER_EMBLEM_DAMAGE = BUILDER.push("Attributes").push("Destroyer Emblem").defineInRange("attackDamage", 0.1, 0.0, 10.0);
    public static final ModConfigSpec.DoubleValue DESTROYER_EMBLEM_CRITICAL_CHANCE = BUILDER.defineInRange("criticalChance", 0.08, 0.0, 1.0);
    public static final ModConfigSpec.DoubleValue DESTROYER_EMBLEM_RANGED = BUILDER.defineInRange("rangedDamage", 0.1, 0.0, 10.0);
    public static final ModConfigSpec.DoubleValue DESTROYER_EMBLEM_MAGIC = BUILDER.defineInRange("magicDamage", 0.1, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue EYE_OF_GOLEM_CRITICAL_CHANCE = BUILDER.pop().push("EyeOfTheGolem").defineInRange("criticalChance", 0.1, 0.0, 1.0);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
