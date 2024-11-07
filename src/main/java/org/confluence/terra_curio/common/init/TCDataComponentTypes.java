package org.confluence.terra_curio.common.init;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.component.EffectImmunities;
import org.confluence.terra_curio.common.component.ModRarity;
import org.confluence.terra_curio.common.component.NbtComponent;

import java.util.function.Supplier;

public final class TCDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TerraCurio.MODID);

    public static final Supplier<DataComponentType<EffectImmunities>> EFFECT_IMMUNITIES = TYPES.register("effect_immunities", () -> DataComponentType.<EffectImmunities>builder().persistent(EffectImmunities.CODEC).networkSynchronized(EffectImmunities.STREAM_CODEC).cacheEncoding().build());
    public static final Supplier<DataComponentType<ModRarity>> MOD_RARITY = TYPES.register("mod_rarity", () -> DataComponentType.<ModRarity>builder().persistent(ModRarity.CODEC).networkSynchronized(ModRarity.STREAM_CODEC).cacheEncoding().build());
    public static final Supplier<DataComponentType<AccessoriesComponent>> ACCESSORIES = TYPES.register("accessories", () -> DataComponentType.<AccessoriesComponent>builder().persistent(AccessoriesComponent.CODEC).networkSynchronized(AccessoriesComponent.STREAM_CODEC).cacheEncoding().build());
    public static final Supplier<DataComponentType<NbtComponent>> NBT = TYPES.register("nbt", () -> DataComponentType.<NbtComponent>builder().persistent(NbtComponent.CODEC).networkSynchronized(NbtComponent.STREAM_CODEC).cacheEncoding().build());
}
