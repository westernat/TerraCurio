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
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.util.LibDateUtils;
import org.confluence.terra_curio.client.model.accessory.MermanModel;
import org.confluence.terra_curio.client.model.accessory.WerewolfModel;
import org.confluence.terra_curio.mixed.ILivingEntityRenderState;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class MoonShellRenderer implements ICurioRenderer.HumanoidRender {
    private final MermanModel mermanModel;
    private final WerewolfModel werewolfModel;

    public MoonShellRenderer(EntityModelSet entityModels) {
        this.mermanModel = new MermanModel(entityModels.bakeLayer(MermanModel.LAYER_LOCATION));
        this.werewolfModel = new WerewolfModel(entityModels.bakeLayer(WerewolfModel.LAYER_LOCATION));
    }

    @Override
    public <L extends LivingEntityRenderState, T extends EntityModel<? super L>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, SubmitNodeCollector renderTypeBuffer, int packedLight, L renderState, RenderLayerParent<L, T> renderLayerParent, EntityRendererProvider.Context context, float yRotation, float xRotation) {
        if (isInWater(slotContext) || LibDateUtils.isNight(slotContext.entity().level())) {
            HumanoidRender.super.render(stack, slotContext, poseStack, renderTypeBuffer, packedLight, renderState, renderLayerParent, context, yRotation, xRotation);
            ILivingEntityRenderState.of(renderState).terra_curio$setShowingCosmetic(true);
        }
    }

    @Override
    public EntityModel<HumanoidRenderState> getModel(ItemStack stack, SlotContext slotContext) {
        return isInWater(slotContext) ? mermanModel : werewolfModel;
    }

    @Override
    public Identifier getModelTexture(ItemStack stack, SlotContext slotContext) {
        return isInWater(slotContext) ? MermanRenderer.TEXTURE : WerewolfRenderer.TEXTURE;
    }

    private static boolean isInWater(SlotContext slotContext) {
        return slotContext.entity().isInWater();
    }
}
