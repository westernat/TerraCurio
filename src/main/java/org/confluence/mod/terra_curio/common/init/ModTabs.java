package org.confluence.mod.terra_curio.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;

import java.util.function.Supplier;

public final class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraCurio.MODID);

    public static final Supplier<CreativeModeTab> ACCESSORIES = TABS.register("accessories",
            () -> CreativeModeTab.builder().icon(() -> Items.DIAMOND.getDefaultInstance())
                    .title(Component.translatable("creativetab.terra_curio"))
                    .displayItems((parameters, output) -> {
                        ModItems.ITEMS.getEntries().forEach(entry -> output.accept(entry.get()));
                    })
                    .build()
    );
}
