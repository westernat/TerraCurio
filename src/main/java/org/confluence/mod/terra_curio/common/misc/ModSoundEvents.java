package org.confluence.mod.terra_curio.common.misc;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;

import java.util.function.Supplier;


public final class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, TerraCurio.MOD_ID);

    public static final Supplier<SoundEvent> TRANSMISSION = register("transmission");
    public static final Supplier<SoundEvent> WAVING = register("waving");
    public static final Supplier<SoundEvent> DOUBLE_JUMP = register("double_jump");
    public static final Supplier<SoundEvent> SHOES_FLY = register("shoes_fly");
    public static final Supplier<SoundEvent> SHOES_FLY_JET = register("shoes_fly_jet");
    public static final Supplier<SoundEvent> SHOES_WALK = register("shoes_walk");
    public static final Supplier<SoundEvent> FART_SOUND = register("fart_sound");
    public static final Supplier<SoundEvent> ACHIEVEMENTS = register("achievements");

    private static Supplier<SoundEvent> register(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TerraCurio.MOD_ID, id)));
    }
}
