package org.confluence.mod.terra_curio.common.event;

import net.minecraft.network.chat.MutableComponent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent.GatherComponents;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.init.ModDataComponentTypes;
import org.confluence.mod.terra_curio.common.misc.ModRarity;

@EventBusSubscriber(modid = TerraCurio.MOD_ID,bus = EventBusSubscriber.Bus.GAME)
public class renderDisplayNameEventSubscriber {
    @SubscribeEvent
    public static void renderDisplayNameEvent(GatherComponents event){
        ModRarity rarity = event.getItemStack().getComponents().get(ModDataComponentTypes.MOD_RARITY.get());
        if(rarity==null ) return;
        event.getTooltipElements().getFirst().left().ifPresent(text->
                ((MutableComponent)text).setStyle(rarity.style));
    }
}
