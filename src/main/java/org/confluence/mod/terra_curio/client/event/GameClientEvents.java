package org.confluence.mod.terra_curio.client.event;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.client.animate.ExpertColorAnimation;
import org.confluence.mod.terra_curio.client.animate.MasterColorAnimation;

@EventBusSubscriber(modid = TerraCurio.MODID,value = Dist.CLIENT)
public final class GameClientEvents {
    @SubscribeEvent
    public static void clientTick$Post(ClientTickEvent.Pre event) {
        ExpertColorAnimation.INSTANCE.updateColor();
        MasterColorAnimation.INSTANCE.updateColor();
    }
}
