package io.github.aeckar.collections

/**
 * Returns an iterator pivoting over the elements in the list.
 */
public fun <E, H> List<E>.pivotIterator(init: (Int) -> H): PivotIterator<E, Int, H> {
    val revertible = ListRevertibleIterator(this)
    return object : AbstractPivotIterator<E, Int, H>(
        revertible,
        init
    ), RevertibleIterator<E, Int> by revertible {}
}

/**
 * Returns an iterator pivoting over the characters in this string.
 */
public fun <H> String.pivotIterator(init: (Int) -> H): CharPivotIterator<Int, H> {
    val revertible = StringRevertibleIterator(this)
    return object : AbstractPivotIterator<Char, Int, H>(
        revertible,
        init
    ), CharPivotIterator<Int, H>, CharRevertibleIterator<Int> by revertible {
        override fun peek() = revertible.peek() // Silences compiler warning
    }
}

// ------------------------------ interfaces & implementations ------------------------------

/**
 * A revertible iterator over a sequence of elements, each of which is assigned some value.
 *
 * Use when it is necessary to map both positional data and metadata to elements in a sequence
 * by using revertible iteration.
 *
 * ```kotlin
 *     val chars = "Hello, world!".pivotIterator { arrayOf(0) }
 *     while (chars.hasNext()) {
 *         chars.here()[0] = chars.nextChar().code
 *     }
 *     println(chars.pivots().map { it.value })    // [72, 101, 108, ... 33]
 * ```
 */
public interface PivotIterator<out E, P : Comparable<P>, out V> : RevertibleIterator<E, P> {
    /**
     * Returns the value assigned to the current element.
     */
    public fun here(): V

    /**
     * Returns a list of the previously visited pivots, including the current one.
     *
     * Invoking this function may incur a significant performance impact for large sequences.
     */
    public fun pivots(): List<Pivot<P, V>>
}

/**
 * An iterator pivoting over a sequence of characters.
 */
public interface CharPivotIterator<P : Comparable<P>, out V> : PivotIterator<Char, P, V>, CharRevertibleIterator<P>

/**
 * A basic implementation of a pivoting iterator.
 * @param revertible a revertible iterator over the same sequence of elements
 * @param init returns the default value of each pivot
 */
@Suppress("EqualsOrHashCode")
public abstract class AbstractPivotIterator<out E, P : Comparable<P>, out V>(
    private val revertible: RevertibleIterator<E, P>,
    private val init: (P) -> V
) : PivotIterator<E, P, V>, RevertibleIterator<E, P> {
    private var cursor: Pivot<P, V>? = null

    final override fun here(): V {
        val position = revertible.position()
        val node = cursor?.getOrInsert(revertible.position()) { init(position) }
            ?: Pivot(revertible.position(), init(position))
        this.cursor = node
        return node.value
    }

    final override fun pivots(): List<Pivot<P, V>> = cursor.toList()
    final override fun toString(): String = revertible.toString()
    final override fun hashCode(): Int = revertible.hashCode()
}