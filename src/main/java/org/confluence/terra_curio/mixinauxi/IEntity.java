package org.confluence.terra_curio.mixinauxi;

public interface IEntity {
    int terra_curio$getCthulhuSprintingTime();

    void terra_curio$setCthulhuSprintingTime(int amount);

    default boolean terra_curio$isOnCthulhuSprinting() {
        return terra_curio$getCthulhuSprintingTime() > 20;
    }

    void terra_curio$setShouldRot(boolean bool);

    boolean terra_curio$isShouldRot();
}
