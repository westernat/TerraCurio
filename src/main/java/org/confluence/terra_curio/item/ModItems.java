package org.confluence.terra_curio.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.item.common.CellPhone;
import org.confluence.terra_curio.item.common.DemonHeart;
import org.confluence.terra_curio.item.common.IconItem;
import org.confluence.terra_curio.item.common.MagicMirror;
import org.confluence.terra_curio.item.curio.CurioItems;


@SuppressWarnings("unused")
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TerraCurio.MODID);

    public static final RegistryObject<Item> STAR = ITEMS.register("star", () -> new Item(new Item.Properties()));
    public static final RegistryObject<DemonHeart> DEMON_HEART = ITEMS.register("demon_heart", DemonHeart::new);
    public static final RegistryObject<MagicMirror> MAGIC_MIRROR = ITEMS.register("magic_mirror", MagicMirror::new);
    public static final RegistryObject<CellPhone> CELL_PHONE = ITEMS.register("cell_phone", CellPhone::new);

    public static void register(IEventBus bus) {
        IconItem.Icons.init();
        CurioItems.initialize();
        ITEMS.register(bus);
    }
}
