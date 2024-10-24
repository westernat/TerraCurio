package org.confluence.mod.terra_curio.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.CommonConfigs;
import org.confluence.mod.terra_curio.common.init.ModAttributes;

@EventBusSubscriber(modid = TerraCurio.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CommonConfigs.onLoad();
            ModAttributes.modifyAttributesUpperLimit();
        });
    }

    @SubscribeEvent
    public static void entityAttributeModification(EntityAttributeModificationEvent event) {
        ModAttributes.readJsonConfig();
        ModAttributes.registerAttribute(ModAttributes.CRIT_CHANCE, event::add);
        ModAttributes.registerAttribute(ModAttributes.RANGED_VELOCITY, event::add);
        ModAttributes.registerAttribute(ModAttributes.RANGED_DAMAGE, event::add);
        ModAttributes.registerAttribute(ModAttributes.DODGE_CHANCE, event::add);
        ModAttributes.registerAttribute(ModAttributes.AGGRO, event::add);
        if (ModList.get().isLoaded("confluence")) {
            ModAttributes.registerAttribute(ModAttributes.MAGIC_DAMAGE, event::add);
        }
        ModAttributes.registerAttribute(ModAttributes.ARMOR_PASS, event::add);
        ModAttributes.registerAttribute(ModAttributes.PICKUP_RANGE, event::add);
    }
}
