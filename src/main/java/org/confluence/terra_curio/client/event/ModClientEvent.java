package org.confluence.terra_curio.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.model.entity.BeeProjectileModel;
import org.confluence.terra_curio.client.model.entity.StepStoolModel;
import org.confluence.terra_curio.client.renderer.entity.BeeProjectileRenderer;
import org.confluence.terra_curio.client.renderer.entity.StarCloakEntityRenderer;
import org.confluence.terra_curio.client.renderer.entity.StepStoolRenderer;
import org.confluence.terra_curio.common.init.TCEntities;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = TerraCurio.MODID, value = Dist.CLIENT)
public final class ModClientEvent {
    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BeeProjectileModel.LAYER_LOCATION, BeeProjectileModel::createBodyLayer);
        event.registerLayerDefinition(StepStoolModel.LAYER_LOCATION, StepStoolModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TCEntities.BEE_PROJECTILE.get(), BeeProjectileRenderer::new);
        event.registerEntityRenderer(TCEntities.STEP_STOOL.get(), StepStoolRenderer::new);
        event.registerEntityRenderer(TCEntities.STAR_CLOAK.get(), StarCloakEntityRenderer::new);
    }
}
