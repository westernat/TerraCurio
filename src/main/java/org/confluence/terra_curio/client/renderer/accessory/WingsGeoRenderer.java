package org.confluence.terra_curio.client.renderer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.client.model.accessory.AccessoryGeoModel;
import org.confluence.terra_curio.client.model.accessory.NormalWingsGeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class WingsGeoRenderer extends AccessoryGeoRenderer {
    public WingsGeoRenderer(NormalWingsGeoModel model) {
        super(model);
    }

    @Override
    protected void defaultRender(SlotContext slotContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, float ageInTicks) {
        poseStack.pushPose();
        ICurioRenderer.translateIfSneaking(poseStack, slotContext.entity());
        ICurioRenderer.rotateIfSneaking(poseStack, slotContext.entity());
        super.defaultRender(slotContext, poseStack, bufferSource, packedLight, partialTick, ageInTicks);
        poseStack.popPose();
    }

    @Override
    protected void actuallyRender(LivingEntity living, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, float ageInTick, int slotIndex) {
        AnimationState<AccessoryGeoModel> animationState = new AnimationState<>(geoModel, 0, 0, partialTick, false);
        animationState.setData(DataTickets.TICK, (double) living.tickCount + partialTick);
        NormalWingsGeoModel.State state;
        if (living == Minecraft.getInstance().player) {
            state = NormalWingsGeoModel.State.local();
        } else {
            state = NormalWingsGeoModel.State.remote(living.getDeltaMovement().y);
        }
        animationState.setData(NormalWingsGeoModel.STATE, state);
        geoModel.handleAnimations(geoModel, living.getId() + slotIndex, animationState);
        super.actuallyRender(living, poseStack, bufferSource, packedLight, partialTick, ageInTick, slotIndex);
    }
}
