package org.confluence.terra_curio.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.level.ItemLike;
import org.confluence.terra_curio.client.model.accessory.*;
import org.confluence.terra_curio.client.renderer.accessory.*;
import org.confluence.terra_curio.common.init.TCItems;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class CuriosClient {
    public static void registerRenderers() {
        registerRenderer(TCItems.WORM_SCARF, WormScarfRenderer::new);
        registerRenderer(TCItems.TERRASPARK_BOOTS, TerrasparkBootsRenderer::new);
        registerRenderer(TCItems.DUNERIDER_BOOTS, DuneriderBootsRenderer::new);
        registerRenderer(TCItems.MAGMA_SKULL, MagmaSkullRenderer::new);
        registerRenderer(TCItems.FLURRY_BOOTS, FlurryBootsRenderer::new);
        registerRenderer(TCItems.HERMES_BOOTS, HermesBootsRenderer::new);
//        registerRenderer(TCItems.OBSIDIAN_SKULL, ObsidianSkullRenderer::new);
        registerRenderer(TCItems.SHIELD_OF_CTHULHU, ShieldOfCthulhuRenderer::new);
        registerRenderer(TCItems.NEPTUNES_SHELL, MermanRenderer::new);
        registerRenderer(TCItems.MOON_CHARM, WerewolfRenderer::new);
        registerRenderer(TCItems.MOON_SHELL, MoonShellRenderer::new);
        registerRenderer(TCItems.CELESTIAL_SHELL, MoonShellRenderer::new);
    }

    private static void registerRenderer(ItemLike item, Function<EntityModelSet, ICurioRenderer> factory) {
        ICurioRenderer.register(item.asItem(), () -> factory.apply(Minecraft.getInstance().getEntityModels()));
    }

    public static void registerLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> layerDefinition) {
        layerDefinition.accept(WormScarfModel.LAYER_LOCATION, WormScarfModel::createBodyLayer);
        layerDefinition.accept(TerrasparkBootsModel.LAYER_LOCATION, TerrasparkBootsModel::createBodyLayer);
        layerDefinition.accept(DuneriderBootsModel.LAYER_LOCATION, DuneriderBootsModel::createBodyLayer);
        layerDefinition.accept(MagmaSkullModel.LAYER_LOCATION, MagmaSkullModel::createBodyLayer);
        layerDefinition.accept(FlurryBootsModel.LAYER_LOCATION, FlurryBootsModel::createBodyLayer);
        layerDefinition.accept(HermesBootsModel.LAYER_LOCATION, HermesBootsModel::createBodyLayer);
        //layerDefinition.accept(ObsidianSkullModel.LAYER_LOCATION, ObsidianSkullModel::createBodyLayer);
        layerDefinition.accept(ShieldOfCthulhuModel.LAYER_LOCATION, ShieldOfCthulhuModel::createBodyLayer);
        layerDefinition.accept(MermanModel.LAYER_LOCATION, MermanModel::createBodyLayer);
        layerDefinition.accept(WerewolfModel.LAYER_LOCATION, WerewolfModel::createBodyLayer);
    }
}
