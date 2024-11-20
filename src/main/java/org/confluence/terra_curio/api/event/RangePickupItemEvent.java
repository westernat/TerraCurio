package org.confluence.terra_curio.api.event;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.confluence.terra_curio.mixin.accessor.ItemEntityAccessor;

public abstract class RangePickupItemEvent extends PlayerEvent {
    public RangePickupItemEvent(Player entity) {
        super(entity);
    }

    public static class Pre extends RangePickupItemEvent {
        private final float originalRange;
        private float range;

        public Pre(Player entity, float range) {
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
        private final float originalRange;
        private float distanceTo = -1.0F;

        public Post(Player entity, ItemEntity itemEntity, float range) {
            super(entity);
            this.itemEntity = itemEntity;
            this.originalRange = range;
        }

        public ItemEntity getItemEntity() {
            return itemEntity;
        }

        public float getOriginalRange() {
            return originalRange;
        }

        public int getPickupDelay() {
            return ((ItemEntityAccessor) itemEntity).getPickupDelay();
        }

        public float getDistanceTo() {
            if (distanceTo == -1.0F) {
                this.distanceTo = getEntity().distanceTo(itemEntity);
            }
            return distanceTo;
        }

        public boolean canPickupWithin(float range) {
            return range >= getDistanceTo();
        }
    }
}
