package org.confluence.terra_curio.common.init;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.component.AccessoriesComponent;

import java.util.function.Supplier;

public final class TCDataComponentTypes {
    public static final DeferredRegister.DataComponents TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TerraCurio.MODID);

    public static final Supplier<DataComponentType<AccessoriesComponent>> ACCESSORIES = TYPES.registerComponentType("accessories", builder -> builder.persistent(AccessoriesComponent.CODEC).networkSynchronized(AccessoriesComponent.STREAM_CODEC));
}
