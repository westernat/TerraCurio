package org.confluence.terra_curio.common.item.curio.expert;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.joml.Vector3d;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class BoneHelm extends BaseCurioItem {
    private static final double rangeSqr = Mth.square(500.0 / 8 * 2 / 3);

    public BoneHelm(Builder builder) {
        super(builder);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        if (!(living.level() instanceof ServerLevel level)) return;
        CompoundTag tag = LibUtils.getItemStackNbtNoCopy(stack);
        if (level.getGameTime() - tag.getLong("lastCheckTime") < tag.getInt("insanityShadowCooldown")) return;
        tag.putInt("insanityShadowCooldown", living.getRandom().nextInt(7, 34));
        tag.putLong("lastCheckTime", level.getGameTime());
        List<? extends LivingEntity> _hallucinationCandidates = level.getEntities(EntityTypeTest.forClass(LivingEntity.class), entity ->
                entity instanceof Enemy &&
                        entity.canBeHitByProjectile() &&
                        entity.distanceToSqr(living) < rangeSqr
        );
        if (!_hallucinationCandidates.isEmpty()) {
            Vector3d spawnposition = new Vector3d();
            Vector3d spawnvelocity = new Vector3d();
            float[] ai = new float[2];
            RandomizeInsanityShadowFor(Util.getRandom(_hallucinationCandidates, living.getRandom()), false, spawnposition, spawnvelocity, ai);

        }
    }

    public static void RandomizeInsanityShadowFor(Entity targetEntity, boolean isHostile, Vector3d spawnposition, Vector3d spawnvelocity, float[] ai) {
        RandomSource random = targetEntity.getRandom();
        Vec3 velocity = targetEntity.getDeltaMovement();
        Vector3d center = new Vector3d(targetEntity.getX(), targetEntity.getEyeY(), targetEntity.getZ());
        float facing = random.nextInt(2) * 2 - 1;
        int variant = random.nextInt(4);
        float dist2Target = (isHostile ? 200 : 100) / 8.0F;
        float timeClose2Target = (isHostile ? 30 : 20) / 3.0F;
        float predict = (isHostile ? 30 : 0) / 8.0F;
        float roll = NextFloatDirection(random) * Mth.PI * 0.125F;
        float yaw = random.nextFloat() * Mth.TWO_PI;
        Vector3d temp = new Vector3d();

        if (isHostile && velocity.x * facing > 0) {
            facing = -facing;
        }

        if (variant == 0) {
            if (isHostile) timeClose2Target += 10 / 3.0F;
            RotatedBy(temp.set(facing * -dist2Target, 0, 0), roll, yaw);
            spawnposition.set(velocity.x, velocity.y, velocity.z).mul(predict).add(center).add(temp);
            spawnvelocity.set(RotatedBy(temp.set(facing * dist2Target / timeClose2Target, 0, 0), roll, yaw));
        } else if (variant == 1) {
            temp.set(ToRotationVector3(roll, yaw));
            temp.mul(isHostile ? dist2Target : dist2Target * 0.5, spawnposition);
            center.sub(spawnposition, spawnposition);
            ai[0] = 180;
            ai[1] = roll - Mth.HALF_PI;
            spawnvelocity.set(temp.mul((isHostile ? 4 : 2) / 8.0));
        } else if (variant == 2) {
            temp.set(ToRotationVector3(roll, yaw));
            temp.mul(dist2Target, spawnposition);
            center.sub(spawnposition, spawnposition);
            ai[0] = 300;
            ai[1] = roll;
            spawnvelocity.set(temp.mul((isHostile ? 4 : 2) / 8.0));
        } else {
            float timeRotate = (isHostile ? 60 : 30) / 3.0F;
            float rollStep = Mth.HALF_PI / timeRotate * NextFloatDirection(random);
            temp.set(velocity.x, velocity.y, velocity.z).mul(timeRotate);
            center.add(temp, spawnposition);
            temp.set(ToRotationVector3(roll * (isHostile ? 8 : 3), yaw));
            for (int i = 0; i < timeRotate; i++) {
                spawnposition.sub(temp);
                RotatedBy(temp, -rollStep, yaw);
            }
            spawnvelocity.set(temp);
            ai[0] = 390;
            ai[1] = rollStep;
        }
    }

    public static float NextFloatDirection(RandomSource random) {
        return random.nextFloat() * 2 - 1;
    }

    public static Vector3d ToRotationVector3(float roll, float yaw) {
        return new Vector3d(Mth.cos(roll), Mth.sin(roll), 0).rotateY(yaw);
    }

    public static Vector3d RotatedBy(Vector3d v, float roll, float yaw) {
        return v.rotateZ(roll).rotateY(yaw);
    }
}
