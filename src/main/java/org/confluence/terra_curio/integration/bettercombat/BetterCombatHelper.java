package org.confluence.terra_curio.integration.bettercombat;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Method;

public class BetterCombatHelper {
    private static Boolean isLoaded;
    private static Method getAttributes;

    public static boolean isLoaded() {
        if (isLoaded == null) {
            isLoaded = ModList.get().isLoaded("bettercombat");
        }
        return isLoaded;
    }

    public static boolean hasWeaponAttributes(ItemStack itemStack) {
        try {
            if (getAttributes == null) {
                Class<?> WeaponRegistry = BetterCombatHelper.class.getClassLoader().loadClass("net.bettercombat.logic.WeaponRegistry");
                getAttributes = WeaponRegistry.getDeclaredMethod("getAttributes", ItemStack.class);
                getAttributes.setAccessible(true);
            }
            return getAttributes.invoke(null, itemStack) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
