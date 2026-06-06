package org.confluence.terra_curio.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.common.entity.StarCloakEntity;
import org.confluence.terra_curio.common.init.TCItems;

public class StarCloakEntityRenderer extends EntityRenderer<StarCloakEntity> {
    private final ItemRenderer itemRenderer;
    private final ItemStack item;

    public StarCloakEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.item = TCItems.STAR.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getTextureLocation(StarCloakEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    @Override
    protected int getBlockLightLevel(StarCloakEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(StarCloakEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        itemRenderer.renderStatic(item, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}
