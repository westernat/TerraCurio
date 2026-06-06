package org.confluence.terra_curio.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.model.entity.XBoneProjectileModel;
import org.confluence.terra_curio.common.entity.XBoneProjectile;

public class XBoneProjectileRenderer extends EntityRenderer<XBoneProjectile> {
    private static final ResourceLocation TEXTURE = TerraCurio.asResource("textures/entity/x_bone_projectile.png");
    private final XBoneProjectileModel model;

    public XBoneProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new XBoneProjectileModel(context.bakeLayer(XBoneProjectileModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(XBoneProjectile entity) {
        return TEXTURE;
    }

    @Override
    public void render(XBoneProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(0.75F, 0.75F, 0.75F);
        poseStack.translate(0, 0.5F, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - 90.0F));
        poseStack.mulPose(Axis.ZP.rotation(-Mth.lerp(partialTick, entity.rotate.old, entity.rotate.neo)));
        poseStack.mulPose(Axis.YP.rotation(-Mth.HALF_PI));
        poseStack.translate(0,0 -0.5F, 0);
        model.renderToBuffer(poseStack, buffer.getBuffer(model.renderType(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
