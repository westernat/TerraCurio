package org.confluence.terra_curio.common.data.gen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCItems;

import java.util.stream.Stream;

public class TCModelProvider extends ModelProvider {
    public TCModelProvider(PackOutput output) {
        super(output, TerraCurio.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        TCItems.OTHERS.getEntries().forEach(item -> {
            if (!(item.get() instanceof BlockItem)) {
                itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
            }
        });
        TCItems.CURIOS.getEntries().forEach(item -> {
            Material texture = new Material(TerraCurio.asResource("item/curio/" + item.getId().getPath()));
            TextureMapping layer0 = new TextureMapping().put(TextureSlot.LAYER0, texture);
            Identifier id = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item.get()), layer0, itemModels.modelOutput);
            itemModels.itemModelOutput.accept(item.get(), ItemModelUtils.plainModel(id));
        });
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }
}
