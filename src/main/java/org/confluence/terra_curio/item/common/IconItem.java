package org.confluence.terra_curio.item.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.datagen.limit.CustomModel;
import org.confluence.terra_curio.item.ModItems;
import org.confluence.terra_curio.util.EnumRegister;

public class IconItem extends Item implements CustomModel {
    public IconItem() {
        super(new Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1));
    }

    public enum Icons implements EnumRegister<IconItem> {
        ACCESSORIES_ICON("accessories_icon");

        private final RegistryObject<IconItem> value;

        Icons(String id) {
            this.value = ModItems.ITEMS.register(id, IconItem::new);
        }

        @Override
        public RegistryObject<IconItem> getValue() {
            return value;
        }

        public static void init() {
            TerraCurio.LOGGER.info("Registering icon items");
        }
    }
}
