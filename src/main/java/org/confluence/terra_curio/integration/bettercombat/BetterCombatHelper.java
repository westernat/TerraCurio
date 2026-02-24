package org.confluence.terra_curio.integration.bettercombat;

import net.minecraft.world.item.ItemStack;
import org.confluence.lib.util.LibUtils;

import java.lang.reflect.Method;

public class BetterCombatHelper {
    public static final boolean LOADED = LibUtils.isModLoaded("bettercombat");
    private static Method getAttributes;
    private static boolean error = false;

    public static boolean hasWeaponAttributes(ItemStack itemStack) {
        if (LOADED && !error) {
            try {
                if (getAttributes == null) {
                    Class<?> WeaponRegistry = BetterCombatHelper.class.getClassLoader().loadClass("net.bettercombat.logic.WeaponRegistry");
                    getAttributes = WeaponRegistry.getDeclaredMethod("getAttributes", ItemStack.class);
                    getAttributes.setAccessible(true);
                }
                return getAttributes.invoke(null, itemStack) != null;
            } catch (Exception e) {
                error = true;
            }
        }
        return false;
    }
}
