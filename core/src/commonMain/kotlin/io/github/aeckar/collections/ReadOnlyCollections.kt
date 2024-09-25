package io.github.aeckar.collections

// ------------------------------ factories ------------------------------

/**
 * Returns a read-only view of this list.
 */
public fun <E> List<E>.readOnly(): ReadOnlyList<E> = ListView(this)

/**
 * Returns a read-only view over this set.
 */
public fun <E> Set<E>.readOnly(): ReadOnlySet<E> = SetView(this)

/**
 * Returns a read-only view over this multiset.
 */
public fun <E> MultiSet<E>.readOnly(): ReadOnlyMultiSet<E> = MultiSetView(this)

/**
 * Returns a read-only view over this map.
 */
public fun <K, V> Map<K, V>.readOnly(): ReadOnlyMap<K, V> = MapView(this)

// ------------------------------ interfaces ------------------------------

/**
 * A read-only view of another sequence of elements.
 */
public sealed interface ReadOnlyCollection<E> : Collection<E> {
    override fun toString(): String
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}

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

// ------------------------------ implementations ------------------------------

private abstract class View {
    protected abstract val original: Any

    final override fun toString() = original.toString()
    final override fun equals(other: Any?) = original == other
    final override fun hashCode() = original.hashCode()
}

private class ListView<E>(override val original: List<E>) : View(), ReadOnlyList<E>, List<E> by original {
    override fun subList(fromIndex: Int, toIndex: Int): ReadOnlyList<E> {
        return original.subList(fromIndex, toIndex).readOnly()
    }
}

private class SetView<E>(override val original: Set<E>) : View(), ReadOnlySet<E>, Set<E> by original
private class MultiSetView<E>(override val original: MultiSet<E>) : View(), ReadOnlyMultiSet<E>, MultiSet<E> by original
private class MapView<K, V>(override val original: Map<K, V>) : View(), ReadOnlyMap<K, V>, Map<K, V> by original