package org.confluence.mod.terra_curio.common.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.init.ModItems;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TerraCurio.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModItems.ITEMS.getEntries().forEach(item -> {
            try {
                Item value = item.get();
                String path = item.getId().getPath().toLowerCase();
                if (value instanceof BaseCurioItem) {
                    withExistingParent(path, "item/generated").texture("layer0", TerraCurio.asResource("item/curio/" + path));
                } else {
                    withExistingParent(path, "item/generated").texture("layer0", TerraCurio.asResource( "item/" + path));
                }
            } catch (Exception e) {
                TerraCurio.LOGGER.error(e.getMessage());
            }
        });
    }
}