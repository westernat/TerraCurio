package org.confluence.terra_curio.common.event;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.confluence.lib.api.event.ArmorPenetrationEvent;
import org.confluence.lib.common.LibDamageTypes;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.common.TCCommonConfigs;
import org.confluence.terra_curio.common.advancement.CuriosEquippedTrigger;
import org.confluence.terra_curio.common.attachment.AccessoriesAttachment;
import org.confluence.terra_curio.common.attachment.AccessoriesValueCommand;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.init.TCTags;
import org.confluence.terra_curio.common.item.DivingHelmet;
import org.confluence.terra_curio.common.item.curio.combat.PanicNecklace;
import org.confluence.terra_curio.common.item.curio.combat.RamRune;
import org.confluence.terra_curio.mixin.accessor.ItemEntityAccessor;
import org.confluence.terra_curio.network.s2c.EntityKilledPacketS2C;
import org.confluence.terra_curio.network.s2c.InfoCurioCheckPacketS2C;
import org.confluence.terra_curio.network.s2c.SetItemEntityPickupDelayPacketS2C;
import org.confluence.terra_curio.util.TCUtils;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventPriority;
import org.mesdag.portlib.event.entity.PortEntityInvulnerabilityCheckEvent;
import org.mesdag.portlib.event.entity.living.PortLivingDamageEvent;
import org.mesdag.portlib.event.entity.living.PortLivingIncomingDamageEvent;
import org.mesdag.portlib.event.entity.living.PortMobEffectEvent;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

public final class TCGameEvents {
    public static void init() {
        PortEventHandler.addListener(TCGameEvents::registerCommand);
        PortEventHandler.addListener(TCGameEvents::curios);
        PortEventHandler.addListener(TCGameEvents::entityInvulnerabilityCheck);
        PortEventHandler.addListener(TCGameEvents::livingIncomingDamage);
        PortEventHandler.addListener(TCGameEvents::livingDamage$Pre);
        PortEventHandler.addListener(TCGameEvents::livingFall);
        PortEventHandler.addListener(TCGameEvents::livingDeath);
        PortEventHandler.addListener(TCGameEvents::entityJoinLevel);
        PortEventHandler.addListener(TCGameEvents::playerLogin);
        PortEventHandler.addListener(TCGameEvents::playerLogout);
        PortEventHandler.addListener(TCGameEvents::playerTick$Post);
        PortEventHandler.addListener(TCGameEvents::itemToss);
        PortEventHandler.addListener(TCGameEvents::livingBreathe);
        PortEventHandler.addListener(TCGameEvents::finalizeSpawn);
        PortEventHandler.addListener(PortEventPriority.LOWEST, TCGameEvents::mobEffect$Applicable);
        PortEventHandler.addListener(TCGameEvents::armorPenetration);
    }

    private static void registerCommand(RegisterCommandsEvent event) {
        AccessoriesValueCommand.register(event.getDispatcher());
    }

    private static void curios(CurioChangeEvent event) {
        LivingEntity living = event.getEntity();
        if (!living.level().isClientSide && !ItemStack.isSameItem(event.getFrom(), event.getTo())) {
            AccessoriesAttachment.of(living).flushAbility(living);
            if (living instanceof ServerPlayer serverPlayer) {
                TCUtils.resetClientPacket(serverPlayer);
                CuriosEquippedTrigger.INSTANCE.trigger(serverPlayer, event.getTo());
            }
        }
    }

    private static void entityInvulnerabilityCheck(PortEntityInvulnerabilityCheckEvent event) {
        if (event.isInvulnerable()) return;
        DamageSource damageSource = event.getSource();
        if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) || damageSource.is(DamageTypes.GENERIC_KILL)) {
            return;
        }

        if (TCUtils.isInvulnerableTo(event.getEntity(), damageSource)) {
            event.setInvulnerable(true);
        }
    }

    private static void livingIncomingDamage(PortLivingIncomingDamageEvent event) {
        PortDamageContainer container = event.getContainer();
        float invulnerableTicksMultiplier = TCUtils.getValue(event.getEntity(), TCItems.INVULNERABLE$TICKS$MULTIPLIER);
        container.setPostAttackInvulnerabilityTicks((int) (container.getPostAttackInvulnerabilityTicks() * invulnerableTicksMultiplier));
    }

    private static void livingDamage$Pre(PortLivingDamageEvent.Pre event) {
        float amount = event.getNewDamage();
        if (amount <= 0.0F) return;
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;
        DamageSource damageSource = event.getSource();
        if (!damageSource.is(DamageTypes.MAGIC)) {
            RamRune.cancel(victim);
        }
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
        RandomSource random = victim.level().random;

        TCUtils.applyFireAttack(damageSource, victim);
        if (damageSource.getEntity() != null) {
            TCUtils.applyHoneyComb(victim, random);
        }
        TCUtils.applyStarClock(victim, random);
        PanicNecklace.apply(victim);

        amount = DivingHelmet.apply(victim, damageSource, amount);
        amount = TCUtils.applyFrozenTurtleShell(victim, amount);
        amount = TCUtils.applyLavaHurtReduce(victim, damageSource, amount);
        amount = TCUtils.applyInjuryFree(victim, amount);
        amount = TCUtils.applyBrainOfConfusion(victim, random, damageSource, amount);

        if (TCCommonConfigs.RANDOM_ATTACK_DAMAGE.get()) {
            amount *= Mth.nextFloat(random,
                    TCCommonConfigs.RANDOM_ATTACK_DAMAGE_MIN.get().floatValue(),
                    TCCommonConfigs.RANDOM_ATTACK_DAMAGE_MAX.get().floatValue()
            );
        }
        event.setNewDamage(amount);
    }

    private static void livingFall(LivingFallEvent event) {
        LivingEntity living = event.getEntity();
        if (living instanceof ServerPlayer serverPlayer) {
            if (RamRune.isFalling(serverPlayer)) {
                event.setDamageMultiplier(0.0F);
                RamRune.onLanding(serverPlayer);
            }
        }
    }

    private static void livingDeath(LivingDeathEvent event) {
        DamageSource damageSource = event.getSource();
        if (damageSource.getEntity() instanceof ServerPlayer serverPlayer) {
            EntityType<?> entityType = event.getEntity().getType();
            EntityKilledPacketS2C.sendToClient(serverPlayer, entityType);
        }
    }

    private static void entityJoinLevel(EntityJoinLevelEvent event) {
        if (event.loadedFromDisk() || event.getLevel().isClientSide) {
            if (event.getEntity() instanceof LivingEntity living) {
                AccessoriesAttachment.of(living).flushAbility(living);
            }
            return;
        }
        if (event.getEntity() instanceof AbstractArrow arrow && arrow.getOwner() instanceof LivingEntity living) {
            TCUtils.applyIgniteArrow(living, arrow);
        }
    }

    private static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        AccessoriesAttachment.of(player).flushAbility(player);
        ServerPlayer serverPlayer = (ServerPlayer) player;
        TCUtils.resetClientPacket(serverPlayer);
        InfoCurioCheckPacketS2C.sendToClient(serverPlayer, serverPlayer.getInventory());
    }

    private static void playerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        RamRune.cancel(event.getEntity());
    }

    private static void playerTick$Post(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (!player.isPassenger()) {
            TCUtils.applyFluidWalk(player);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.level().getGameTime() % 200 == 0) {
                InfoCurioCheckPacketS2C.sendToOthers(serverPlayer);
            }
        }
    }

    private static void itemToss(ItemTossEvent event) {
        ItemEntity itemEntity = event.getEntity();
        SetItemEntityPickupDelayPacketS2C.sendToAll(itemEntity.getId(), ((ItemEntityAccessor) itemEntity).getPickupDelay());
    }

    private static void livingBreathe(LivingBreatheEvent event) {
        LivingEntity living = event.getEntity();
        if (event.canBreathe()) return;
        if (living.level().isClientSide) {
            if (living.getClass() == LocalPlayer.class && TCClientPacketHandler.isHasNeptunesShell()) {
                event.setCanBreathe(true);
                event.setRefillAirAmount(4);
            }
        } else {
            if (TCUtils.hasType(living, TCItems.NEPTUNES$SHELL)) {
                event.setCanBreathe(true);
                event.setRefillAirAmount(4);
            } else if (living.getAirSupply() > 0 && living.level().getGameTime() % 8 != 0) {
                if (living.getItemBySlot(EquipmentSlot.HEAD).is(TCTags.Items.DIVING) || TCUtils.hasType(living, TCItems.DIVING)) {
                    event.setConsumeAirAmount(0);
                }
            }
        }
    }

    private static void finalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.isSpawnCancelled()) return;
        if (event.getEntity() instanceof Drowned drowned && drowned.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && drowned.getRandom().nextFloat() < 0.05F) {
            drowned.setItemSlot(EquipmentSlot.HEAD, TCItems.DIVING_HELMET.get().getDefaultInstance());
            drowned.setDropChance(EquipmentSlot.HEAD, 1.0F);
        }
    }

    private static void mobEffect$Applicable(PortMobEffectEvent.Applicable event) {
        if (event.getPortResult() != PortMobEffectEvent.Applicable.PortResult.DO_NOT_APPLY) {
            if (TCUtils.getValue(event.getEntity(), TCItems.EFFECT$IMMUNITIES).contains(event.getEffectInstance().getEffect())) {
                event.setPortResult(PortMobEffectEvent.Applicable.PortResult.DO_NOT_APPLY);
            }
        }
    }

    private static void armorPenetration(ArmorPenetrationEvent event) {
        if (event.getDamageSource().is(LibDamageTypes.STAR_CLOAK)) {
            event.setPenetration(event.getPenetration() - 3);
        }
    }
}
