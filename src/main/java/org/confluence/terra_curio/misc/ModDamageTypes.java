package org.confluence.terra_curio.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.TerraCurio;

public final class ModDamageTypes {
    public static final ResourceKey<DamageType> STAR_CLOAK = register("star_cloak");

    private static ResourceKey<DamageType> register(String id) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, TerraCurio.asResource(id));
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        return of(level, key, null, null);
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing) {
        return of(level, key, causing, causing);
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing, Entity direct) {
        return new DamageSource(level.registryAccess().registry(Registries.DAMAGE_TYPE).orElseThrow().getHolderOrThrow(key), causing, direct);
    }
}
