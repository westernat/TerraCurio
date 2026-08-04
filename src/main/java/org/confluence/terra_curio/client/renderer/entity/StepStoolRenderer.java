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
import org.confluence.terra_curio.client.model.entity.StepStoolModel;
import org.confluence.terra_curio.common.entity.StepStoolEntity;

public class StepStoolRenderer extends EntityRenderer<StepStoolEntity, StepStoolRenderer.State> {
    private static final Identifier TEXTURE = TerraCurio.asResource("textures/entity/step_stool.png");

    private final StepStoolModel model;

    public StepStoolRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new StepStoolModel((pContext.bakeLayer(StepStoolModel.LAYER_LOCATION)));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(StepStoolEntity entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.step = entity.getStep();
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0, 1.5F, 0);
        poseStack.mulPose(Axis.ZP.rotation(Mth.PI));
        for (int i = 0; i < state.step; i++) {
            submitNodeCollector.submitModel(model, state, poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
            poseStack.translate(0, -1, 0);
        }
        poseStack.popPose();
    }

    public static class State extends EntityRenderState {
        public int step;
    }
}
