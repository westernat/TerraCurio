package org.confluence.terra_curio.common.item.curio.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 牧羊符文
public class RamRune extends BaseCurioItem {
    public static final double FALL_VELOCITY = -1.0D;
    private static final Set<Integer> FALLING = new HashSet<>();

    public RamRune(Builder builder) {
        super(builder);
    }

    public static void startFalling(ServerPlayer player) {
        System.out.println(FALLING);
        if (player.onGround()) return;
        if (!FALLING.add(player.getId())) return;
        Vec3 vec3 = player.getDeltaMovement();
        player.setDeltaMovement(vec3.x, FALL_VELOCITY, vec3.z);
        player.hasImpulse = true;
    }

    public static boolean isFalling(LivingEntity living) {
        if (!(living instanceof Player player)) return false;
        return FALLING.contains(player.getId());
    }

    public static void cancel(LivingEntity living) {
        if (living instanceof Player player) {
            FALLING.remove(player.getId());
            Vec3 motion = living.getDeltaMovement();
            player.setDeltaMovement(motion.x, Math.min(0, motion.y + 1), motion.z);
        }
    }

    public static void cancelOnJump(ServerPlayer player, float motionY) {
        if (motionY > 0.0F) {
            FALLING.remove(player.getId());
        }
    }

    public static void onLanding(ServerPlayer player) {
        if (!FALLING.remove(player.getId())) return;
        player.level().playSound(null, player.getOnPos(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
        ServerLevel level = player.serverLevel();
        BlockPos center = player.getOnPos();
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-2, 0, -2), center.offset(2, 0, 2))) {
            BlockState state = level.getBlockState(pos);
            if (!state.isAir()) {
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 1.0;
                double z = pos.getZ() + 0.5;
                level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), x, y, z, 8, 0.2, 0.2, 0.2, 0.1);
            }
        }
        AABB area = player.getBoundingBox().inflate(4.0, 1.0, 4.0);
        List<Entity> collide = player.level().getEntities(player, player.getBoundingBox(), e -> e instanceof LivingEntity && e != player);
        List<Entity> around = player.level().getEntities(player, area, e -> e instanceof LivingEntity && e != player);
        for (Entity entity : around) {
            if (!collide.contains(entity)) {
                entity.hurt(player.damageSources().playerAttack(player), 10.0F);
            }
        }
        for (Entity entity : collide) {
            entity.hurt(player.damageSources().playerAttack(player), 15.0F);
        }
    }
}
