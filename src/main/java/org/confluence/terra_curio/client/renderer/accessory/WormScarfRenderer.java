package org.confluence.terra_curio.client.renderer.accessory;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.model.accessory.WormScarfModel;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class WormScarfRenderer implements ICurioRenderer.HumanoidRender {
    public static final Identifier TEXTURE = TerraCurio.asResource("textures/curio/worm_scarf.png");

    private final WormScarfModel model;

    public WormScarfRenderer(EntityModelSet entityModels) {
        this.model = new WormScarfModel(entityModels.bakeLayer(WormScarfModel.LAYER_LOCATION));
    }

    @Override
    public EntityModel<HumanoidRenderState> getModel(ItemStack stack, SlotContext slotContext) {
        return model;
    }

    @Override
    public Identifier getModelTexture(ItemStack stack, SlotContext slotContext) {
        return TEXTURE;
    }
}
