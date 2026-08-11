package org.confluence.terra_curio.client.renderer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredItem;
import org.confluence.terra_curio.client.model.accessory.AccessoryGeoModel;
import org.confluence.terra_curio.common.init.TCItems;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtil;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LayeredGeoRenderer extends AccessoryGeoRenderer {
    protected static final Reference2ObjectMap<Item, ObjectIntPair<Layer>> RENDER_LAYERS = new Reference2ObjectOpenHashMap<>();
    protected static final Object2ObjectMap<UUID, Object2ObjectMap<Layer, Item>> RENDERED_LAYERS = new Object2ObjectOpenHashMap<>();

    protected final Layer layer;
    protected ResourceKey<Level> currentLevel;

    public LayeredGeoRenderer(Layer layer, AccessoryGeoModel model) {
        super(model);
        this.layer = layer;
    }

    public static void registerAll() {
        register(TCItems.BLINDFOLD, Layer.HEAD_ALL, 0);
        // 骸骨头盔 100
        register(TCItems.DIVING_GEAR, Layer.HEAD_ALL, 200);
        register(TCItems.JELLYFISH_DIVING_GEAR, Layer.HEAD_ALL, 300);
        register(TCItems.ARCTIC_DIVING_GEAR, Layer.HEAD_ALL, 400);

        // 大自然的恩赐 0
        // 奥术花 100
        register(TCItems.OBSIDIAN_ROSE, Layer.HEAD_TOP, 200);

        register(TCItems.SHACKLE, Layer.DOUBLE_HAND, 0);
        register(TCItems.CLIMBING_CLAWS, Layer.DOUBLE_HAND, 100);
        register(TCItems.HAND_WARMER, Layer.DOUBLE_HAND, 200);
        register(TCItems.FROG_WEBBING, Layer.DOUBLE_HAND, 300);
        register(TCItems.FERAL_CLAWS, Layer.DOUBLE_HAND, 400);
        register(TCItems.BONE_GLOVE, Layer.DOUBLE_HAND, 500);
        register(TCItems.POWER_GLOVE, Layer.DOUBLE_HAND, 600);
        register(TCItems.TITAN_GLOVE, Layer.DOUBLE_HAND, 700);
        register(TCItems.MECHANICAL_GLOVE, Layer.DOUBLE_HAND, 800);
        register(TCItems.FIRE_GAUNTLET, Layer.DOUBLE_HAND, 900);
        register(TCItems.BERSERKERS_GLOVE, Layer.DOUBLE_HAND, 1000);
        // 魔法手铐 1100
        // 天界手铐 1200

        register(TCItems.COPPER_WATCH, Layer.RIGHT_HAND, 0);
        register(TCItems.TIN_WATCH, Layer.RIGHT_HAND, 10);
        register(TCItems.SILVER_WATCH, Layer.RIGHT_HAND, 30);
        register(TCItems.TUNGSTEN_WATCH, Layer.RIGHT_HAND, 40);
        register(TCItems.GOLD_WATCH, Layer.RIGHT_HAND, 50);
        register(TCItems.PLATINUM_WATCH, Layer.RIGHT_HAND, 60);
        register(TCItems.BAND_OF_REGENERATION, Layer.RIGHT_HAND, 100);
        // 星力手环 200
        // 神话护身符 300

        register(TCItems.LAVA_CHARM, Layer.RIGHT_ARM, 0);
        register(TCItems.MOON_STONE, Layer.RIGHT_ARM, 100);
        register(TCItems.SUN_STONE, Layer.RIGHT_ARM, 200);

        register(TCItems.COBALT_SHIELD, Layer.SHIELD, 0);
        register(TCItems.OBSIDIAN_SHIELD, Layer.SHIELD, 100);
        register(TCItems.PALADINS_SHIELD, Layer.SHIELD, 200);
        register(TCItems.HERO_SHIELD, Layer.SHIELD, 300);
        register(TCItems.FROZEN_SHIELD, Layer.SHIELD, 400);
        register(TCItems.ANKH_SHIELD, Layer.SHIELD, 500);
//        register(TCItems.SHIELD_OF_CTHULHU, Layer.SHIELD, 600);

        for (Map.Entry<Item, ObjectIntPair<Layer>> entry : RENDER_LAYERS.entrySet()) {
            CuriosRendererRegistry.register(entry.getKey(), () -> new LayeredGeoRenderer(entry.getValue().key(), new AccessoryGeoModel(entry.getKey().builtInRegistryHolder().unwrapKey().orElseThrow().location())));
        }
    }

    protected static void register(DeferredItem<?> item, Layer layer, int order) {
        RENDER_LAYERS.put(item.get(), new ObjectIntImmutablePair<>(layer, order));
    }

    /// [GeoArmorRenderer#applyBaseTransformations]
    public enum Layer {
        HEAD_ALL {
            @Override
            public void transform(HumanoidModel<?> baseModel, BakedGeoModel bakedModel) {
                head(baseModel, bakedModel);
            }
        },
        HEAD_TOP {
            @Override
            public void transform(HumanoidModel<?> baseModel, BakedGeoModel bakedModel) {
                head(baseModel, bakedModel);
            }
        },
        DOUBLE_HAND {
            @Override
            public void transform(HumanoidModel<?> baseModel, BakedGeoModel bakedModel) {
                rightArm(baseModel, bakedModel);
                leftArm(baseModel, bakedModel);
            }
        },
        RIGHT_HAND {
            @Override
            public void transform(HumanoidModel<?> baseModel, BakedGeoModel bakedModel) {
                rightArm(baseModel, bakedModel);
            }
        },
        RIGHT_ARM {
            @Override
            public void transform(HumanoidModel<?> baseModel, BakedGeoModel bakedModel) {
                rightArm(baseModel, bakedModel);
            }
        },
        SHIELD {
            @Override
            public void transform(HumanoidModel<?> baseModel, BakedGeoModel bakedModel) {
                leftArm(baseModel, bakedModel);
            }
        };

        private static void head(HumanoidModel<?> baseModel, BakedGeoModel bakedModel) {
            bakedModel.getBone("armorHead").ifPresent(head -> {
                ModelPart headPart = baseModel.head;
                RenderUtil.matchModelPartRot(headPart, head);
                head.updatePosition(headPart.x, -headPart.y, headPart.z);
            });
        }

        private static void rightArm(HumanoidModel<?> baseModel, BakedGeoModel bakedModel) {
            bakedModel.getBone("armorRightArm").ifPresent(rightArm -> {
                ModelPart rightArmPart = baseModel.rightArm;
                RenderUtil.matchModelPartRot(rightArmPart, rightArm);
                rightArm.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z);
            });
        }

        private static void leftArm(HumanoidModel<?> baseModel, BakedGeoModel bakedModel) {
            bakedModel.getBone("armorLeftArm").ifPresent(leftArm -> {
                ModelPart leftArmPart = baseModel.leftArm;
                RenderUtil.matchModelPartRot(leftArmPart, leftArm);
                leftArm.updatePosition(leftArmPart.x - 5, 2 - leftArmPart.y, leftArmPart.z);
            });
        }

        public abstract void transform(HumanoidModel<?> baseModel, BakedGeoModel bakedModel);
    }

    @Override
    protected boolean shouldRender(ItemStack stack, SlotContext slotContext) {
        LivingEntity living = slotContext.entity();
        if (living.level().dimension() != currentLevel) {
            this.currentLevel = living.level().dimension();
            RENDERED_LAYERS.clear();
        }
        if (super.shouldRender(stack, slotContext)) {
            Object2ObjectMap<Layer, Item> layers = RENDERED_LAYERS.get(living.getUUID());
            return layers != null && layers.get(layer) == stack.getItem();
        }
        return false;
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource bufferSource, int packedLight, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (shouldRender(stack, slotContext)) {
            poseStack.pushPose();
            BakedGeoModel bakedModel = geoModel.getBakedModel();
            if (renderLayerParent.getModel() instanceof HumanoidModel<?> baseModel) {
                layer.transform(baseModel, bakedModel);
            }
            poseStack.scale(-1, -1, 1);
            poseStack.translate(0, -1.501F, 0);

            VertexConsumer buffer = bufferSource.getBuffer(renderType);
            for (GeoBone bone : bakedModel.topLevelBones()) {
                renderRecursively(poseStack, bone, buffer, packedLight);
            }

            poseStack.popPose();
        }
    }

    public static void handlePacket(Player player, int entityId) {
        Entity entity = player.level().getEntity(entityId);
        if (!(entity instanceof LivingEntity living)) return;
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(living);
        if (curiosInventory.isEmpty()) return;
        Object2ObjectMap<Layer, Item> layers = RENDERED_LAYERS.computeIfAbsent(living.getUUID(), id -> new Object2ObjectOpenHashMap<>());
        for (ICurioStacksHandler curioStacksHandler : curiosInventory.get().getCurios().values()) {
            IDynamicStackHandler stackHandler = curioStacksHandler.getStacks();
            for (int i = 0; i < stackHandler.getSlots(); i++) {
                ItemStack stack = stackHandler.getStackInSlot(i);
                if (stack.isEmpty()) continue;
                Item item = stack.getItem();
                ObjectIntPair<Layer> current = RENDER_LAYERS.get(item);
                if (current == null) continue;
                Layer layer = current.left();
                ObjectIntPair<Layer> previous = RENDER_LAYERS.get(layers.get(layer));
                if (previous == null || current.rightInt() >= previous.rightInt()) {
                    layers.put(layer, item);
                }
            }
        }
    }
}
