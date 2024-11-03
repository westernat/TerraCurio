package org.confluence.terra_curio.util;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.apache.commons.compress.utils.Lists;
import org.confluence.terra_curio.common.attachment.AccessoriesAttachment;
import org.confluence.terra_curio.common.component.EffectImmunities;
import org.confluence.terra_curio.common.entity.projectile.BeeProjectile;
import org.confluence.terra_curio.common.entity.projectile.StarCloakEntity;
import org.confluence.terra_curio.common.init.*;
import org.jetbrains.annotations.ApiStatus;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;

public final class TCUtils {
    @ApiStatus.Internal
    public static void forConfluence$Inject() {}

    @ApiStatus.Internal
    public static <T> T forConfluence$ModifyExpression(T value) {
        return value;
    }

    public static float nextFloat(RandomSource randomSource, float origin, float bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound - origin is non positive");
        } else {
            return origin + randomSource.nextFloat() * (bound - origin);
        }
    }

    public static boolean isServerNotFake(Player player) {
        return player instanceof ServerPlayer && !(player instanceof FakePlayer);
    }

    public static boolean applyEffectImmunity(LivingEntity living, Holder<MobEffect> mobEffect) {
        ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosInventory(living).orElse(null);
        return curiosItemHandler != null && curiosItemHandler.getCurios().values().stream()
                .map(ICurioStacksHandler::getStacks).flatMap(iDynamicStackHandler -> {
                    int slots = iDynamicStackHandler.getSlots();
                    List<ItemStack> stacks = Lists.newArrayList();
                    for (int i = 0; i < slots; i++) {
                        stacks.add(iDynamicStackHandler.getStackInSlot(i));
                    }
                    return stacks.stream();
                }).anyMatch(stack -> {
                    EffectImmunities component = stack.get(TCDataComponentTypes.EFFECT_IMMUNITIES);
                    return component != null && component.contains(mobEffect);
                });
    }

    public static void applyFireAttack(Player player, Entity entity) {
        if (player.getData(TCAttachments.ACCESSORIES).isFireAttack()) {
            float f = player.getRandom().nextFloat();
            int time;
            if (f < 0.25F) {
                time = 120;
            } else if (f < 0.375F) {
                time = 80;
            } else {
                time = 40;
            }
            entity.igniteForTicks(time);
        }
    }

    public static boolean isInvulnerableTo(Entity self, DamageSource damageSource) {
        if (!(self instanceof LivingEntity living)) return false;
        Entity entity = damageSource.getEntity();
        AccessoriesAttachment attachment = self.getData(TCAttachments.ACCESSORIES);
        if (entity != null) {
            if (attachment.getIgnores().contains(entity.getType())) {
                return true;
            }
        }
        return TCAttributes.applyDodge(living, living.getRandom());
    }

    public static float applyInjuryFree(LivingEntity living, float amount) {
        float injuryFree = living.getData(TCAttachments.ACCESSORIES).getInjuryFree();
        return amount * (1.0F - injuryFree);
    }

    public static void applyStarClock(LivingEntity living, RandomSource random) {
        boolean starClock = living.getData(TCAttachments.ACCESSORIES).isStarClock();
        if (starClock) {
            Level level = living.level();
            List<Entity> list = level.getEntities(living, new AABB(living.getOnPos()).inflate(4.0, 3.0, 4.0), entity -> entity instanceof Enemy);
            for (int i = 0; i < 3; i++) {
                Entity target;
                if (list.isEmpty()) {
                    target = living;
                } else {
                    target = list.get(random.nextInt(list.size()));
                }
                StarCloakEntity entity = new StarCloakEntity(level, living, target, forConfluence$ModifyExpression(false)); // todo mixin here
                level.addFreshEntity(entity);
            }
        }
    }

    public static void applyHoneyComb(LivingEntity living, RandomSource random) {
        AccessoriesAttachment attachment = living.getData(TCAttachments.ACCESSORIES);
        if (attachment.isHoneyComb()) {
            boolean hasHivePack = attachment.isHivePack();
            int summon = random.nextInt(1, hasHivePack ? 5 : 4);
            for (int i = 0; i < summon; i++) {
                BeeProjectile projectile = new BeeProjectile(living.level(), living, hasHivePack && random.nextBoolean());
                projectile.setPos(living.position().add(random.nextInt(3) - 1.0, 2.0, random.nextInt(3) - 1.0));
                living.level().addFreshEntity(projectile);
            }
            living.addEffect(new MobEffectInstance(TCEffects.HONEY, 100));
        }
    }

    public static void applyIgniteArrow(LivingEntity living, AbstractArrow arrow) {
        if (living.getData(TCAttachments.ACCESSORIES).isIgniteArrow()) {
            arrow.igniteForSeconds(100.0F);
        }
    }

    public static float applyFrozenTurtleShell(LivingEntity living, float amount) {
        if (living.getHealth() / living.getMaxHealth() < 0.5F && living.getData(TCAttachments.ACCESSORIES).isFrozenTurtleShell()) {
            return amount * 0.75F;
        }
        return amount;
    }

    public static float applyBrainOfConfusion(LivingEntity living, RandomSource randomSource, DamageSource damageSource, float amount) {
        if (damageSource.is(TCTags.HARMFUL_EFFECT)) return amount;
        if (!living.getData(TCAttachments.ACCESSORIES).isBrainOfConfusion()) return amount;
        if (randomSource.nextFloat() < 0.6F + amount * 0.02F) {
            float rangeMin, rangeMax;
            if (amount <= 120) rangeMin = amount * 0.5F + 200;
            else if (amount <= 266.6F) rangeMin = amount * 0.375F + 275;
            else if (amount <= 440) rangeMin = amount * 0.1875F + 487.5F;
            else rangeMin = amount * 0.046875F + 796.875F;
            if (amount <= 20) rangeMax = amount * 2 + 300;
            else if (amount <= 46.6F) rangeMax = amount * 1.5F + 350;
            else if (amount <= 100) rangeMax = amount * 0.75F + 525;
            else rangeMax = amount * 0.1875F + 806.25F;
            float range = TCUtils.nextFloat(randomSource, rangeMin, rangeMax) / 24;
            int duration = randomSource.nextInt((int) (90 + amount / 3), (int) (300 + amount / 2));
            living.level().getEntities(living, new AABB(living.getOnPos()).inflate(range), entity -> entity instanceof Enemy).forEach(enemy -> {
                if (enemy instanceof LivingEntity living1) {
                    living1.addEffect(new MobEffectInstance(TCEffects.CONFUSED, duration));
                }
            });
        }
        if (randomSource.nextFloat() < 0.1667F && !living.hasEffect(TCEffects.CEREBRAL_MINDTRICK)) {
            living.addEffect(new MobEffectInstance(TCEffects.CEREBRAL_MINDTRICK, 80));
            return 0.0F;
        }
        return amount;
    }

    public static boolean magicQuiver$shouldConsume(LivingEntity living) {
        return !living.getData(TCAttachments.ACCESSORIES).isMagicQuiver() || living.getRandom().nextFloat() >= 0.2F;
    }

    public static void resetClientPacket(ServerPlayer serverPlayer) {
        // todo
    }
}
