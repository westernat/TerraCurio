package org.confluence.terra_curio.item.curio.construction;

import net.minecraft.network.chat.Component;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.misc.ModRarity;

public class BrickLayer extends BaseCurioItem implements IRightClickSubtractor {
    public BrickLayer() {
        super(ModRarity.ORANGE);
    }

    @Override
    public Component[] getInformation() {
        return new Component[]{
            Component.translatable("item.terra_curio.brick_layer.info")
        };
    }
}
