package dev.jadss.jadapi.interfaces;

/**
 * This object can be copied into another object.
 * @param <T> The object it can be copied into.
 */
public interface Copyable<T> {

    /**
     * Copy this object into another object of T.
     * @return The object copied into.
     */
    T copy();
}
