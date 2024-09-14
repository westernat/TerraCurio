package org.confluence.mod.terra_curio.common.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.entity.projectile.BeeProjectile;
import org.confluence.mod.terra_curio.common.entity.projectile.StarCloakEntity;


@SuppressWarnings("unused")
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, TerraCurio.MOD_ID);

    public static final DeferredHolder<EntityType<?>,EntityType<BeeProjectile>> BEE_PROJECTILE = ENTITIES.register("bee_projectile", () -> EntityType.Builder.<BeeProjectile>of(BeeProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).build("confluence:bee_projectile"));
    public static final DeferredHolder<EntityType<?>,EntityType<StarCloakEntity>> STAR_CLOAK = ENTITIES.register("star_cloak", () -> EntityType.Builder.<StarCloakEntity>of(StarCloakEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(16).updateInterval(20).build("confluence:star_cloak"));
    public static final DeferredHolder<EntityType<?>,EntityType<StepStoolEntity>> STEP_STOOL = ENTITIES.register("step_stool", () -> EntityType.Builder.<StepStoolEntity>of(StepStoolEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).build("confluence:step_stool"));
}
