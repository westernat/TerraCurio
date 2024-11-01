package org.confluence.terra_curio.mixinauxi;

@SuppressWarnings("unchecked")
public interface SelfGetter<T> {
    default T self(){
        return (T) this;
    }
}
