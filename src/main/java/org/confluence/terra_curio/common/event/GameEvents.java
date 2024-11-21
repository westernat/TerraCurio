package org.confluence.terra_curio.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.event.AfterAccessoryAbilitiesFlushedEvent;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.confluence.terra_curio.common.attachment.AccessoriesValueCommand;
import org.confluence.terra_curio.common.init.*;
import org.confluence.terra_curio.common.item.DivingHelmet;
import org.confluence.terra_curio.common.item.curio.combat.PaladinsShield;
import org.confluence.terra_curio.common.item.curio.combat.PanicNecklace;
import org.confluence.terra_curio.mixin.accessor.ItemEntityAccessor;
import org.confluence.terra_curio.network.s2c.AttackDamagePacketS2C;
import org.confluence.terra_curio.network.s2c.EntityKilledPacketS2C;
import org.confluence.terra_curio.network.s2c.InfoCurioCheckPacketS2C;
import org.confluence.terra_curio.network.s2c.SetItemEntityPickupDelayPacketS2C;
import org.confluence.terra_curio.util.TCUtils;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = TerraCurio.MODID)
public final class GameEvents {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        AccessoriesValueCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void curios(CurioChangeEvent event) {
        LivingEntity living = event.getEntity();
        if (!living.level().isClientSide) {
            living.getData(TCAttachments.ACCESSORIES).flushAbility(living);
            NeoForge.EVENT_BUS.post(new AfterAccessoryAbilitiesFlushedEvent(living));
        }
        if (living instanceof ServerPlayer serverPlayer) {
            TCUtils.resetClientPacket(serverPlayer);
            TCTriggers.CURIOS_EQUIPPED.get().trigger(serverPlayer, event.getTo());
        }
    }

    @SubscribeEvent
    public static void entityInvulnerabilityCheck(EntityInvulnerabilityCheckEvent event) {
        if (!event.isInvulnerable() && TCUtils.isInvulnerableTo(event.getEntity(), event.getSource())) {
            event.setInvulnerable(true);
        }
    }

    @SubscribeEvent
    public static void livingIncomingDamage(LivingIncomingDamageEvent event) {
        DamageContainer container = event.getContainer();
        float invulnerableTicksMultiplier = TCUtils.getAccessoriesValue(event.getEntity(), ValueType.INVULNERABLE$TICKS$MULTIPLIER);
        container.setPostAttackInvulnerabilityTicks((int) (container.getPostAttackInvulnerabilityTicks() * invulnerableTicksMultiplier));
    }

    @SubscribeEvent
    public static void livingDamage$Pre(LivingDamageEvent.Pre event) {
        LivingEntity living = event.getEntity();
        if (living.level().isClientSide) return;
        DamageSource damageSource = event.getSource();
        if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) || damageSource.is(DamageTypes.GENERIC_KILL)) return;
        RandomSource random = living.level().random;
        float amount = event.getNewDamage();

        TCUtils.applyHoneyComb(living, random);
        TCUtils.applyStarClock(living, random);
        PanicNecklace.apply(living);

        amount = DivingHelmet.apply(living, damageSource, amount);
        amount = TCAttributes.applyMagicDamage(damageSource, amount);
        amount = TCAttributes.applyRangedDamage(living, damageSource, amount);
        amount = PaladinsShield.apply(living, damageSource, amount);
        amount = TCUtils.applyFrozenTurtleShell(living, amount);
        amount = TCUtils.applyLavaHurtReduce(living, damageSource, amount);
        amount = TCUtils.applyInjuryFree(living, amount);
        amount = TCUtils.applyBrainOfConfusion(living, random, damageSource, amount);

        if (TCCommonConfigs.RANDOM_ATTACK_DAMAGE.get()) {
            amount *= TCUtils.nextFloat(random,
                    TCCommonConfigs.RANDOM_ATTACK_DAMAGE_MIN.get().floatValue(),
                    TCCommonConfigs.RANDOM_ATTACK_DAMAGE_MAX.get().floatValue()
            );
        }
        AttackDamagePacketS2C.sendToClient(amount, damageSource.getEntity());
        event.setNewDamage(amount);
    }

    @SubscribeEvent
    public static void livingDeath(LivingDeathEvent event) {
        LivingEntity living = event.getEntity();
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            EntityType<?> entityType = living.getType();
            EntityKilledPacketS2C.sendToClient(serverPlayer, entityType);
        }
    }

    @SubscribeEvent
    public static void livingChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity self = event.getEntity();
        if (!(self instanceof Enemy)) return;
        if (event.getNewAboutToBeSetTarget() instanceof Player playerO) { // 当新目标为玩家时
            double range = self.getAttributeValue(Attributes.FOLLOW_RANGE);
            double rangeSqr = range * range;
            self.level().players().stream()
                    .filter(player -> player.distanceToSqr(self) < rangeSqr && self.canAttack(player))
                    .max((playerA, playerB) -> {
                        AttributeInstance instanceA = playerA.getAttribute(TCAttributes.AGGRO);
                        AttributeInstance instanceB = playerB.getAttribute(TCAttributes.AGGRO);
                        if (instanceA != null && instanceB != null) {
                            return (int) (instanceA.getValue() - instanceB.getValue());
                        }
                        return 0;
                    }).ifPresent(player -> {
                        if (player == playerO) return;
                        AttributeInstance instanceO = playerO.getAttribute(TCAttributes.AGGRO);
                        AttributeInstance instance = player.getAttribute(TCAttributes.AGGRO);
                        if (instanceO != null && instance != null && instanceO.getValue() < instance.getValue()) {
                            event.setNewAboutToBeSetTarget(player); // 只有当新目标的仇恨值大于旧目标时，才设置新目标
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        if (event.loadedFromDisk() || event.getLevel().isClientSide) {
            if (event.getEntity() instanceof LivingEntity living) {
                living.getData(TCAttachments.ACCESSORIES).flushAbility(living);
            }
            return;
        }
        if (event.getEntity() instanceof AbstractArrow arrow && arrow.getOwner() instanceof LivingEntity living) {
            TCAttributes.applyToArrow(living, arrow);
            TCUtils.applyIgniteArrow(living, arrow);
        }
    }

    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        player.getData(TCAttachments.ACCESSORIES).flushAbility(player);
        ServerPlayer serverPlayer = (ServerPlayer) player;
        TCUtils.resetClientPacket(serverPlayer);
        InfoCurioCheckPacketS2C.sendToClient(serverPlayer, serverPlayer.getInventory());
    }

    @SubscribeEvent
    public static void playerTick$Pre(PlayerTickEvent.Pre event) {
        if (event.getEntity().isLocalPlayer()) {
            GravitationHandler.unCrouching(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void playerTick$Post(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        TCAttributes.applyPickupRange(player);
        if (player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.level().getGameTime() % 200 == 0) {
                // 每十秒向周围玩家共享一次信息配饰
                InfoCurioCheckPacketS2C.sendToOthers(serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void effectApplicable(MobEffectEvent.Applicable event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (effectInstance != null && TCUtils.getAccessoriesValue(event.getEntity(), ValueType.EFFECT$IMMUNITIES).contains(effectInstance.getEffect())) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    @SubscribeEvent
    public static void attackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            TCUtils.applyFireAttack(player, event.getTarget());
        }
    }

    @SubscribeEvent
    public static void criticalHit(CriticalHitEvent event) {
        if (TCAttributes.hasCustomAttribute(TCAttributes.CRIT_CHANCE)) return;
        Player player = event.getEntity();
        if (!event.isVanillaCritical()) {
            double chance = player.getAttributeValue(TCAttributes.CRIT_CHANCE);
            if (player.level().random.nextFloat() < chance) {
                event.setDamageMultiplier(1.5F);
                event.setCriticalHit(true);
            }
        }
    }

    @SubscribeEvent
    public static void itemToss(ItemTossEvent event) {
        ItemEntity itemEntity = event.getEntity();
        SetItemEntityPickupDelayPacketS2C.sendToAll(itemEntity.getId(), ((ItemEntityAccessor) itemEntity).getPickupDelay());
    }

    @SubscribeEvent
    public static void livingBreathe(LivingBreatheEvent event) {
        LivingEntity living = event.getEntity();
        if (!event.canBreathe() && living.getAirSupply() > 0 && living.level().getGameTime() % 8 != 0) { //延长至120秒
            if (living.getItemBySlot(EquipmentSlot.HEAD).is(TCTags.DIVING) || TCUtils.hasAccessoriesType(living, ValueType.DIVING)) {
                event.setConsumeAirAmount(0);
            }
        }
    }
}
