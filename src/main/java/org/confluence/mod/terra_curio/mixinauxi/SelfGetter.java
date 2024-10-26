package org.confluence.mod.terra_curio.mixinauxi;

@SuppressWarnings("unchecked")
public interface SelfGetter<T> {
    default T self(){
        return (T) this;
    }
}
