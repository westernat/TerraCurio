package org.confluence.terra_curio.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.util.TCUtils;

import java.util.function.Supplier;

public final class TCTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraCurio.MODID);

    public static final Supplier<CreativeModeTab> ACCESSORIES = TABS.register("accessories",
            () -> CreativeModeTab.builder().icon(() -> TCItems.ANKH_SHIELD.get().getDefaultInstance())
                    .title(Component.translatable("creativetab.terra_curio"))
                    .displayItems((parameters, output) -> {
                        TCUtils.forConfluence$Inject();
                        TCItems.CURIOS.getEntries().forEach(entry -> output.accept(entry.get()));
                    })
                    .build()
    );
}
