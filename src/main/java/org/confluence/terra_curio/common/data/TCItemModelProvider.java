package org.confluence.terra_curio.common.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCItems;

public class TCItemModelProvider extends ItemModelProvider {
    public TCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TerraCurio.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        TCItems.OTHERS.getEntries().forEach(item -> {
            if (item.get() instanceof BlockItem) return;
            try {
                String path = item.getId().getPath().toLowerCase();
                withExistingParent(path, "item/generated").texture("layer0", TerraCurio.asResource("item/" + path));
            } catch (Exception e) {
                TerraCurio.LOGGER.error(e.getMessage());
            }
        });
        TCItems.CURIOS.getEntries().forEach(item -> {
            try {
                String path = item.getId().getPath().toLowerCase();
                withExistingParent(path, "item/generated").texture("layer0", TerraCurio.asResource("item/curio/" + path));
            } catch (Exception e) {
                TerraCurio.LOGGER.error(e.getMessage());
            }
        });
    }
}
