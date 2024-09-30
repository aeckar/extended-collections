package io.github.aeckar.collections

import io.github.aeckar.collections.utils.checkBounds

/*
    Specialized forEach() doesn't make sense for revertible, pivoting iterators
    since they are typically stored to a variable.
 */

/**
 * Returns a revertible iterator over the elements in the list.
 */
public fun <E> List<E>.revertibleIterator(): RevertibleIterator<E, Int> = ListRevertibleIterator(this)

/**
 * Returns a revertible iterator over the characters in this string.
 */
public fun String.revertibleIterator(): CharRevertibleIterator<Int> = StringRevertibleIterator(this)

// ------------------------------ interfaces & implementations ------------------------------

/**
 * A sequence of elements whose position can be saved and reverted to later.
 *
 * ```kotlin
 *     val chars = "Hello, world!".revertibleIterator()
 *     chars.save()
 *     chars.advance(7)
 *     println(Iterable { chars }.joinToString(""))    // world!
 *     chars.revert()
 *     println(Iterable { chars }.joinToString(""))    // Hello, world!
 * ```
 */
public interface RevertibleIterator<out E, out P> : Iterator<E> {
    /**
     * Advances the cursor pointing to the current element by the given number of places.
     * @throws IllegalArgumentException [places] is negative
     */
    public fun advance(places: Int)

    /**
     * Saves the current cursor position.
     *
     * Can be called more than once to save multiple positions, and even if [isExhausted] is true.
     */
    public fun save()

    /**
     * Reverts the position of the cursor to the one that was last [saved][save],
     * and removes it from the set of saved positions.
     * @throws IllegalStateException [save] has not been called prior
     */
    public fun revert()

    /**
     * Removes the cursor position last [saved][save] without reverting the current cursor position to it.
     * @throws IllegalStateException [save] has not been called prior
     */
    public fun removeSave()

    /**
     * Returns the next element in the sequence without advancing the current cursor position.
     * @throws NoSuchElementException the iterator is exhausted
     */
    public fun peek(): E

    /**
     * Returns true if no more elements can be read from this iterator
     * without reverting to a previously saved cursor position.
     *
     * Equal in value to `!`[hasNext]`.
     */
    public fun isExhausted(): Boolean = !hasNext()

    /**
     * Returns an object representing the current position of this iterator.
     */
    public fun position(): P

    /**
     * Returns true if [other] is a revertible iterator over the same instance at the same position as this one.
     */
    override fun equals(other: Any?): Boolean
}

/**
 * A revertible iterator over a sequence of characters.
 */
public interface CharRevertibleIterator<out P> : RevertibleIterator<Char, P>, CharIterator {
    /**
     * Returns a [peek] of the next unboxed character.
     * @throws NoSuchElementException the iterator is exhausted
     */
    public fun peekChar(): Char

    override fun peek(): Char = peekChar()
}

/**
 * A revertible iterator over an indexable sequence of elements.
 */
internal abstract class IndexableRevertibleIterator<out E> : IndexableIterator<E>(), RevertibleIterator<E, Int> {
    protected abstract val elements: Any?

    private val savedPositions = IntList()

    final override fun advance(places: Int) {
        position += places
    }

    final override fun save() {
        savedPositions += position
    }

    final override fun revert() {
        position = removeLastSave()
    }

    final override fun removeSave() {
        removeLastSave()
    }

    final override fun position() = position

    final override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        if (other !is IndexableRevertibleIterator<*>) {
            return false
        }
        return elements === other.elements && position == other.position
    }

    final override fun hashCode(): Int {
        var result = elements.hashCode()
        result = 31 * result + position
        return result
    }

    final override fun toString(): String {
        val message = if (isExhausted()) "<iterator exhausted>" else "${peek()}"
        return "$message (index = $position)"
    }

    private fun removeLastSave(): Int {
        return try {
            savedPositions.removeLast()
        } catch (_: NoSuchElementException) {
            error("No positions saved")
        }
    }
}

internal class ListRevertibleIterator<out E>(override val elements: List<E>) : IndexableRevertibleIterator<E>() {
    override fun hasNext() = position < elements.size
    override fun isExhausted() = position >= elements.size
    override fun next(): E = peek().also { ++position }
    override fun peek() = checkBounds { elements[position] }
}

internal class StringRevertibleIterator(
    override val elements: String
) : IndexableRevertibleIterator<Char>(), CharRevertibleIterator<Int> {
    override fun next() = nextChar()
    override fun hasNext() = position < elements.length
    override fun isExhausted() = position >= elements.length
    override fun nextChar() = peekChar().also { ++position }
    override fun peekChar() = checkBounds { elements[position] }
}