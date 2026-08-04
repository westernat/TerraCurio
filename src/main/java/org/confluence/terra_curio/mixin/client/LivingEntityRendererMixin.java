package org.confluence.terra_curio.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.mixed.IEntity;
import org.confluence.terra_curio.mixed.ILivingEntityRenderState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {
    @ModifyReturnValue(method = "isEntityUpsideDown", at = @At("RETURN"))
    private static boolean upsideDown(boolean original, @Local(argsOnly = true, name = "mob") LivingEntity mob) {
        if (!original && IEntity.of(mob).terra_curio$isShouldRot()) {
            return true;
        }
        return original;
    }

    @ModifyExpressionValue(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private @Nullable RenderType skip(@Nullable RenderType original, @Local(argsOnly = true, name = "state") LivingEntityRenderState state) {
        if (ILivingEntityRenderState.of(state).terra_curio$isShowingCosmetic()) {
            return null;
        }
        return original;
    }
}
