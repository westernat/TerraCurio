package org.confluence.terra_curio.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.model.entity.XBoneProjectileModel;
import org.confluence.terra_curio.common.entity.XBoneProjectile;

public class XBoneProjectileRenderer extends EntityRenderer<XBoneProjectile, XBoneProjectileRenderer.State> {
    private static final Identifier TEXTURE = TerraCurio.asResource("textures/entity/x_bone_projectile.png");
    private final XBoneProjectileModel model;

    public XBoneProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new XBoneProjectileModel(pContext.bakeLayer(XBoneProjectileModel.LAYER_LOCATION));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(XBoneProjectile entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot();
        state.zRot = Mth.lerp(partialTicks, entity.rotate.old, entity.rotate.neo);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.scale(0.75F, 0.75F, 0.75F);
        poseStack.translate(0, 0.5F, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZN.rotation(state.zRot));
        poseStack.mulPose(Axis.YN.rotation(Mth.HALF_PI));
        poseStack.translate(0, -0.5F, 0);
        submitNodeCollector.submitModel(model, state, poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
    }

    public static class State extends EntityRenderState {
        public float yRot, zRot;
    }
}
