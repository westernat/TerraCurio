package org.confluence.terra_curio.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.UnitValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.attachment.AccessoriesAttachment;
import org.confluence.terra_curio.common.component.NbtComponent;
import org.confluence.terra_curio.common.entity.projectile.BeeProjectile;
import org.confluence.terra_curio.common.entity.projectile.StarCloakEntity;
import org.confluence.terra_curio.common.init.*;
import org.confluence.terra_curio.mixed.IEntity;
import org.confluence.terra_curio.network.s2c.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static org.confluence.terra_curio.api.primitive.ValueType.*;

public final class TCUtils {
    public static final AttributeModifier ICE_SPEED_MODIFIER = new AttributeModifier(TerraCurio.asResource("ice_speed"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    @ApiStatus.Internal
    public static void forConfluence$Inject() {}

    @ApiStatus.Internal
    public static <T> T forConfluence$ModifyExpression(T value) {
        return value;
    }

    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public static <T, V extends PrimitiveValue<T>> V tryCast(PrimitiveValue<?> primitiveValue) {
        return (V) primitiveValue;
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

    public static void applyFireAttack(Player player, Entity entity) {
        if (hasAccessoriesType(player, FIRE$ATTACK)) {
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
        AccessoriesAttachment attachment = self.getData(TCAttachments.ACCESSORIES);
        Entity attacker = damageSource.getEntity();
        if (attacker != null && attachment.getValue(MOB$IGNORE).contains(attacker.getType())) {
            return true;
        }
        if (attachment.contains(SHIELD$OF$CTHULHU) && ((IEntity) living).terra_curio$isOnCthulhuSprinting()) {
            return true;
        }
        if (attachment.contains(FIRE$IMMUNE) && damageSource.is(DamageTypes.IN_FIRE) ||
                damageSource.is(DamageTypes.ON_FIRE) ||
                damageSource.is(DamageTypes.HOT_FLOOR) ||
                damageSource.is(DamageTypes.UNATTRIBUTED_FIREBALL) ||
                damageSource.is(DamageTypes.FIREBALL)
        ) return true;
        return TCAttributes.applyDodge(living, living.getRandom());
    }

    public static float applyInjuryFree(LivingEntity living, float amount) {
        float injuryFree = TCUtils.getAccessoriesValue(living, INJURY$FREE);
        return amount * (1.0F - injuryFree);
    }

    public static void applyStarClock(LivingEntity living, RandomSource random) {
        AccessoriesAttachment attachment = living.getData(TCAttachments.ACCESSORIES);
        boolean starClock = attachment.contains(STAR$CLOCK);
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
                StarCloakEntity entity = new StarCloakEntity(level, living, target, attachment.getValue(STAR$CLOCK));
                level.addFreshEntity(entity);
            }
        }
    }

    public static void applyHoneyComb(LivingEntity living, RandomSource random) {
        AccessoriesAttachment attachment = living.getData(TCAttachments.ACCESSORIES);
        if (attachment.contains(HONEY$COMB)) {
            boolean hasHivePack = attachment.contains(HIVE$PACK);
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
        if (hasAccessoriesType(living, IGNITE$ARROW)) {
            arrow.igniteForTicks(2000);
        }
    }

    public static float applyFrozenTurtleShell(LivingEntity living, float amount) {
        if (living.getHealth() / living.getMaxHealth() < 0.5F && hasAccessoriesType(living, FROZEN$TURTLE$SHELL)) {
            return amount * 0.75F;
        }
        return amount;
    }

    public static float applyBrainOfConfusion(LivingEntity living, RandomSource randomSource, DamageSource damageSource, float amount) {
        if (damageSource.is(TCTags.HARMFUL_EFFECT)) return amount;
        if (!living.getData(TCAttachments.ACCESSORIES).contains(BRAIN$OF$CONFUSION)) return amount;
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
            float range = nextFloat(randomSource, rangeMin, rangeMax) / 24;
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
        return !living.getData(TCAttachments.ACCESSORIES).contains(MAGIC$QUIVER) || living.getRandom().nextFloat() >= 0.2F;
    }

    public static void resetClientPacket(ServerPlayer serverPlayer) {
        InfoCurioCheckPacketS2C.sendToPlayer(serverPlayer, serverPlayer.getInventory());
        CurioExistsPacketS2C.sendToClient(serverPlayer);
        PlayerClimbPacketS2C.sendToClient(serverPlayer);
        PlayerJumpPacketS2C.sendToClient(serverPlayer);
        PlayerFlyPacketS2C.sendToClient(serverPlayer);
        RightClickSubtractorPacketS2C.sendToClient(serverPlayer);
    }

    public static @NotNull CompoundTag getItemStackNbt(ItemStack itemStack) {
        NbtComponent nbtComponent = itemStack.get(TCDataComponentTypes.NBT);
        if (nbtComponent == null) {
            CompoundTag nbt = new CompoundTag();
            itemStack.set(TCDataComponentTypes.NBT, new NbtComponent(nbt));
            return nbt;
        }
        return nbtComponent.nbt().copy();
    }

    public static void updateItemStackNbt(ItemStack itemStack, Consumer<CompoundTag> consumer) {
        NbtComponent nbtComponent = itemStack.get(TCDataComponentTypes.NBT);
        CompoundTag nbt;
        if (nbtComponent == null) {
            nbt = new CompoundTag();
        } else {
            nbt = nbtComponent.nbt().copy();
        }
        consumer.accept(nbt);
        itemStack.set(TCDataComponentTypes.NBT, new NbtComponent(nbt));
    }

    public static float applyLavaHurtReduce(LivingEntity living, DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypes.LAVA)) {
            float value = getAccessoriesValue(living, LAVA$HURT$REDUCE);
            living.igniteForTicks(140);
            return amount * (1.0F - value);
        }
        return amount;
    }

    public static void onChangedBlock(LivingEntity living, ServerLevel level) {
        if (living.onGround()) {
            AccessoriesAttachment attachment = living.getData(TCAttachments.ACCESSORIES);
            BlockPos onPos = living.getOnPos();
            if (attachment.contains(FLOWER$BOOTS) && level.getBlockState(onPos).is(TCTags.FLOWER_BOOTS_AVAILABLE)) {
                BlockPos abovePos = onPos.above();
                RandomSource random = level.random;
                for (BlockPos aroundPos : BlockPos.betweenClosed(abovePos.offset(-1, 0, -1), abovePos.offset(1, 0, 1))) {
                    if (!level.getBlockState(aroundPos.below()).is(TCTags.FLOWER_BOOTS_AVAILABLE)) continue;
                    if (level.getBlockState(aroundPos).isCollisionShapeFullBlock(level, aroundPos)) continue;
                    if (random.nextFloat() < 0.3F && level.getBlockState(aroundPos).isAir()) {
                        List<ConfiguredFeature<?, ?>> list = level.getBiome(aroundPos).value().getGenerationSettings().getFlowerFeatures();
                        if (list.isEmpty()) continue;
                        ((RandomPatchConfiguration) list.getFirst().config()).feature().value().place(level, level.getChunkSource().getGenerator(), random, aroundPos);
                    }
                }
            }
            if (attachment.contains(ICE$SPEED)) {
                AttributeInstance instance = living.getAttribute(Attributes.MOVEMENT_SPEED);
                assert instance != null;
                if (level.getBlockState(onPos).is(BlockTags.ICE)) {
                    if (!instance.hasModifier(ICE_SPEED_MODIFIER.id())) {
                        instance.addTransientModifier(ICE_SPEED_MODIFIER);
                    }
                } else {
                    instance.removeModifier(ICE_SPEED_MODIFIER);
                }
            }
        }
    }

    public static Vec3 getWalkVec(LivingEntity living, Vec3 par1) {
        if (living instanceof Player && living.getEyeInFluidType() == NeoForgeMod.EMPTY_TYPE.value()) {
            if (living.canStandOnFluid(living.level().getFluidState(living.blockPosition()))) {
                AttributeInstance instance = living.getAttribute(Attributes.MOVEMENT_SPEED);
                if (instance == null) return par1;
                double horizon = Math.min(0.91 * living.getSpeed() / instance.getBaseValue(), 0.93);
                return living.getDeltaMovement().multiply(horizon, 1.0, horizon);
            }
        }
        return par1;
    }

    public static float applyArmorPass(DamageSource damageSource, float armorValue) {
        if (!TCAttributes.hasCustomAttribute(TCAttributes.ARMOR_PASS) && damageSource.getEntity() instanceof LivingEntity attacker) {
            AttributeInstance attributeInstance = attacker.getAttribute(TCAttributes.ARMOR_PASS);
            if (attributeInstance != null) armorValue -= (float) attributeInstance.getValue();
            if (damageSource.is(TCDamageTypes.STAR_CLOAK)) armorValue -= 3.0F;
            return Math.max(armorValue, 0.0F);
        }
        return armorValue;
    }

    public static boolean applyTotemAbility(LivingEntity living) {
        int cooldown = getAccessoriesValue(living, ValueType.TOTEM$WITH$COOLDOWN);
        CompoundTag data = living.getPersistentData();
        if (cooldown > 0) {
            if (data.getInt("terra_curio:totem_cooldown") <= 0) {
                living.setHealth(1.0F);
                living.removeEffectsCuredBy(net.neoforged.neoforge.common.EffectCures.PROTECTED_BY_TOTEM);
                living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                living.level().broadcastEntityEvent(living, EntityEvent.TALISMAN_ACTIVATE);
                data.putInt("terra_curio:totem_cooldown", cooldown);
                return true;
            }
        } else {
            data.putInt("terra_curio:totem_cooldown", -1);
        }
        return false;
    }

    public static boolean hasAccessoriesType(LivingEntity living, ValueType<Unit, UnitValue> type) {
        return living.getData(TCAttachments.ACCESSORIES).contains(type);
    }

    public static <T, V extends PrimitiveValue<T>> T getAccessoriesValue(LivingEntity living, ValueType<T, V> type) {
        return living.getData(TCAttachments.ACCESSORIES).getValue(type);
    }
}
