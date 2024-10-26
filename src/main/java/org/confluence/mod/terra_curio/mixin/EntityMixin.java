package org.confluence.mod.terra_curio.mixin;

import net.minecraft.world.entity.Entity;
import org.confluence.mod.terra_curio.mixinauxi.IEntity;
import org.confluence.mod.terra_curio.mixinauxi.SelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity, SelfGetter<Entity> {
    @Unique
    private int confluence$cthulhuSprintingTime = 0;
    @Unique
    private boolean confluence$isShouldRot = false;

    @Override
    public int confluence$getCthulhuSprintingTime() {
        return confluence$cthulhuSprintingTime;
    }

    @Override
    public void confluence$setCthulhuSprintingTime(int amount) {
        this.confluence$cthulhuSprintingTime = amount;
    }

    @Override
    public void confluence$setShouldRot(boolean bool) {
        this.confluence$isShouldRot = bool;
    }

    @Override
    public boolean confluence$isShouldRot() {
        return confluence$isShouldRot;
    }
}
