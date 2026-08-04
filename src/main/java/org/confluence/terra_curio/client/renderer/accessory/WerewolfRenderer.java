package org.confluence.terra_curio.client.renderer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.util.LibDateUtils;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.client.model.accessory.WerewolfModel;
import org.confluence.terra_curio.mixed.ILivingEntityRenderState;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class WerewolfRenderer implements ICurioRenderer.HumanoidRender {
    public static final Identifier TEXTURE = TerraCurio.asResource("textures/curio/werewolf.png");

    private final WerewolfModel model;

    public WerewolfRenderer(EntityModelSet entityModels) {
        this.model = new WerewolfModel(entityModels.bakeLayer(WerewolfModel.LAYER_LOCATION));
    }

    @Override
    public EntityModel<HumanoidRenderState> getModel(ItemStack stack, SlotContext slotContext) {
        return model;
    }

    @Override
    public Identifier getModelTexture(ItemStack stack, SlotContext slotContext) {
        return TEXTURE;
    }

    @Override
    public <L extends LivingEntityRenderState, T extends EntityModel<? super L>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, SubmitNodeCollector renderTypeBuffer, int packedLight, L renderState, RenderLayerParent<L, T> renderLayerParent, EntityRendererProvider.Context context, float yRotation, float xRotation) {
        LivingEntity living = slotContext.entity();
        if (LibDateUtils.isNight(living.level()) && !TCClientPacketHandler.canShowNeptunesShell(living)) {
            HumanoidRender.super.render(stack, slotContext, poseStack, renderTypeBuffer, packedLight, renderState, renderLayerParent, context, yRotation, xRotation);
            ILivingEntityRenderState.of(renderState).terra_curio$setShowingCosmetic(true);
        }
    }
}
