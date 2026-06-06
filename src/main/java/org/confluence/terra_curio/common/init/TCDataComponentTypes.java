package org.confluence.terra_curio.common.init;

import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.component.PrimitiveValueComponent;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.registries.PortDataComponentRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.registries.PortRegistryEntry;

public final class TCDataComponentTypes {
    public static final PortDataComponentRegistration TYPES = PortRegisterHandler.dataComponent(TerraCurio.MODID);

    public static final PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<PrimitiveValueComponent>> ACCESSORIES = TYPES.builder("accessories", builder -> builder.persistent(PrimitiveValueComponent.CODEC).networkSynchronized(PrimitiveValueComponent.STREAM_CODEC));
}
