package org.confluence.terra_curio.common.item;

import net.minecraft.world.item.Item;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;

public class MasterItem extends Item {
    public MasterItem() {
        super(new Properties().component(TCDataComponentTypes.MOD_RARITY, ModRarity.MASTER));
    }
}
