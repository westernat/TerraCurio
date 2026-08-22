package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.mixed.ITCClientLivingEntity;
import org.confluence.terra_curio.mixed.ITCEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {
    @ModifyReturnValue(method = "isEntityUpsideDown", at = @At(value = "RETURN", ordinal = 1))
    private static boolean upsideDown(boolean original, @Local(argsOnly = true) LivingEntity living) {
        if (!original && ITCEntity.of(living).terra_curio$isShouldRot()) {
            return true;
        }
        return original;
    }

    @WrapWithCondition(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    private boolean couldRender(EntityModel<T> instance, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, @Local(argsOnly = true) T living) {
        boolean b = ITCClientLivingEntity.of(living).terra_curio$isShowingCosmetic();
        ITCClientLivingEntity.of(living).terra_curio$setShowingCosmetic(false);
        return !b;
    }
}
