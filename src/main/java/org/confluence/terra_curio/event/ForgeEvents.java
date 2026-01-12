package org.confluence.terra_curio.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.capability.ability.AbilityProvider;
import org.confluence.terra_curio.capability.ability.PlayerAbility;
import org.confluence.terra_curio.item.curio.combat.*;
import org.confluence.terra_curio.item.curio.expert.BrainOfConfusion;
import org.confluence.terra_curio.item.curio.expert.WormScarf;
import org.confluence.terra_curio.item.curio.informational.IDPSMeter;
import org.confluence.terra_curio.item.curio.movement.IFallResistance;
import org.confluence.terra_curio.misc.ModAttributes;
import org.confluence.terra_curio.misc.ModConfigs;
import org.confluence.terra_curio.network.NetworkHandler;
import org.confluence.terra_curio.network.s2c.EntityKilledPacketS2C;
import org.confluence.terra_curio.network.s2c.InfoCurioCheckPacketS2C;

@Mod.EventBusSubscriber(modid = TerraCurio.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEvents {
    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity living) {
            if (PlayerAbility.of(living).isPresent()) return;
            event.addCapability(TerraCurio.asResource("ability"), new AbilityProvider());
        }
    }

    @SubscribeEvent
    public static void livingDamage(LivingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;
        DamageSource damageSource = event.getSource();
        if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) || damageSource.is(DamageTypes.GENERIC_KILL)) return;
        float amount = event.getAmount();
        if (amount <= 0) return; // 防止莫名的负数伤害

        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            IFireAttack.apply(victim, attacker);

            if (!ModAttributes.hasCustomAttribute(ModAttributes.CRIT_CHANCE.get())) {
                AttributeInstance instance = attacker.getAttribute(ModAttributes.CRIT_CHANCE.get());
                if (instance != null && attacker.getRandom().nextFloat() < instance.getValue()) {
                    event.setAmount(amount * 1.5F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void livingHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;
        DamageSource damageSource = event.getSource();
        if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) || damageSource.is(DamageTypes.GENERIC_KILL)) return;
        RandomSource random = victim.getRandom();
        float amount = event.getAmount();
        if (amount <= 0) return; // 防止莫名的负数伤害

        IHoneycomb.apply(victim, random);
        IStarCloak.apply(victim, random);
        PanicNecklace.apply(victim);

        amount = ModAttributes.applyMagicDamage(damageSource, amount);
        amount = ModAttributes.applyRangedDamage(damageSource, amount);
        amount = PaladinsShield.apply(victim, damageSource, amount);
        amount = FrozenTurtleShell.apply(victim, amount);
        amount = ILavaHurtReduce.apply(victim, damageSource, amount);
        amount = IFallResistance.apply(victim, damageSource, amount);
        amount = WormScarf.apply(victim, amount);
        amount = BrainOfConfusion.apply(victim, random, amount);

        if (ModConfigs.RANDOM_ATTACK_DAMAGE.get()) {
            amount *= Mth.nextFloat(random,
                    ModConfigs.RANDOM_ATTACK_DAMAGE_MIN.get().floatValue(),
                    ModConfigs.RANDOM_ATTACK_DAMAGE_MAX.get().floatValue()
            );
        }
        IDPSMeter.sendMsg(amount, damageSource.getEntity());
        event.setAmount(amount);
    }

    @SubscribeEvent
    public static void livingDeath(LivingDeathEvent event) {
        LivingEntity living = event.getEntity();
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            EntityType<?> entityType = living.getType();
            NetworkHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new EntityKilledPacketS2C(
                            serverPlayer.getStats().getValue(Stats.ENTITY_KILLED.get(entityType)),
                            ForgeRegistries.ENTITY_TYPES.getKey(entityType)
                    )
            );
        }
    }

    @SubscribeEvent
    public static void livingChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity self = event.getEntity();
        if (!(self instanceof Enemy)) return;
        if (event.getNewTarget() instanceof Player playerO) { // 当新目标为玩家时
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
                            event.setNewTarget(player); // 只有当新目标的仇恨值大于旧目标时，才设置新目标
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();
        ModAttributes.applyPickupRange(living);
        if (living.level() instanceof ServerLevel level && level.getGameTime() % 200 == 0) {
            // 每十秒向周围玩家共享一次信息配饰
            InfoCurioCheckPacketS2C.sendToOthers(level, living);
        }
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        if (event.loadedFromDisk() || event.getLevel().isClientSide) return;
        if (event.getEntity() instanceof AbstractArrow arrow && arrow.getOwner() instanceof LivingEntity living) {
            ModAttributes.applyToArrow(living, arrow);
            MoltenQuiver.applyToArrow(living, arrow);
        }
    }
}
