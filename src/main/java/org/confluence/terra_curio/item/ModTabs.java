package org.confluence.terra_curio.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.block.ModBlocks;
import org.confluence.terra_curio.item.common.IconItem;
import org.confluence.terra_curio.item.curio.CurioItems;

@SuppressWarnings("unused")
public final class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraCurio.MODID);

    public static final RegistryObject<CreativeModeTab> JEWELRY = TABS.register("accessories",
        () -> CreativeModeTab.builder().icon(() -> new ItemStack(IconItem.Icons.ACCESSORIES_ICON.get()))
            .title(Component.translatable("creativetab.terra_curio.accessories"))
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.WORKSHOP.get());
                output.accept(ModItems.DEMON_HEART.get());
                output.accept(ModItems.MAGIC_MIRROR.get());
                output.accept(ModItems.CELL_PHONE.get());
                for (CurioItems curioItems : CurioItems.values()) output.accept(curioItems.get());
            })
            .build());
}