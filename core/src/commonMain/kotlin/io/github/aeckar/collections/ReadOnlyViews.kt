package io.github.aeckar.collections

// Read-only view over an ordinary tree node impossible due to limitations on generics

// ------------------------------ factories ------------------------------

/**
 * Returns a read-only view over this collection.
 */
public fun <E> List<E>.readOnly(): ReadOnlyList<E> = ListView(this)

/**
 * Returns a read-only view over this collection.
 */
public fun <E> Set<E>.readOnly(): ReadOnlySet<E> = SetView(this)

/**
 * Returns a read-only view over this collection.
 */
public fun <E> MultiSet<E>.readOnly(): ReadOnlyMultiSet<E> = MultiSetView(this)

/**
 * Returns a read-only view over this map.
 */
public fun <K, V> Map<K, V>.readOnly(): ReadOnlyMap<K, V> = MapView(this)

/**
 * Returns a read-only view over this tree node.
 */
public fun <V> DataTreeNode<V>.readOnly(): ReadOnlyDataTreeNode<V> = DataTreeNodeView(this)

// ------------------------------ interfaces ------------------------------

/**
 * A read-only view of another sequence of elements.
 */
public sealed interface ReadOnlyCollection<E> : Collection<E>

/**
 * A read-only view of another list.
 */
public sealed interface ReadOnlyList<E> : ReadOnlyCollection<E>, List<E> {
    override fun subList(fromIndex: Int, toIndex: Int): ReadOnlyList<E>
}

/**
 * A read-only view over another set.
 */
public sealed interface ReadOnlySet<E> : Set<E>

/**
 * A read-only view over another multiset.
 */
public sealed interface ReadOnlyMultiSet<E> : MultiSet<E>, ReadOnlySet<E>

/**
 * A read-only view over another map.
 */
public sealed interface ReadOnlyMap<K, V> : Map<K, V>

/**
 * A read-only view over another, data-containing, tree node.
 *
 * Disallows modification of the [value] contained.
 */
public sealed interface ReadOnlyDataTreeNode<V> : TreeNode<ReadOnlyDataTreeNode<V>>, DataNode<V>

// ------------------------------ implementations ------------------------------

private abstract class View(private val original: Any) {
    final override fun toString() = original.toString()
    final override fun equals(other: Any?) = original == other
    final override fun hashCode() = original.hashCode()
}

private class SetView<E>(original: Set<E>) : View(original), ReadOnlySet<E>, Set<E> by original
private class MultiSetView<E>(original: MultiSet<E>) : View(original), ReadOnlyMultiSet<E>, MultiSet<E> by original
private class MapView<K, V>(original: Map<K, V>) : View(original), ReadOnlyMap<K, V>, Map<K, V> by original

private class ListView<E>(
    private val original: List<E>   // Prefer over override
) : View(original), ReadOnlyList<E>, List<E> by original {
    override fun subList(fromIndex: Int, toIndex: Int): ReadOnlyList<E> {
        return original.subList(fromIndex, toIndex).readOnly()
    }
}

private class DataTreeNodeView<V>(
    private val original: DataTreeNode<V>
) : View(original), ReadOnlyDataTreeNode<V> {
    override val value: V get() = original.value
    override val children: List<ReadOnlyDataTreeNode<V>> = super.children.readOnly()
}