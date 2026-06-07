package org.confluence.terra_curio.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terra_curio.TerraCurio;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = TerraCurio.MODID, value = Dist.CLIENT)
public final class TCKeyBindings {
    @SubscribeEvent
    public static void keyBinding(RegisterKeyMappingsEvent event) {
        event.register(METAL_DETECTOR.get());
        event.register(STEP_STOOL.get());
        event.register(FLIP_GRAVITATION.get());
        event.register(CTHULHU_SPRINTING.get());
    }

    public static final Supplier<KeyMapping> METAL_DETECTOR = Suppliers.memoize(() -> new KeyMapping(
            "key.terra_curio.metal_detector",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_CONTROL,
            category()
    ));

    public static final Supplier<KeyMapping> STEP_STOOL = Suppliers.memoize(() -> new KeyMapping(
            "key.terra_curio.step_stool",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UP,
            category()
    ));

    public static final Supplier<KeyMapping> FLIP_GRAVITATION = Suppliers.memoize(() -> new KeyMapping(
            "key.terra_curio.flip_gravitation",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UP,
            category()
    ));

    public static final Supplier<KeyMapping> CTHULHU_SPRINTING = Suppliers.memoize(() -> new KeyMapping(
            "key.terra_curio.cthulhu_sprinting",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            category()
    ));

    private static String category() {
        return "key.terra_curio.gameplay"; // confluence mixin here
    }
}
