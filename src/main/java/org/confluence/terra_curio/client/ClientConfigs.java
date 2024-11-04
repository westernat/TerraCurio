package org.confluence.terra_curio.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ClientConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue PLAY_SHOES_SOUND = BUILDER.push("Speed Shoes").define("playSound", true);
    private static final ModConfigSpec.BooleanValue SHOW_SHOES_PARTICLE = BUILDER.define("showParticle", true);
    private static final ModConfigSpec.DoubleValue INFORMATION_HUD_TOP = BUILDER.pop().push("Information HUD").comment("finalTop = screenHeight * top").defineInRange("top", 0.5, 0.0, 1.0);
    private static final ModConfigSpec.BooleanValue INFORMATION_HUD_LEFT = BUILDER.comment("left or right").define("isLeft", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean playShoesSound = true;
    public static boolean showShoesParticle = true;
    public static double informationHudTop = 0.5;
    public static boolean informationIsLeft = false;

    public static void onLoad() {
        playShoesSound = PLAY_SHOES_SOUND.get();
        showShoesParticle = SHOW_SHOES_PARTICLE.get();
        informationHudTop = INFORMATION_HUD_TOP.get();
        informationIsLeft = INFORMATION_HUD_LEFT.get();
    }
}
