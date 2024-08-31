package org.confluence.mod.terra_curio.common.init;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.client.component.EffectImmunities;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TerraCurio.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EffectImmunities>> EFFECT_IMMUNITIES = DATA_COMPONENT_TYPE.register("effect_immunities",
            () -> DataComponentType.<EffectImmunities>builder().persistent(EffectImmunities.CODEC).networkSynchronized(EffectImmunities.STREAM_CODEC).cacheEncoding().build());


}
