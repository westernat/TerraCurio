package org.confluence.terra_curio.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.entity.BeeProjectile;
import org.confluence.terra_curio.common.entity.StarCloakEntity;
import org.confluence.terra_curio.common.entity.StepStoolEntity;
import org.confluence.terra_curio.common.entity.XBoneProjectile;

@SuppressWarnings("unused")
public final class TCEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, TerraCurio.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<BeeProjectile>> BEE_PROJECTILE = ENTITIES.register("bee_projectile", id -> EntityType.Builder.<BeeProjectile>of(BeeProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).build(asKey(id)));
    public static final DeferredHolder<EntityType<?>, EntityType<StarCloakEntity>> STAR_CLOAK = ENTITIES.register("star_cloak", id -> EntityType.Builder.<StarCloakEntity>of(StarCloakEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(16).updateInterval(20).build(asKey(id)));
    public static final DeferredHolder<EntityType<?>, EntityType<StepStoolEntity>> STEP_STOOL = ENTITIES.register("step_stool", id -> EntityType.Builder.<StepStoolEntity>of(StepStoolEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).build(asKey(id)));
    public static final DeferredHolder<EntityType<?>, EntityType<XBoneProjectile>> X_BONE = ENTITIES.register("x_bone", id -> EntityType.Builder.of(XBoneProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).build(asKey(id)));

    private static ResourceKey<EntityType<?>> asKey(Identifier id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, id);
    }
}
