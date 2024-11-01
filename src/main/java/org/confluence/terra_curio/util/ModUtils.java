package org.confluence.terra_curio.util;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.apache.commons.compress.utils.Lists;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.component.EffectImmunities;
import org.confluence.terra_curio.common.component.primitive.PrimitiveValue;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;

public final class ModUtils {
    public static float nextFloat(RandomSource randomSource, float origin, float bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound - origin is non positive");
        } else {
            return origin + randomSource.nextFloat() * (bound - origin);
        }
    }

    public static boolean isServerNotFake(Player player) {
        return player instanceof ServerPlayer && !(player instanceof FakePlayer);
    }

    public static <T, V extends PrimitiveValue<T>> @Nullable V getPrimitiveValue(ItemStack itemStack, AccessoriesComponent.Type<T, V> type) {
        AccessoriesComponent component = itemStack.get(TCDataComponentTypes.ACCESSORIES);
        V value;
        if (component != null && (value = component.get(type)) != null) {
            return value;
        }
        return null;
    }

    public static <T, V extends PrimitiveValue<T>> void putPrimitiveValue(ItemStack itemStack, AccessoriesComponent.Type<T, V> type, V value) {
        AccessoriesComponent component = itemStack.get(TCDataComponentTypes.ACCESSORIES);
        if (component != null) component.put(type, value);
    }

    public static boolean hasEffectImmunity(LivingEntity living, Holder<MobEffect> mobEffect) {
        ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosInventory(living).orElse(null);
        return curiosItemHandler != null && curiosItemHandler.getCurios().values().stream()
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
                    EffectImmunities component = stack.get(TCDataComponentTypes.EFFECT_IMMUNITIES);
                    return component != null && component.contains(mobEffect);
                });
    }

    public static void applyFireAttack(Player player, Entity entity) {
        if (CuriosUtils.hasCurio(player, AccessoriesComponent.FIRE_ATTACK.key())) {
            float f = player.getRandom().nextFloat();
            int time;
            if (f < 0.25F) {
                time = 120;
            } else if (f < 0.375F) {
                time = 80;
            } else {
                time = 40;
            }
            entity.igniteForTicks(time);
        }
    }
}
