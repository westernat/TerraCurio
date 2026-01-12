package org.confluence.terra_curio.mixin.spartanweaponry;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.item.curio.combat.MagicQuiver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "com.oblivioussp.spartanweaponry.item.HeavyCrossbowItem", remap = false)
public abstract class HeavyCrossbowItemMixin {
    @Shadow
    @Final
    public static String NBT_CHARGED;

    @WrapWithCondition(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putBoolean(Ljava/lang/String;Z)V", ordinal = 1))
    private boolean canClear(CompoundTag instance, String pKey, boolean pValue, @Local(argsOnly = true) LivingEntity pShooter) {
        return MagicQuiver.shouldConsume(pShooter);
    }

    @WrapWithCondition(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;", ordinal = 1))
    private boolean canClear(CompoundTag instance, String pKey, Tag pValue) {
        return !instance.getBoolean(NBT_CHARGED);
    }
}
