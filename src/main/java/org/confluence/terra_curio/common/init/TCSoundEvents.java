package org.confluence.terra_curio.common.init;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;

import java.util.function.Supplier;


public final class TCSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, TerraCurio.MODID);

    public static final Supplier<SoundEvent> TRANSMISSION = register("transmission");
    public static final Supplier<SoundEvent> DOUBLE_JUMP = register("double_jump");
    public static final Supplier<SoundEvent> SHOES_WALK = register("shoes_walk");
    public static final Supplier<SoundEvent> FART_SOUND = register("fart_sound");
    public static final Supplier<SoundEvent> ROCKET_BOOTS_BOOST = register("rocket_boots_boost");
    public static final Supplier<SoundEvent> ROCKET_BOOTS_STOP = register("rocket_boots_stop");

    private static Supplier<SoundEvent> register(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TerraCurio.MODID, id)));
    }
}
