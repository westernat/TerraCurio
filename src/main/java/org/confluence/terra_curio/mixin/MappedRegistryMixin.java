package org.confluence.terra_curio.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_curio.TerraCurio;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> {
    @Shadow
    @Final
    private Map<ResourceLocation, Holder.Reference<T>> byLocation;

    @SuppressWarnings("unchecked")
    @ModifyExpressionValue(method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private <V> @Nullable V fixModId(@Nullable V original, @Local(argsOnly = true) @Nullable ResourceLocation name) {
        if (original == null && name != null && "confluence".equals(name.getNamespace())) {
            return (V) byLocation.get(TerraCurio.asResource(name.getPath()));
        }
        return original;
    }
}
