package org.confluence.terra_curio.common.entity.projectile;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.common.init.TCEntities;
import org.jetbrains.annotations.Nullable;

@javax.annotation.ParametersAreNonnullByDefault
@net.minecraft.MethodsReturnNonnullByDefault
public class BeeProjectile extends Projectile {
    private static final EntityDataAccessor<Boolean> DATA_IS_GIANT = SynchedEntityData.defineId(BeeProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDimensions SMALL = TCEntities.BEE_PROJECTILE.get().getDimensions().scale(0.5f);
    private static final EntityDimensions GIANT = TCEntities.BEE_PROJECTILE.get().getDimensions();

    private int blockHitCount;
    private int lifeTime;
    private Entity target;

    public BeeProjectile(EntityType<BeeProjectile> entityType, Level level) {
        super(entityType, level);
        this.blockHitCount = 0;
        this.lifeTime = 0;
    }

    public BeeProjectile(Level level, @Nullable LivingEntity owner, boolean isGiant) {
        this(TCEntities.BEE_PROJECTILE.get(), level);
        setOwner(owner);
        this.blockHitCount = 0;
        this.lifeTime = 0;
        entityData.set(DATA_IS_GIANT, isGiant);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_IS_GIANT, false);
    }

    public boolean isGiant() {
        return entityData.get(DATA_IS_GIANT);
    }

    @Override
    public void tick() {
        if (target == null) {
            double d0 = -1.0;
            Entity enemy = null;
            for (Entity entity : level().getEntities(this, new AABB(blockPosition()).inflate(8))) {
                if (entity instanceof Enemy) {
                    double d1 = entity.distanceToSqr(getX(), getY(), getZ());
                    if (d0 == -1.0 || d1 < d0) {
                        d0 = d1;
                        enemy = entity;
                    }
                }
            }
            this.target = enemy;
        }
        if (target != null) {
            if (target.isSpectator() || (target instanceof LivingEntity living && living.isDeadOrDying())) this.target = null;
            if (target != null) {
                Vec3 vec3 = target.getEyePosition().subtract(position()).normalize();
                addDeltaMovement(vec3.scale(0.95).scale(isGiant() ? 0.15 : 0.05));
            }
        }
        if (lifeTime % 4 == 0) {
            AABB boundingBox = getBoundingBox().inflate(1.0);
            HitResult hitresult = ProjectileUtil.getEntityHitResult(level(), this, boundingBox.getMinPosition(), boundingBox.getMaxPosition(), boundingBox, this::canHitEntity);
            if (hitresult instanceof EntityHitResult entityHitResult) {
                onHitEntity(entityHitResult);
            }
        }

        checkInsideBlocks();
        updateRotation();
        Vec3 vec3 = getDeltaMovement();
        move(MoverType.SELF, vec3);
        Vec3 motion = getDeltaMovement();
        if (motion.x != vec3.x || motion.y != vec3.y || motion.z != vec3.z) {
            if (motion.x != vec3.x) motion = new Vec3(-vec3.x, vec3.y, vec3.z);
            if (motion.y != vec3.y) motion = new Vec3(vec3.x, -vec3.y, vec3.z);
            if (motion.z != vec3.z) motion = new Vec3(vec3.x, vec3.y, -vec3.z);
            setDeltaMovement(motion);
            blockHitCount++;
        }
        if (getInBlockState().liquid()) discard();
        else if (blockHitCount > (isGiant() ? 2 : 1) || lifeTime++ > (isGiant() ? 220 : 200)) discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (getOwner() != null) {
            float damage = 5.0F + (isGiant() ? random.nextInt(1, 4) : (random.nextBoolean() ? 1 : 0));
            entity.hurt(damageSources().indirectMagic(getOwner(), this), damage);
            if (isGiant()) {
                Vec3 motion = entity.position().subtract(position()).normalize().scale(0.5);
                entity.push(motion.x, motion.y, motion.z);
            }
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return isGiant() ? GIANT : SMALL;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_IS_GIANT.equals(key)) {
            refreshDimensions();
            setBoundingBox(makeBoundingBox());
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return target.canBeHitByProjectile() && target != getOwner();
    }

    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        float cos = Mth.cos(x * Mth.DEG_TO_RAD);
        float value = y * Mth.DEG_TO_RAD;
        float f = -Mth.sin(value) * cos;
        float f1 = -Mth.sin((x + z) * Mth.DEG_TO_RAD);
        float f2 = Mth.cos(value) * cos;
        shoot(f, f1, f2, velocity, inaccuracy);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}
