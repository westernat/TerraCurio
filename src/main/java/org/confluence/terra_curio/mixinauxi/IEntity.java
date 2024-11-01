package org.confluence.terra_curio.mixinauxi;

public interface IEntity {
    int confluence$getCthulhuSprintingTime();

    void confluence$setCthulhuSprintingTime(int amount);

    default boolean confluence$isOnCthulhuSprinting() {
        return confluence$getCthulhuSprintingTime() > 20;
    }

    void confluence$setShouldRot(boolean bool);

    boolean confluence$isShouldRot();
}
