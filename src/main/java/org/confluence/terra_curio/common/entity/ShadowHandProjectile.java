package org.confluence.terra_curio.common.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

// todo
public class ShadowHandProjectile extends Projectile {
    private final float[] ai = new float[2];
    private float rotation;
    private float yaw;

    public ShadowHandProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

//    public ShadowHandProjectile(Level level, LivingEntity owner, float roll, float yaw, float[] ai) {
//        super(entityType, level);
//        setOwner(owner);
//        this.rotation = roll;
//        this.yaw = yaw;
//        this.ai[0] = ai[0];
//        this.ai[1] = ai[1];
//    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public void baseTick() {
        super.baseTick();

        int variation = 0;
        float ai0 = ai[0];
        float counterMax;

        AI_187_ShadowHand_GetVariation:
        {
            float num = 0f;
            float num2 = counterMax = 180f;
            if (ai0 >= num && ai0 < num2) {
                break AI_187_ShadowHand_GetVariation;
            }
            num = num2;
            num2 += 120f;
            if (ai0 >= num && ai0 < num2) {
                variation = 1;
                ai0 -= num;
                counterMax = num2 - num;
                break AI_187_ShadowHand_GetVariation;
            }
            num = num2;
            num2 += 90f;
            if (ai0 >= num && ai0 < num2) {
                variation = 2;
                ai0 -= num;
                counterMax = num2 - num;
            }
            num = num2;
            num2 += 90f;
            if (ai0 >= num && ai0 < num2) {
                variation = 3;
                ai0 -= num;
                counterMax = num2 - num;
            }
        }

        float fromValue = ai0 / counterMax;
//        SlotId val;
//        if (ai0 == 0f) {
//            float[] array = localAI;
//            val = SoundEngine.PlayTrackedSound(SoundID.DD2_GhastlyGlaiveImpactGhost, base.Center);
//            array[1] = ((SlotId)(ref val)).ToFloat();
//        }
//        ActiveSound activeSound = SoundEngine.GetActiveSound(SlotId.FromFloat(localAI[1]));
//        if (activeSound == null) {
//            float[] array2 = localAI;
//            val = SlotId.Invalid;
//            array2[1] = ((SlotId)(ref val)).ToFloat();
//        } else {
//            activeSound.Position = base.Center;
//        }
//        float num = counterMax - 15f;
//        if (ai0 > num) {
//            alpha += 25;
//            if (alpha > 255) {
//                alpha = 255;
//            }
//        } else {
//            alpha -= 25;
//            if (alpha < 50) {
//                alpha = 50;
//            }
//        }
        if (ai0 > counterMax) {
            discard();
            return;
        }

//        Vector3d velocity = new Vector3d(getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
//
//        if (variation == 0) {
//            velocity.mul(0.98);
//            updateRotation();
//        } else if (variation == 1) {
//            float num2 = 70f;
//            direction = spriteDirection = velocity.X > 0f ? 1 : -1;
//            if (getDeltaMovement().lengthSqr() > 0.01f) {
//                velocity.mul(0.95);
//            }
//            num2 *= direction;
//            Vector2 vector = base.Center - rotation.ToRotationVector2() * num2;
//            float num3 = Mth.clampedMap(fromValue, 0.3f, 0.5f, 0f, 1f) * Mth.clampedMap(fromValue, 0.45f, 0.5f, 1f, 0f);
//            float num4 = Mth.clampedMap(fromValue, 0.5f, 0.55f, 0f, 1f) * Mth.clampedMap(fromValue, 0.5f, 1f, 1f, 0f);
//            float num5 = num3 * Mth.PI / 20;
//            num5 += num4 * -Mth.PI * 8f / 20;
//            rotation += num5 * -direction;
//            rotation = MathHelper.WrapAngle(rotation);
//            base.Center = vector + rotation.ToRotationVector2() * num2;
//        } else if (variation == 2) {
//            float f = ai[1];
//            float num6 = Mth.clampedMap(fromValue, 0f, 0.4f, 1f, 0f);
//            float num7 = Mth.clampedMap(fromValue, 0.3f, 0.4f, 0f, 1f) * Mth.clampedMap(fromValue, 0.4f, 1f, 1f, 0f);
//            float num8 = num6 * 2f + num7 * 8f + 0.01f;
//            velocity = f.ToRotationVector2() * num8;
//            direction = spriteDirection = velocity.X > 0f ? 1 : -1;
//            rotation = velocity.ToRotation();
//            if (spriteDirection == -1) {
//                rotation += Mth.PI;
//            }
//        } else {
//            float num9 = ai[1];
//            BoneHelm.RotatedBy(velocity, num9, yaw);
//            direction = spriteDirection = velocity.X > 0f ? 1 : -1;
//            rotation = velocity.ToRotation();
//            if (spriteDirection == -1) {
//                rotation += Mth.PI;
//            }
//        }
//        ai[0] += 1f;
    }
}
