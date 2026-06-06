package org.confluence.terra_curio.common.item;

import PortLib.extensions.net.minecraft.world.item.Item.PortItemExtension;
import net.minecraft.world.item.Item;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.common.component.ModRarity;

public class MasterItem extends Item {
    public MasterItem() {
        super(PortItemExtension.Properties.component(new Properties(), ConfluenceMagicLib.MOD_RARITY, ModRarity.MASTER));
    }
}
