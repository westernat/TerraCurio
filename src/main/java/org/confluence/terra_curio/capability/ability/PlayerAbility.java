package org.confluence.terra_curio.capability.ability;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.confluence.terra_curio.item.curio.ILavaImmune;
import org.confluence.terra_curio.item.curio.combat.IFireImmune;
import org.confluence.terra_curio.item.curio.combat.IInvulnerableTime;
import org.confluence.terra_curio.item.curio.movement.IFallResistance;
import org.confluence.terra_curio.item.curio.movement.IFluidWalk;
import org.confluence.terra_curio.item.curio.movement.IJumpBoost;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class PlayerAbility implements INBTSerializable<CompoundTag> {
    private double jumpBoost;
    private int fallResistance;
    private int invulnerableTime;
    private boolean fireImmune;
    private int maxLavaImmuneTicks;
    private transient int remainLavaImmuneTicks;
    private final List<TagKey<Fluid>> fluidWalk = new ArrayList<>();

    public PlayerAbility() {
        this.jumpBoost = 1.0;
        this.fallResistance = 0;
        this.invulnerableTime = 20;
        this.fireImmune = false;
        this.maxLavaImmuneTicks = 0;
        this.remainLavaImmuneTicks = 0;
    }

    public void flushAbility(LivingEntity living) {
        AtomicDouble jump = new AtomicDouble(1.0);
        AtomicInteger fall = new AtomicInteger();
        AtomicInteger invul = new AtomicInteger(20);
        AtomicBoolean fire = new AtomicBoolean();
        AtomicInteger lava = new AtomicInteger();
        Set<TagKey<Fluid>> fluids = new HashSet<>();
        Set<Class<?>> interfaces = new HashSet<>();
        CuriosApi.getCuriosInventory(living).ifPresent(handler -> {
            IItemHandlerModifiable itemHandlerModifiable = handler.getEquippedCurios();
            for (int i = 0; i < itemHandlerModifiable.getSlots(); i++) {
                ItemStack itemStack = itemHandlerModifiable.getStackInSlot(i);
                Item item = itemStack.getItem();
                if (item instanceof IJumpBoost iJumpBoost) jump.set(Math.max(iJumpBoost.getBoost(), jump.get()));
                if (item instanceof IFallResistance iFallResistance && fall.get() != -1) {
                    int distance = iFallResistance.getFallResistance();
                    fall.set(distance < 0 ? -1 : Math.max(distance, fall.get()));
                }
                if (item instanceof IInvulnerableTime iInvulnerableTime) {
                    invul.set(Math.max(iInvulnerableTime.getTime(), invulnerableTime));
                }
                if (item instanceof IFireImmune) fire.set(true);
                if (item instanceof ILavaImmune iLavaImmune) {
                    lava.set(Math.max(iLavaImmune.getLavaImmuneTicks(), lava.get()));
                }
                if (item instanceof IFluidWalk iFluidWalk) {
                    fluids.addAll(iFluidWalk.canStandOn());
                }
            }
        });
        this.jumpBoost = jump.get();
        this.fallResistance = fall.get();
        this.invulnerableTime = invul.get();
        this.fireImmune = fire.get();
        this.maxLavaImmuneTicks = lava.get();
        this.fluidWalk.clear();
        this.fluidWalk.addAll(fluids);
    }

    public double getJumpBoost() {
        return jumpBoost;
    }

    public int getFallResistance() {
        return fallResistance;
    }

    public int getInvulnerableTime() {
        return invulnerableTime;
    }

    public boolean isFireImmune() {
        return fireImmune;
    }

    public void increaseLavaImmuneTicks() {
        if (remainLavaImmuneTicks < maxLavaImmuneTicks) {
            remainLavaImmuneTicks++;
        }
    }

    public boolean decreaseLavaImmuneTicks() {
        if (remainLavaImmuneTicks > 0) {
            remainLavaImmuneTicks--;
            return true;
        }
        return false;
    }

    public boolean canWalkOnFluid(FluidState state) {
        return fluidWalk.stream().anyMatch(state::is);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("jumpBoost", jumpBoost);
        nbt.putInt("fallResistance", fallResistance);
        nbt.putInt("invulnerableTime", invulnerableTime);
        nbt.putBoolean("fireImmune", fireImmune);
        nbt.putInt("maxLavaImmuneTicks", maxLavaImmuneTicks);
        ListTag listTag = new ListTag();
        Codec<TagKey<Fluid>> codec = TagKey.codec(Registries.FLUID);
        for (TagKey<Fluid> tag : fluidWalk) {
            codec.encodeStart(NbtOps.INSTANCE, tag).result().ifPresent(listTag::add);
        }
        nbt.put("fluidWalk", listTag);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.jumpBoost = nbt.getDouble("jumpBoost");
        this.fallResistance = nbt.getInt("fallResistance");
        this.invulnerableTime = nbt.getInt("invulnerableTime");
        this.fireImmune = nbt.getBoolean("fireImmune");
        this.maxLavaImmuneTicks = nbt.getInt("maxLavaImmuneTicks");
        this.fluidWalk.clear();
        ListTag listTag = nbt.getList("fluidWalk", Tag.TAG_COMPOUND);
        Codec<TagKey<Fluid>> codec = TagKey.codec(Registries.FLUID);
        for (Tag tag : listTag) {
            codec.decode(NbtOps.INSTANCE, tag).result().ifPresent(pair -> fluidWalk.add(pair.getFirst()));
        }
    }

    public void copyFrom(PlayerAbility playerAbility) {
        this.jumpBoost = playerAbility.jumpBoost;
        this.fallResistance = playerAbility.fallResistance;
        this.invulnerableTime = playerAbility.invulnerableTime;
        this.fireImmune = playerAbility.fireImmune;
        this.maxLavaImmuneTicks = playerAbility.maxLavaImmuneTicks;
        this.fluidWalk.clear();
        this.fluidWalk.addAll(playerAbility.fluidWalk);
    }

    public static LazyOptional<PlayerAbility> of(LivingEntity living) {
        return living.getCapability(AbilityProvider.CAPABILITY);
    }
}
