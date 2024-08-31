package org.confluence.mod.terra_curio.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.init.ModItems;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = TerraCurio.asResource("jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        ModItems.ITEMS.getEntries().forEach(entry -> {
            if (entry.get() instanceof BaseCurioItem curioItem){
                Component[] information = new Component[curioItem.getJeiInformationCount()];
                for (int i = 0; i < information.length; i++) {
                    information[i] = Component.translatable("jei.tooltips" + curioItem.getDescriptionId());
                }
                registration.addItemStackInfo(new ItemStack(entry.get()), information);
            }
        });
    }
}
