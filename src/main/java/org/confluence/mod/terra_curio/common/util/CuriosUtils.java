package org.confluence.mod.terra_curio.common.util;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;
import org.confluence.mod.terra_curio.common.component.EffectImmunities;
import org.confluence.mod.terra_curio.common.init.ModDataComponentTypes;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;


public class CuriosUtils {
    public static boolean hasEffectImmunity(LivingEntity living, Holder<MobEffect> mobEffect) {
        ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosInventory(living).orElse(null);
        if (curiosItemHandler != null){
            return curiosItemHandler.getCurios().values().stream()
                    .map(ICurioStacksHandler::getStacks)
                    .flatMap(iDynamicStackHandler -> {
                        int slots = iDynamicStackHandler.getSlots();
                        List<ItemStack> stacks = Lists.newArrayList();
                        for (int i = 0; i < slots; i++) {
                            stacks.add(iDynamicStackHandler.getStackInSlot(i));
                        }
                        return stacks.stream();
                    })
                    .anyMatch(stack -> {
                        EffectImmunities effectImmunities = stack.get(ModDataComponentTypes.EFFECT_IMMUNITIES);
                        if (effectImmunities != null) {
                            return effectImmunities.contains(mobEffect);
                        }
                        return false;
                    });
        }
        return false;
    }
}
