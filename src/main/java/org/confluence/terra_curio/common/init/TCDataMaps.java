package org.confluence.terra_curio.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.component.PrimitiveValueComponent;
import org.mesdag.portlib.datamap.PortAdvancedDataMapType;
import org.mesdag.portlib.event.registries.PortRegisterDataMapTypesEvent;

public final class TCDataMaps {
    public static final PortAdvancedDataMapType<Item, PrimitiveValueComponent, PrimitiveValueComponent.Remover> ACCESSORIES = PortAdvancedDataMapType.builder(TerraCurio.asResource("accessories"), Registries.ITEM, PrimitiveValueComponent.CODEC)
            .synced(PrimitiveValueComponent.CODEC, false)
            .remover(PrimitiveValueComponent.Remover.CODEC)
            .merger(PrimitiveValueComponent.Merger.INSTANCE).build();

    public static void registerDataMapTypes(PortRegisterDataMapTypesEvent event) {
        event.register(ACCESSORIES);
    }
}
