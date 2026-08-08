package org.confluence.terra_curio.common.init;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.confluence.lib.common.LibDamageTypes;
import org.jetbrains.annotations.ApiStatus;

@Deprecated(since = "1.2.0", forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "1.4.0")
public final class TCDamageTypes {
    public static final ResourceKey<DamageType> STAR_CLOAK = LibDamageTypes.STAR_CLOAK;

    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        return LibDamageTypes.of(level, key);
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing) {
        return LibDamageTypes.of(level, key, causing);
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing, Entity direct) {
        return LibDamageTypes.of(level, key, causing, direct);
    }
}
