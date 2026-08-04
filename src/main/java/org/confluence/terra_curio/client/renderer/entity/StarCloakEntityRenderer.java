package org.confluence.terra_curio.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.common.entity.StarCloakEntity;
import org.confluence.terra_curio.common.init.TCItems;
import org.jetbrains.annotations.NotNull;

public class StarCloakEntityRenderer extends EntityRenderer<StarCloakEntity, ItemClusterRenderState> {
    private ItemStack stack;

    public StarCloakEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(@NotNull StarCloakEntity pEntity, @NotNull BlockPos pPos) {
        return 15;
    }

    @Override
    public ItemClusterRenderState createRenderState() {
        return new ItemClusterRenderState();
    }

    @Override
    public void extractRenderState(StarCloakEntity entity, ItemClusterRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (stack == null) {
            stack = TCItems.STAR.toStack();
        }
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(state.item, stack, ItemDisplayContext.GROUND, entity.level(), entity.getOwner(), 260726);
    }

    @Override
    public void submit(ItemClusterRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        poseStack.mulPose(Axis.YP.rotation(Mth.PI));
        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}
