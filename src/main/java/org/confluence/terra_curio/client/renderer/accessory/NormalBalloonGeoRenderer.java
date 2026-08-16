package org.confluence.terra_curio.client.renderer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredItem;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.model.accessory.AccessoryGeoModel;
import org.confluence.terra_curio.common.init.TCItems;
import org.joml.Matrix4f;

public class NormalBalloonGeoRenderer extends AccessoryGeoRenderer {
    public static final ResourceLocation MODEL = TerraCurio.asResource("geo/accessory/normal_balloon.geo.json");

    // Balloon type flags — each bit represents a balloon variant
    protected static final int FLAG_CLOUD = 1;
    protected static final int FLAG_BLIZZARD = 2;
    protected static final int FLAG_SANDSTORM = 4;
    protected static final int FLAG_FART = 8;
    protected static final int FLAG_HONEY = 16;
    protected static final int FLAG_SHINY_RED = 32;
    protected static final int FLAG_PUFFERFISH = 64;
    protected static final int FLAG_SHARKRON = 128;

    // Meta flags — above 0xFF, not counted as sub-balloons
    protected static final int FLAG_HORSESHOE = 256;
    protected static final int FLAG_PINK_HORSESHOE = 512;
    protected static final int FLAG_WHITE_HORSESHOE = 1024;
    protected static final int BALLOON_MASK = 0xFF;

    protected static final Reference2IntOpenHashMap<Item> BALLOON_FLAGS = new Reference2IntOpenHashMap<>();
    protected static final Int2ObjectMap<ResourceLocation> SUB_TEXTURES = new Int2ObjectOpenHashMap<>();
    protected static final Int2ObjectMap<RenderType> SUB_RENDER_TYPES = new Int2ObjectOpenHashMap<>();
    protected static final ResourceLocation HORSESHOE_MODEL_PATH = TerraCurio.asResource("geo/accessory/horseshoe.geo.json");

    public static void registerAll() {
        registerBalloon(TCItems.CLOUD_IN_A_BALLOON, FLAG_CLOUD);
        registerBalloon(TCItems.BLIZZARD_IN_A_BALLOON, FLAG_BLIZZARD);
        registerBalloon(TCItems.SANDSTORM_IN_A_BALLOON, FLAG_SANDSTORM);
        registerBalloon(TCItems.FART_IN_A_BALLOON, FLAG_FART);
        registerBalloon(TCItems.HONEY_BALLOON, FLAG_HONEY);
        registerBalloon(TCItems.SHINY_RED_BALLOON, FLAG_SHINY_RED);
        registerBalloon(TCItems.BALLOON_PUFFERFISH, FLAG_PUFFERFISH);
        registerBalloon(TCItems.SHARKRON_BALLOON, FLAG_SHARKRON);

        // Bundles — composite flags
        BALLOON_FLAGS.put(TCItems.BUNDLE_OF_BALLOONS.get(), FLAG_CLOUD | FLAG_BLIZZARD | FLAG_SANDSTORM);
        BALLOON_FLAGS.put(TCItems.BUNDLE_OF_HORSESHOE_BALLOONS.get(), FLAG_CLOUD | FLAG_BLIZZARD | FLAG_SANDSTORM | FLAG_HORSESHOE);

        // Horseshoe balloons — base balloon flag + HORSESHOE meta
        BALLOON_FLAGS.put(TCItems.BLUE_HORSESHOE_BALLOON.get(), FLAG_CLOUD | FLAG_HORSESHOE);
        BALLOON_FLAGS.put(TCItems.WHITE_HORSESHOE_BALLOON.get(), FLAG_BLIZZARD | FLAG_HORSESHOE | FLAG_WHITE_HORSESHOE);
        BALLOON_FLAGS.put(TCItems.YELLOW_HORSESHOE_BALLOON.get(), FLAG_SANDSTORM | FLAG_HORSESHOE);
        BALLOON_FLAGS.put(TCItems.GREEN_HORSESHOE_BALLOON.get(), FLAG_FART | FLAG_HORSESHOE);
        BALLOON_FLAGS.put(TCItems.PINK_HORSESHOE_BALLOON.get(), FLAG_SHARKRON | FLAG_HORSESHOE | FLAG_PINK_HORSESHOE);
        BALLOON_FLAGS.put(TCItems.AMBER_HORSESHOE_BALLOON.get(), FLAG_HONEY | FLAG_HORSESHOE);
    }

    protected static void registerBalloon(DeferredItem<?> item, int flag) {
        BALLOON_FLAGS.put(item.get(), flag);
        SUB_TEXTURES.put(flag, AccessoryGeoModel.createTextureResource(item.getId()));
    }

    protected final long seed;
    protected final RandomSource random;
    protected ResourceKey<Level> currentLevel;
    protected AccessoryGeoModel horseshoeModel;
    protected RenderType horseshoeRenderType;

    public NormalBalloonGeoRenderer(ResourceLocation id) {
        this(new AccessoryGeoModel(MODEL, AccessoryGeoModel.createTextureResource(id)));
    }

    public NormalBalloonGeoRenderer(AccessoryGeoModel model) {
        super(model);
        this.seed = RandomSupport.generateUniqueSeed();
        this.random = RandomSource.create(seed);
    }

    public NormalBalloonGeoRenderer setHorseshoe(ResourceLocation horseshoeTextureId) {
        ResourceLocation texture = AccessoryGeoModel.createTextureResource(horseshoeTextureId);
        this.horseshoeModel = new AccessoryGeoModel(HORSESHOE_MODEL_PATH, texture);
        this.horseshoeRenderType = RenderType.armorCutoutNoCull(texture);
        return this;
    }

    @Override
    protected void actuallyRender(LivingEntity living, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, float ageInTick, int slotIndex) {
        ItemStack itemStack = this.stack;
        int flags = itemStack.isEmpty() ? 0 : BALLOON_FLAGS.getInt(itemStack.getItem());
        if (flags == 0) return;

        int balloonFlags = flags & BALLOON_MASK;
        boolean isHorseshoe = (flags & FLAG_HORSESHOE) != 0;

        if (living.level().dimension() != currentLevel) {
            this.currentLevel = living.level().dimension();
            BalloonPhysicsGroup.reset();
        }

        // Expand balloon-type flags into individual sub-balloon indices
        int subCount = Integer.bitCount(balloonFlags);
        int[] subFlags = new int[subCount];
        int idx = 0;
        for (int f = 0; f <= 7; f++) {
            int bit = 1 << f;
            if ((balloonFlags & bit) != 0) subFlags[idx++] = bit;
        }

        BalloonPhysicsGroup group = BalloonPhysicsGroup.getOrCreate(living.getId());
        group.step(living, partialTick);

        float entityYaw = Mth.lerp(partialTick, living.yBodyRotO, living.yBodyRot);
        float yawRad = entityYaw * Mth.DEG_TO_RAD;
        float cos = Mth.cos(yawRad);
        float sin = Mth.sin(yawRad);

        random.setSeed(seed * living.getId());
        Vec3 ropeGripPosition = living.getRopeHoldPosition(partialTick);
        float ropeYOffset = (float) (ropeGripPosition.y - Mth.lerp(partialTick, living.yo, living.getY()));

        for (int si = 0; si < subCount; si++) {
            BalloonPhysicsGroup.BallonRenderState state = group.getSubState(slotIndex, si, subCount);

            float localX = state.posX * cos + state.posZ * sin;
            float localZ = -state.posX * sin + state.posZ * cos;
            float xDif = -localX;
            float zDif = -localZ;

            random.consumeCount(slotIndex * 16 + si);
            float randomY = 1 + random.nextFloat();
            float yDif = state.y - randomY + Mth.sin((slotIndex + si + 1) * 1.3F + ageInTick * 0.05F) * 0.125F;

            // Render balloon model with sub-balloon texture
            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees(Mth.wrapDegrees(state.balloonYaw - entityYaw)));
            poseStack.translate(-xDif, -yDif + ropeYOffset, -zDif);

            int subFlag = subFlags[si];
            RenderType subType = SUB_RENDER_TYPES.computeIfAbsent(subFlag, f -> RenderType.armorCutoutNoCull(SUB_TEXTURES.get(f)));
            VertexConsumer buffer = bufferSource.getBuffer(subType);
            for (var bone : geoModel.getBakedModel().topLevelBones()) {
                renderRecursively(poseStack, bone, buffer, packedLight);
            }

            // Render leash for this sub-balloon
            float offsetMod = Mth.invSqrt(xDif * xDif + zDif * zDif) * 0.0125F;
            float xOffset = zDif * offsetMod;
            float zOffset = xDif * offsetMod;
            VertexConsumer leashBuffer = bufferSource.getBuffer(RenderType.leash());
            BlockPos holderEyePos = BlockPos.containing(living.getEyePosition(partialTick));
            int holderBlockLight = living.isOnFire() ? 15 : living.level().getBrightness(LightLayer.BLOCK, holderEyePos);
            int holderSkyLight = living.level().getBrightness(LightLayer.SKY, holderEyePos);

            poseStack.pushPose();
            Matrix4f posMatrix = poseStack.last().pose();
            for (int segment = 0; segment <= 24; ++segment) {
                renderLeashPiece(leashBuffer, posMatrix, xDif, yDif, zDif, holderBlockLight, holderSkyLight, 0.025f, xOffset, zOffset, segment, false);
            }
            for (int segment = 24; segment >= 0; --segment) {
                renderLeashPiece(leashBuffer, posMatrix, xDif, yDif, zDif, holderBlockLight, holderSkyLight, 0.0f, xOffset, zOffset, segment, true);
            }
            poseStack.popPose();

            poseStack.popPose();

            // Render horseshoe overlay
            if (isHorseshoe) {
                VertexConsumer hsBuffer = bufferSource.getBuffer(horseshoeRenderType);
                for (var bone : horseshoeModel.getBakedModel().topLevelBones()) {
                    renderRecursively(poseStack, bone, hsBuffer, packedLight);
                }
            }
        }
    }

    protected static void renderLeashPiece(
            VertexConsumer buffer, Matrix4f poseStack,
            float xDif, float yDif, float zDif,
            int holderBlockLight, int holderSkyLight,
            float yOffset, float xOffset, float zOffset,
            int segment, boolean isLeashKnot
    ) {
        float piecePosPercent = segment / 24f;
        int packedLight = LightTexture.pack(holderBlockLight, holderSkyLight);
        float knotColourMod = segment % 2 == (isLeashKnot ? 1 : 0) ? 0.7f : 1f;
        float red = 0.5f * knotColourMod;
        float green = 0.4f * knotColourMod;
        float blue = 0.3f * knotColourMod;
        float x = xDif * piecePosPercent;
        float y = yDif > 0.0f ? yDif * piecePosPercent * piecePosPercent : yDif - yDif * (1.0f - piecePosPercent) * (1.0f - piecePosPercent);
        float z = zDif * piecePosPercent;

        buffer.addVertex(poseStack, x - xOffset, y + yOffset, z + zOffset).setColor(red, green, blue, 1).setLight(packedLight);
        buffer.addVertex(poseStack, x + xOffset, y + (float) 0.025 - yOffset, z - zOffset).setColor(red, green, blue, 1).setLight(packedLight);
    }
}
