package org.confluence.mod.terra_curio.common.init;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.capability.CuriosAbility;
import org.confluence.mod.terra_curio.common.capability.CuriosAbilityProvider;

public class ModCapability {
    public static final EntityCapability<CuriosAbility,Void> CURIOS_HANDLE =
            EntityCapability.createVoid(TerraCurio.asResource("terra_curios_ability"), CuriosAbility.class);


    @EventBusSubscriber(modid = TerraCurio.MODID,bus = EventBusSubscriber.Bus.MOD)
    public static class ModEventBus{
        @SubscribeEvent
        private static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerEntity(CURIOS_HANDLE, EntityType.PLAYER, new CuriosAbilityProvider());
        }
    }

}
