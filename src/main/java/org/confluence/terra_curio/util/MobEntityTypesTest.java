package org.confluence.terra_curio.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class MobEntityTypesTest implements EntityTypeTest<Entity, Mob> {
    private final Set<EntityType<?>> values;

    public MobEntityTypesTest(Set<EntityType<?>> values) {
        this.values = values;
    }

    @Override
    public @Nullable Mob tryCast(Entity entity) {
        return (Mob) (values.contains(entity.getType()) ? entity : null);
    }

    @Override
    public Class<Mob> getBaseClass() {
        return Mob.class;
    }
}
