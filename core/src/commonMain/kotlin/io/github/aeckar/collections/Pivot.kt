package io.github.aeckar.collections

/**
 * Returns a [linker][link] for a [Pivot].
 */
public fun <P : Comparable<P>, V> pivots(): (Pair<P, V>) -> Pivot<P, V> = { Pivot(it.first, it.second) }

/**
 * An object containing a position and a value.
 * @param position the location of this in some larger object
 * @param value a value specific to the location of this
 */
public data class Pivot<P : Comparable<P>, out V>(
    public val position: P,
    override val value: V
) : ListNode<Pivot<P, @UnsafeVariance V>>(), ValueNode<V> {
    override fun toString(): String = "$value @ $position"
}

/**
 * Returns the pivot whose position has a [total ordering][Comparable] equal to the one given.
 *
 * If one does not exist, it is inserted according to the ordering of [P].
 */
public fun <V, P : Comparable<P>> Pivot<P, V>.getOrInsert(position: P, lazyValue: () -> V): Pivot<P, V> {
    /**
     * Assumes positions are not equal.
     */
    fun Pivot<P, V>.insert(): Pivot<P, V> {
        val pivot = Pivot(position, lazyValue())
        if (this.position > position) {
            insertBefore(pivot)
        } else {
            insertAfter(pivot)
        }
        return pivot
    }

    if (position.compareTo(this.position) == 0) {
        return this
    }
    var node = this
    if (this.position > position) {
        node = node.backtrace { node.position <= position }
        if (position.compareTo(node.position) == 0) {
            return node
        }
        return node.insert()
    }
    node = node.seek { node.position >= position }
    if (position.compareTo(node.position) == 0) {
        return node
    }
    return node.insert()
}