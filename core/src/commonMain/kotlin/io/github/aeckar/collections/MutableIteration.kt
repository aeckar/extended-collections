package io.github.aeckar.collections

/**
 * Returns a removable object referring to the current object pointed to by this iterator.
 */
public fun MutableIterator<*>.asRemovable(): Removable = Removable { remove() }

/**
 * A removable object.
 */
public fun interface Removable {
    public fun remove()
}

/**
 * A sequence of elements that returns a mutable iterator.
 */
public interface MutableIterable<T> : Iterable<T> {
    override fun iterator(): MutableIterator<T>
}