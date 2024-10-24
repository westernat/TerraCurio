//package org.confluence.mod.terra_curio.common.capability.ability;
//
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//
//import net.neoforged.neoforge.capabilities.ICapabilityProvider;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//public final class AbilityProvider implements ICapabilityProvider<PlayerAbility,Void,Void> {
//    public static final Capability<PlayerAbility> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
//    });
//    private PlayerAbility playerAbility;
//    private final LazyOptional<PlayerAbility> abilityLazyOptional = LazyOptional.of(this::getOrCreateStorage);
//
//    @Override
//    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
//        return CAPABILITY.orEmpty(cap, abilityLazyOptional);
//    }
//
//    @Override
//    public CompoundTag serializeNBT() {
//        return getOrCreateStorage().serializeNBT();
//    }
//
//    @Override
//    public void deserializeNBT(CompoundTag nbt) {
//        getOrCreateStorage().deserializeNBT(nbt);
//    }
//
//    private PlayerAbility getOrCreateStorage() {
//        if (playerAbility == null) {
//            this.playerAbility = new PlayerAbility();
//        }
//        return playerAbility;
//    }
//
//    @Override
//    public @Nullable Void getCapability(PlayerAbility o, Void unused) {
//        return null;
//    }
//}
