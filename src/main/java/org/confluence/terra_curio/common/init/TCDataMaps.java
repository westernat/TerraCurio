package org.confluence.terra_curio.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.component.AccessoriesComponent;

public final class TCDataMaps {
    public static final AdvancedDataMapType<Item, AccessoriesComponent, AccessoriesComponent.Remover> ACCESSORIES = AdvancedDataMapType.builder(
            TerraCurio.asResource("accessories"),
            Registries.ITEM,
            AccessoriesComponent.CODEC
    ).synced(AccessoriesComponent.CODEC, false).remover(AccessoriesComponent.Remover.CODEC).build();

    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(ACCESSORIES);
    }
}
