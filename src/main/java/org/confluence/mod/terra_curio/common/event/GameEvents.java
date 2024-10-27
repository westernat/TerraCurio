package org.confluence.mod.terra_curio.common.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.client.handler.GravitationHandler;
import org.confluence.mod.terra_curio.common.CommonConfigs;
import org.confluence.mod.terra_curio.common.advancement.ModTriggers;
import org.confluence.mod.terra_curio.common.data.pack.CurioItemManager;
import org.confluence.mod.terra_curio.common.init.ModAttributes;
import org.confluence.mod.terra_curio.network.s2c.AttackDamagePacketS2C;
import org.confluence.mod.terra_curio.network.s2c.EntityKilledPacketS2C;
import org.confluence.mod.terra_curio.util.ModUtils;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = TerraCurio.MODID)
public final class GameEvents {
    @SubscribeEvent
    public static void curios(CurioChangeEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ModTriggers.CURIOS_EQUIPPED.get().trigger(serverPlayer, event.getTo());
        }
    }

    @SubscribeEvent
    public static void livingDamage$Pre(LivingDamageEvent.Pre event) {
        LivingEntity living = event.getEntity();
        if (living.level().isClientSide) return;
        DamageSource damageSource = event.getSource();
        if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) || damageSource.is(DamageTypes.GENERIC_KILL)) return;
        RandomSource random = living.level().random;
        if (ModAttributes.applyDodge(living, random)) {
            event.setNewDamage(0.0F);
            return;
        }

        float amount = event.getNewDamage();

//        IHoneycomb.apply(living, random);
//        IStarCloak.apply(living, random);
//        PanicNecklace.apply(living);

        amount = ModAttributes.applyMagicDamage(damageSource, amount);
        amount = ModAttributes.applyRangedDamage(living, damageSource, amount);
//        amount = PaladinsShield.apply(living, damageSource, amount);
//        amount = FrozenTurtleShell.apply(living, amount);
//        amount = ILavaHurtReduce.apply(living, damageSource, amount);
//        amount = IFallResistance.apply(living, damageSource, amount);
//        amount = WormScarf.apply(living, amount);
//        amount = BrainOfConfusion.apply(living, random, amount);

        if (CommonConfigs.RANDOM_ATTACK_DAMAGE.get()) {
            amount *= ModUtils.nextFloat(random,
                    CommonConfigs.RANDOM_ATTACK_DAMAGE_MIN.get().floatValue(),
                    CommonConfigs.RANDOM_ATTACK_DAMAGE_MAX.get().floatValue()
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
            PacketDistributor.sendToPlayer(serverPlayer, new EntityKilledPacketS2C(
                    serverPlayer.getStats().getValue(Stats.ENTITY_KILLED.get(entityType)),
                    BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
            ));
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
                        AttributeInstance instanceA = playerA.getAttribute(ModAttributes.getAggro());
                        AttributeInstance instanceB = playerB.getAttribute(ModAttributes.getAggro());
                        if (instanceA != null && instanceB != null) {
                            return (int) (instanceA.getValue() - instanceB.getValue());
                        }
                        return 0;
                    }).ifPresent(player -> {
                        if (player == playerO) return;
                        AttributeInstance instanceO = playerO.getAttribute(ModAttributes.getAggro());
                        AttributeInstance instance = player.getAttribute(ModAttributes.getAggro());
                        if (instanceO != null && instance != null && instanceO.getValue() < instance.getValue()) {
                            event.setNewAboutToBeSetTarget(player); // 只有当新目标的仇恨值大于旧目标时，才设置新目标
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void entityTick$Post(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living) {
            ModAttributes.applyPickupRange(living);
        }
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        if (event.loadedFromDisk() || !event.getLevel().isClientSide) return;
        if (event.getEntity() instanceof AbstractArrow arrow && arrow.getOwner() instanceof LivingEntity living) {
            ModAttributes.applyToArrow(living, arrow);
//            MoltenQuiver.applyToArrow(living, arrow);
        }
    }

    @SubscribeEvent
    public static void onDataPackLoad(AddReloadListenerEvent event) {
        event.addListener(CurioItemManager.INSTANCE);
    }

    @SubscribeEvent
    public static void playerTick$Pre(PlayerTickEvent.Pre event) {
        if (event.getEntity().isLocalPlayer()) {
            GravitationHandler.unCrouching(event.getEntity());
        }
    }
}
