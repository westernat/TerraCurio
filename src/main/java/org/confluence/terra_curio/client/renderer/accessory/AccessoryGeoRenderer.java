package org.confluence.terra_curio.client.renderer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.client.model.accessory.AccessoryGeoModel;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.util.RenderUtil;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class AccessoryGeoRenderer implements ICurioRenderer {
    protected final AccessoryGeoModel geoModel;
    protected final RenderType renderType;

    public AccessoryGeoRenderer(AccessoryGeoModel model) {
        this.geoModel = model;
        this.renderType = RenderType.armorCutoutNoCull(model.getTexture());
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack poseStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource bufferSource,
            int packedLight,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (slotContext.visible()) {
            defaultRender(slotContext, poseStack, bufferSource, packedLight, partialTick, ageInTicks);
        }
    }

    protected void defaultRender(SlotContext slotContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, float ageInTicks) {
        poseStack.pushPose();
        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -1.501F, 0);
        actuallyRender(slotContext.entity(), poseStack, bufferSource, packedLight, partialTick, ageInTicks, slotContext.index());
        poseStack.popPose();
    }

    protected void actuallyRender(LivingEntity living, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, float ageInTick, int slotIndex) {
        VertexConsumer buffer = bufferSource.getBuffer(renderType);
        for (GeoBone bone : geoModel.getBakedModel().topLevelBones()) {
            renderRecursively(poseStack, bone, buffer, packedLight);
        }
    }

    protected void renderRecursively(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight) {
        poseStack.pushPose();
        RenderUtil.prepMatrixForBone(poseStack, bone);
        renderCubesOfBone(poseStack, bone, buffer, packedLight);
        renderChildBones(poseStack, bone, buffer, packedLight);
        poseStack.popPose();
    }

    protected void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight) {
        if (bone.isHidden()) return;
        for (GeoCube cube : bone.getCubes()) {
            poseStack.pushPose();
            renderCube(poseStack, cube, buffer, packedLight);
            poseStack.popPose();
        }
    }

    protected void renderChildBones(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight) {
        if (bone.isHidingChildren()) return;
        for (GeoBone childBone : bone.getChildBones()) {
            renderRecursively(poseStack, childBone, buffer, packedLight);
        }
    }

    protected void renderCube(PoseStack poseStack, GeoCube cube, VertexConsumer buffer, int packedLight) {
        RenderUtil.translateToPivotPoint(poseStack, cube);
        RenderUtil.rotateMatrixAroundCube(poseStack, cube);
        RenderUtil.translateAwayFromPivotPoint(poseStack, cube);

        Matrix3f normalisedPoseState = poseStack.last().normal();
        Matrix4f poseState = new Matrix4f(poseStack.last().pose());

        for (GeoQuad quad : cube.quads()) {
            if (quad == null) continue;
            Vector3f normal = normalisedPoseState.transform(new Vector3f(quad.normal()));
            RenderUtil.fixInvertedFlatCube(cube, normal);
            createVerticesOfQuad(quad, poseState, normal, buffer, packedLight);
        }
    }

    protected void createVerticesOfQuad(GeoQuad quad, Matrix4f poseState, Vector3f normal, VertexConsumer buffer, int packedLight) {
        for (GeoVertex vertex : quad.vertices()) {
            Vector3f position = vertex.position();
            Vector4f vector4f = poseState.transform(new Vector4f(position.x(), position.y(), position.z(), 1.0f));

            buffer.addVertex(
                    vector4f.x(), vector4f.y(), vector4f.z(),
                    -1, vertex.texU(), vertex.texV(),
                    OverlayTexture.NO_OVERLAY, packedLight,
                    normal.x(), normal.y(), normal.z()
            );
        }
    }
}
