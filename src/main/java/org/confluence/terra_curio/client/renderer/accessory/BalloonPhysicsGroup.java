package org.confluence.terra_curio.client.renderer.accessory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class BalloonPhysicsGroup {
    private static final Int2ObjectMap<BalloonPhysicsGroup> GROUPS = new Int2ObjectOpenHashMap<>();
    private static final int MAX_GROUPS = 128;

    private static final float SPRING_STIFFNESS = 0.15F;
    private static final float SPRING_DAMPING = 0.5F;
    private static final float COLLISION_RADIUS = 6 / 16F;
    private static final float COLLISION_STIFFNESS = 0.2F;
    private static final float MAX_DT = 2.0F;
    private static final float MAX_OFFSET = 1.5F;
    private static final float MIN_SEPARATION = 0.05F;
    private static final float MAX_ANCHOR_DELTA_SQ = 4.0F;
    private static final float ANGLE_STEP = 13.0F * Mth.DEG_TO_RAD;

    private final Int2ObjectMap<BallonRenderState> states = new Int2ObjectOpenHashMap<>();
    private double lastEntityX;
    private double lastEntityZ;
    private long lastStepNs;
    private boolean firstStep = true;

    public static class BallonRenderState {
        public final int slotIndex;
        public float x, y, z;
        public float balloonYaw;
        public float posX, posZ;
        public float velX, velZ;
        public float restX, restZ;

        BallonRenderState(int slotIndex) {
            this.slotIndex = slotIndex;
        }

        void initRestOffsets() {
            float angle = (slotIndex - 2) * ANGLE_STEP;
            this.restX = Mth.cos(angle);
            this.restZ = Mth.sin(angle);
        }
    }

    public static BalloonPhysicsGroup getOrCreate(int entityId) {
        BalloonPhysicsGroup group = GROUPS.get(entityId);
        if (group == null) {
            if (GROUPS.size() >= MAX_GROUPS) {
                GROUPS.clear();
            }
            group = new BalloonPhysicsGroup();
            GROUPS.put(entityId, group);
        }
        return group;
    }

    public static void reset() {
        GROUPS.clear();
    }

    public BallonRenderState getState(int slotIndex) {
        return getSubState(slotIndex, 0, 1);
    }

    public BallonRenderState getSubState(int slotIndex, int subIndex, int subCount) {
        int key = slotIndex * 16 + subIndex;
        return states.computeIfAbsent(key, k -> {
            BallonRenderState state = new BallonRenderState(slotIndex);
            float baseAngle = (slotIndex - 2) * ANGLE_STEP;
            float offset = (subIndex - (subCount - 1) / 2.0f) * 10.0F * Mth.DEG_TO_RAD;
            float angle = baseAngle + offset;
            state.restX = Mth.cos(angle);
            state.restZ = Mth.sin(angle);
            return state;
        });
    }

    public void step(LivingEntity living, float partialTick) {
        long now = System.nanoTime();
        if (now - lastStepNs < 1_000_000L) return; // 1ms cooldown: once per frame, skip double-calls
        lastStepNs = now;
        float dt = Mth.clamp(Minecraft.getInstance().getTimer().getRealtimeDeltaTicks(), 0.0F, MAX_DT);

        float entityYaw = Mth.lerp(partialTick, living.yBodyRotO, living.yBodyRot);
        float yawRad = entityYaw * Mth.DEG_TO_RAD;
        float cosY = Mth.cos(yawRad);
        float sinY = Mth.sin(yawRad);

        double entityX = Mth.lerp(partialTick, living.xo, living.getX());
        double entityZ = Mth.lerp(partialTick, living.zo, living.getZ());

        if (firstStep) {
            lastEntityX = entityX;
            lastEntityZ = entityZ;
            firstStep = false;
        }

        double anchorDx = entityX - lastEntityX;
        double anchorDz = entityZ - lastEntityZ;
        double anchorDistSq = anchorDx * anchorDx + anchorDz * anchorDz;

        if (anchorDistSq > MAX_ANCHOR_DELTA_SQ) {
            for (BallonRenderState s : states.values()) {
                float targetX = s.restX * cosY - s.restZ * sinY + s.x;
                float targetZ = s.restX * sinY + s.restZ * cosY + s.z;
                s.posX = targetX;
                s.posZ = targetZ;
                s.velX = 0;
                s.velZ = 0;
            }
        } else {
            float dx = (float) anchorDx;
            float dz = (float) anchorDz;
            for (BallonRenderState s : states.values()) {
                s.posX += dx;
                s.posZ += dz;
            }
        }
        lastEntityX = entityX;
        lastEntityZ = entityZ;

        for (BallonRenderState s : states.values()) {
            float step = 0.1F + s.slotIndex * 0.01F;
            s.x += ((float) (living.getX() - living.xo) - s.x) * step;
            s.y += ((float) (living.getY() - living.yo) - s.y) * step;
            s.z += ((float) (living.getZ() - living.zo) - s.z) * step;
            s.balloonYaw = Mth.wrapDegrees(s.balloonYaw + Mth.wrapDegrees(entityYaw - s.balloonYaw) * step);
        }

        int count = states.size();
        if (count < 2) {
            for (BallonRenderState s : states.values()) {
                float targetX = s.restX * cosY - s.restZ * sinY + s.x;
                float targetZ = s.restX * sinY + s.restZ * cosY + s.z;
                float fx = -(s.posX - targetX) * SPRING_STIFFNESS - s.velX * SPRING_DAMPING;
                float fz = -(s.posZ - targetZ) * SPRING_STIFFNESS - s.velZ * SPRING_DAMPING;
                s.velX += fx * dt;
                s.velZ += fz * dt;
                s.posX += s.velX * dt;
                s.posZ += s.velZ * dt;
                clampPosition(s);
            }
            return;
        }

        BallonRenderState[] arr = states.values().toArray(new BallonRenderState[0]);

        for (int i = 0; i < count; i++) {
            BallonRenderState s = arr[i];
            float targetX = s.restX * cosY - s.restZ * sinY + s.x;
            float targetZ = s.restX * sinY + s.restZ * cosY + s.z;
            float fx = -(s.posX - targetX) * SPRING_STIFFNESS - s.velX * SPRING_DAMPING;
            float fz = -(s.posZ - targetZ) * SPRING_STIFFNESS - s.velZ * SPRING_DAMPING;
            s.velX += fx * dt;
            s.velZ += fz * dt;
        }

        float collisionDiameter = 2.0F * COLLISION_RADIUS;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                BallonRenderState a = arr[i];
                BallonRenderState b = arr[j];
                float diffX = a.posX - b.posX;
                float diffZ = a.posZ - b.posZ;
                float distSq = diffX * diffX + diffZ * diffZ;
                if (distSq < collisionDiameter * collisionDiameter && distSq > 0.0001F) {
                    float dist = (float) Math.sqrt(distSq);
                    if (dist < MIN_SEPARATION) dist = MIN_SEPARATION;
                    float penetration = collisionDiameter - dist;
                    float push = COLLISION_STIFFNESS * penetration / dist;
                    float pushX = diffX * push * dt;
                    float pushZ = diffZ * push * dt;
                    a.velX += pushX;
                    a.velZ += pushZ;
                    b.velX -= pushX;
                    b.velZ -= pushZ;
                }
            }
        }

        for (BallonRenderState s : arr) {
            s.posX += s.velX * dt;
            s.posZ += s.velZ * dt;
            clampPosition(s);
        }
    }

    private static void clampPosition(BallonRenderState s) {
        float distSq = s.posX * s.posX + s.posZ * s.posZ;
        if (distSq > MAX_OFFSET * MAX_OFFSET) {
            float scale = MAX_OFFSET / (float) Math.sqrt(distSq);
            s.posX *= scale;
            s.posZ *= scale;
            s.velX = 0;
            s.velZ = 0;
        }
    }
}
