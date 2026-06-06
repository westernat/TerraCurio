package org.confluence.terra_curio.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_curio.TerraCurio;

public final class TCSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, TerraCurio.MODID);

    public static final RegistryObject<SoundEvent> TRANSMISSION = register("transmission");
    public static final RegistryObject<SoundEvent> DOUBLE_JUMP = register("double_jump");
    public static final RegistryObject<SoundEvent> SHOES_WALK = register("shoes_walk");
    public static final RegistryObject<SoundEvent> FART_SOUND = register("fart_sound");
    public static final RegistryObject<SoundEvent> ROCKET_BOOTS_BOOST = register("rocket_boots_boost");
    public static final RegistryObject<SoundEvent> ROCKET_BOOTS_STOP = register("rocket_boots_stop");

    private static RegistryObject<SoundEvent> register(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(TerraCurio.asResource(id)));
    }
}
