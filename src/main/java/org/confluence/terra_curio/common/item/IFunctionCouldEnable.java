package org.confluence.terra_curio.common.item;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.api.primitive.TooltipComponentsValue;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.Nullable;

public interface IFunctionCouldEnable {
    String DISABLE = "disable";

    default String getDisableKey() {
        return DISABLE;
    }

    default boolean isEnabled(ItemStack itemStack, @Nullable TooltipComponentsValue.Storage storage) {
        return !LibUtils.getItemStackNbtNoCopy(itemStack).getBoolean(getDisableKey());
    }

    default void cycleEnable(ItemStack itemStack) {
        LibUtils.updateItemStackNbt(itemStack, tag -> {
            String key = getDisableKey();
            tag.putBoolean(key, !tag.getBoolean(key));
        });
    }

    default @Nullable TooltipComponent getTooltipComponent(ItemStack itemStack) {
        AccessoriesComponent component = TCUtils.getAccessoriesComponent(itemStack);
        if (component == null) return null;
        TooltipComponentsValue value = component.get(TCItems.INFORMATION);
        if (value == null) return null;
        return new TooltipComponentsValue.Multi(value.get());
    }

    interface Multi extends IFunctionCouldEnable {
        Object2IntMap<TooltipComponentsValue.Storage> INDEX_MAP = Util.make(new Object2IntArrayMap<>(), map -> {
            map.put(TCItems.MINUTE$WATCH, 0);
            map.put(TCItems.WEATHER$RADIO, 1);
            map.put(TCItems.$SEXTANT, 2);
            map.put(TCItems.FISHERMANS$POCKET$GUIDE, 3);
            map.put(TCItems.METAL$DETECTOR, 4);
            map.put(TCItems.LIFE$FORM$ANALYZER, 5);
            map.put(TCItems.$RADAR, 6);
            map.put(TCItems.TALLY$COUNTER, 7);
            map.put(TCItems.DPS$METER, 8);
            map.put(TCItems.$STOPWATCH, 9);
            map.put(TCItems.$COMPASS, 10);
            map.put(TCItems.DEPTH$METER, 11);
        });

        @Override
        default boolean isEnabled(ItemStack itemStack, @Nullable TooltipComponentsValue.Storage storage) {
            if (storage == null) return false;
            int index = INDEX_MAP.getOrDefault(storage, -1);
            if (index == -1) return false;
            return !LibUtils.getItemStackNbtNoCopy(itemStack).getBoolean(Integer.toString(index));
        }

        @Override
        default void cycleEnable(ItemStack itemStack) {}
    }
}
