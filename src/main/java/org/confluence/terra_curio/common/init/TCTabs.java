package org.confluence.terra_curio.common.init;

import com.google.common.collect.Iterables;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.util.WipNotDisplayOutput;
import org.confluence.terra_curio.TerraCurio;

public final class TCTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraCurio.MODID);

    public static final RegistryObject<CreativeModeTab> ACCESSORIES = TABS.register("accessories",
            () -> CreativeModeTab.builder().icon(() -> TCItems.ICON.get().getDefaultInstance())
                    .title(Component.translatable("creativetab.terra_curio"))
                    .displayItems((parameters, output) -> {
                        output = new WipNotDisplayOutput(output);
                        output.accept(TCItems.WORKSHOP.get());
                        output.accept(TCItems.DEMON_HEART.get());
                        output.accept(TCItems.MAGIC_MIRROR.get());
                        output.accept(TCItems.CELL_PHONE.get());
                        output.accept(TCItems.DIVING_HELMET.get());
                        CreativeModeTab.Output finalOutput = output;
                        Iterables.concat(TCItems.CURIOS.getEntries(), TCItems.WINGS.getEntries()).forEach(holder -> finalOutput.accept(holder.get()));
                    })
                    .withTabsAfter(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(ConfluenceMagicLib.CONFLUENCE_ID, "armors")))
                    .withTabsBefore(
                            ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(ConfluenceMagicLib.CONFLUENCE_ID, "tools")),
                            CreativeModeTabs.SPAWN_EGGS
                    ).build()
    );
}
