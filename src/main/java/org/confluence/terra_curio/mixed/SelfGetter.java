package org.confluence.terra_curio.mixed;

@SuppressWarnings("unchecked")
public interface SelfGetter<T> {
    default T self(){
        return (T) this;
    }
}
