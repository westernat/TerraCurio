package org.confluence.terra_curio.client.renderer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.model.accessory.AccessoryGeoModel;
import org.joml.Matrix4f;

public class NormalBalloonGeoRenderer extends AccessoryGeoRenderer {
    public static final ResourceLocation MODEL = TerraCurio.asResource("geo/accessory/normal_balloon.geo.json");

    protected final Int2ObjectMap<BallonRenderState> states;
    protected final long seed;
    protected final RandomSource random;
    protected ResourceKey<Level> currentLevel;

    public static class BallonRenderState {
        public float x;
        public float y;
        public float z;
        public float balloonYaw;

        public void update(LivingEntity living, float entityYaw, int slotIndex) {
            float step = 0.1F + slotIndex * 0.01F;
            x += ((float) (living.getX() - living.xo) - x) * step;
            y += ((float) (living.getY() - living.yo) - y) * step;
            z += ((float) (living.getZ() - living.zo) - z) * step;
            balloonYaw = Mth.wrapDegrees(balloonYaw + Mth.wrapDegrees(entityYaw - balloonYaw) * step);
        }
    }

    public NormalBalloonGeoRenderer(ResourceLocation id) {
        super(new AccessoryGeoModel(MODEL, AccessoryGeoModel.createTextureResource(id)));
        this.states = new Int2ObjectOpenHashMap<>();
        states.defaultReturnValue(new BallonRenderState());
        this.seed = RandomSupport.generateUniqueSeed();
        this.random = RandomSource.create(seed);
    }

    @Override
    protected void actuallyRender(LivingEntity living, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, float ageInTick, int slotIndex) {
        if (living.level().dimension() != currentLevel) {
            this.currentLevel = living.level().dimension();
            states.clear();
        }
        BallonRenderState state = states.computeIfAbsent(living.getId(), id -> new BallonRenderState());
        float entityYaw = Mth.lerp(partialTick, living.yBodyRotO, living.yBodyRot);
        state.update(living, entityYaw, slotIndex);

        float yawRad = entityYaw * Mth.DEG_TO_RAD;
        float cos = Mth.cos(yawRad);
        float sin = Mth.sin(yawRad);
        float localVelX = state.x * cos + state.z * sin;
        float localVelZ = -state.x * sin + state.z * cos;
        float angleOffset = (slotIndex - 2) * 13.0F * Mth.DEG_TO_RAD;
        float staticX = Mth.cos(angleOffset);
        float staticZ = Mth.sin(angleOffset);
        float xDif = -(localVelX + staticX);
        random.setSeed(seed * living.getId());
        random.consumeCount(slotIndex);
        float randomY = 1 + random.nextFloat();
        float yDif = state.y - randomY + Mth.sin(staticX + ageInTick * 0.05F) * 0.125F;
        float zDif = -(localVelZ + staticZ);
        Vec3 ropeGripPosition = living.getRopeHoldPosition(partialTick);

        poseStack.mulPose(Axis.YN.rotationDegrees(Mth.wrapDegrees(state.balloonYaw - entityYaw)));
        poseStack.translate(-xDif, -yDif + (float) (ropeGripPosition.y - Mth.lerp(partialTick, living.yo, living.getY())), -zDif);
        super.actuallyRender(living, poseStack, bufferSource, packedLight, partialTick, ageInTick, slotIndex);

        float offsetMod = Mth.invSqrt(xDif * xDif + zDif * zDif) * 0.0125F;
        float xOffset = zDif * offsetMod;
        float zOffset = xDif * offsetMod;
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.leash());
        BlockPos holderEyePos = BlockPos.containing(living.getEyePosition(partialTick));
        int holderBlockLight = living.isOnFire() ? 15 : living.level().getBrightness(LightLayer.BLOCK, holderEyePos);
        int holderSkyLight = living.level().getBrightness(LightLayer.SKY, holderEyePos);

        poseStack.pushPose();

        Matrix4f posMatrix = poseStack.last().pose();

        for (int segment = 0; segment <= 24; ++segment) {
            renderLeashPiece(buffer, posMatrix, xDif, yDif, zDif, holderBlockLight, holderSkyLight, 0.025f, xOffset, zOffset, segment, false);
        }

        for (int segment = 24; segment >= 0; --segment) {
            renderLeashPiece(buffer, posMatrix, xDif, yDif, zDif, holderBlockLight, holderSkyLight, 0.0f, xOffset, zOffset, segment, true);
        }

        poseStack.popPose();
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
