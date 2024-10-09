package io.github.aeckar.collections

// ------------------------------ factories ------------------------------

/**
 *
 */
public fun pivotListOf();

/**
 *
 */
public fun pivotListOf()

// ------------------------------ implementation ------------------------------

/**
 * An object containing a position and a value.
 * @param position the location of this in some larger object
 * @param value a value specific to the location of this
 */
public class DataPivot<P : Comparable<P>, V> internal constructor(
    public val position: P,
    override val value: V
) : ListNode<DataPivot<P, V>>(), DataNode<V> {
    /**
     * Returns true if both positions and values are equal.
     */
    override fun equals(other: Any?): Boolean {
        return other is DataPivot<*, *> && position == other.position && value == other.value
    }

    override fun hashCode(): Int {
        var result = 31
        result = 31 * result + position.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    override fun toString(): String = "$value @ $position"
}

/**
 * Returns the pivot whose position has a [total ordering][Comparable] equal to the one given.
 *
 * If one does not exist, it is inserted according to the ordering of [P].
 */
public fun <V, P : Comparable<P>> DataPivot<P, V>.getOrInsert(position: P, lazyValue: () -> V): DataPivot<P, V> {
    /**
     * Assumes positions are not equal.
     */
    fun DataPivot<P, V>.insert(): DataPivot<P, V> {
        val pivot = DataPivot(position, lazyValue())
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