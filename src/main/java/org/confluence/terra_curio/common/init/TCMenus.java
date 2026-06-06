package org.confluence.terra_curio.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.menu.WorkshopMenu;

public final class TCMenus {
    public static final DeferredRegister<MenuType<?>> TYPES = DeferredRegister.create(Registries.MENU, TerraCurio.MODID);

    public static final RegistryObject<MenuType<WorkshopMenu>> WORKSHOP = TYPES.register("workshop", () -> new MenuType<>(WorkshopMenu::new, FeatureFlags.VANILLA_SET));
}
