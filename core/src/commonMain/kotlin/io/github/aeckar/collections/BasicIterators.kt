package io.github.aeckar.collections

import kotlin.js.JsName

/**
 * A sequence of characters providing unboxed access to each element.
 * @see kotlin.collections.CharIterator
 */
public interface CharIterator : Iterator<Char> {
    /**
     * Returns the [next] unboxed character.
     */
    public fun nextChar(): Char

    override fun next(): Char
}

/**
 * A basic iterator containing the current index, starting from 0.
 */
public abstract class IndexableIterator<out T> : Iterator<T> {
    /**
     * The index of the element being pointed to by the iterator.
     */
    @JsName("currentPosition")
    protected var position: Int = 0
}