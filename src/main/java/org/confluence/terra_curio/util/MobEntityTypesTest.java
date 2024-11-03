package org.confluence.terra_curio.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobEntityTypesTest implements EntityTypeTest<Entity, Mob> {
    private final List<EntityType<?>> values;

    public MobEntityTypesTest(List<EntityType<?>> values) {
        this.values = values;
    }

    @Override
    public @Nullable Mob tryCast(Entity entity) {
        return (Mob) (values.contains(entity.getType()) ? entity : null);
    }

    @Override
    public @NotNull Class<Mob> getBaseClass() {
        return Mob.class;
    }
}
