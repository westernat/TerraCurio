package org.confluence.mod.terra_curio.common.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.confluence.mod.terra_curio.common.capability.strategy.AttackEntityStrategy;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;


public class CuriosAbilityProvider implements ICapabilityProvider<Player,Void,CuriosAbility> , INBTSerializable<CompoundTag> {
    private CuriosAbility curiosAbility;
    private CuriosAbility get(){
        if(curiosAbility == null){
            curiosAbility = new CuriosAbility();
            //curiosAbility.attackAbility.add(AttackEntityStrategy.FIRE_ATTACK);
        }

        return curiosAbility;
    }

    @Override
    public @Nullable CuriosAbility getCapability(Player object, Void context) {
        return get();
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return get().saveNBTData();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        get().loadNBTData(nbt);
    }
}
