package org.confluence.terra_curio.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import org.confluence.terra_curio.mixin.accessor.ItemEntityAccessor;

public abstract class RangePickupItemEvent extends LivingEvent {
    public RangePickupItemEvent(LivingEntity entity) {
        super(entity);
    }

    public static class Pre extends RangePickupItemEvent {
        private final float originalRange;
        private float range;

        public Pre(LivingEntity entity, float range) {
            super(entity);
            this.originalRange = range;
            this.range = range;
        }

        public float getOriginalRange() {
            return originalRange;
        }

        public void setRange(float range) {
            this.range = range;
        }

        public float getRange() {
            return range;
        }
    }

    public static class Post extends RangePickupItemEvent implements ICancellableEvent {
        private final ItemEntity itemEntity;

        public Post(LivingEntity entity, ItemEntity itemEntity) {
            super(entity);
            this.itemEntity = itemEntity;
        }

        public ItemEntity getItemEntity() {
            return itemEntity;
        }

        public int getPickupDelay() {
            return ((ItemEntityAccessor) itemEntity).getPickupDelay();
        }
    }
}
