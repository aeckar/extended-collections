package io.github.aeckar.collections

import io.github.aeckar.collections.utils.checkBounds
import kotlin.experimental.ExperimentalTypeInference
import kotlin.math.absoluteValue

internal object EmptyMultiSet : ReadOnlyMultiSet<Any?> {
    override val size = 0

    override fun isEmpty() = true
    override fun containsAll(elements: Collection<Any?>) = elements.isEmpty()
    override fun contains(element: Any?) = false
    override fun count(element: Any?) = 0

    override fun iterator() = object : Iterator<Any?> {
        override fun hasNext() = false
        override fun next() = throw NoSuchElementException("Multiset is empty")
    }
}

// ------------------------------ factories ------------------------------

/**
 * Returns a read-only view over a new multiset.
 */
@OptIn(ExperimentalTypeInference::class)
public fun <E> buildMultiSet(@BuilderInference builder: MutableMultiSet<E>.() -> Unit): ReadOnlyMultiSet<E> {
    return HashMultiSet<E>().apply(builder).readOnly()
}

/**
 * Returns an empty, read-only multiset.
 */
@Suppress("UNCHECKED_CAST")
public fun <E> emptyMultiSet(): ReadOnlyMultiSet<E> = EmptyMultiSet as ReadOnlyMultiSet<E>

/**
 * Returns an empty, read-only multiset.
 *
 * Equivalent to calling [emptyMultiSet].
 */
public fun <E> multiSetOf(): ReadOnlyMultiSet<E> = emptyMultiSet()

/**
 * Returns a read-only multiset containing the given elements.
 */
public fun <E> multiSetOf(vararg elements: E): ReadOnlyMultiSet<E> = mutableMultiSetOf(*elements).readOnly()

/**
 * Returns an empty, mutable multiset.
 */
public fun <E> mutableMultiSetOf(): MutableMultiSet<E> = HashMultiSet()

/**
 * Returns a mutable multiset containing the given elements.
 */
public fun <E> mutableMultiSetOf(vararg elements: E): MutableMultiSet<E> {
    val set = HashMultiSet<E>((elements.size * 1.25).toInt())
    elements.forEach(set::add)
    return set
}

// ------------------------------ interfaces ------------------------------

/**
 * A set capable of containing the same value multiple times.
 * Also called a *bag*.
 *
 * Because duplicated elements are equivalent, they are discarded after they are counted.
 * To preserve duplicated elements, a set of lists should be used instead.
 * @sample io.github.aeckar.collections.samples.multiSet
 */
public interface MultiSet<E> : Set<E> {
    /**
     * Returns the number of times this element was inserted into this set.
     */
    public fun count(element: E): Int
}

/**
 * A mutable multiset.
 */
public interface MutableMultiSet<E> : MultiSet<E>, MutableSet<E>, MutableIterable<E>

// ------------------------------ implementations ------------------------------

/**
 * A multiset backed by a hashtable.
 * @param initialCapacity the initial capacity of the hash table
 * @param loadFactor a ratio that determines when the hash table is resized.
 * See the Java documentation of `HashSet` for more information
 */
public class HashMultiSet<E>(
    initialCapacity: Int = DEFAULT_SIZE,
    private val loadFactor: Double = DEFAULT_LOAD_FACTOR
) : AbstractSet<E>(), MutableMultiSet<E> {
    override var size: Int = 0
        private set

    private var table = Array<Any?>(initialCapacity) { Absent }
    private var counters = IntArray(initialCapacity)

    init {
        require(loadFactor > 0.0 && loadFactor <= 1.0) { "Invalid load factor: $loadFactor" }
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        elements.forEach { removeAt(hashIndexOf(it)) }
        return elements.isNotEmpty()
    }

    override fun remove(element: E): Boolean {
        removeAt(hashIndexOf(element))
        return true
    }

    override fun count(element: E): Int {
        val index = hashIndexOf(element)
        return if (element != table[index]) 0 else counters[index]
    }

    private fun removeAt(index: Int) {
        checkBounds { table[index] = Absent }
        counters[index] = 0
        --size
    }

    /**
     * Returns an iterator over the distinct elements in this set.
     */
    override fun iterator(): MutableIterator<E> = object : IndexableIterator<E>(), MutableIterator<E> {
        init {
            moveToNext()
        }

        override fun remove() = removeAt(position)
        override fun hasNext() = position != table.size

        @Suppress("UNCHECKED_CAST")
        override fun next(): E {
            val element = checkBounds { table[position++] }
            moveToNext()
            return element as E
        }

        private fun moveToNext() {
            while (position < table.size && table[position] === Absent) { ++position }
        }
    }

    override fun clear() {
        table = Array(DEFAULT_SIZE) { Absent }
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        return mutableOrEach { value ->
            elements.none { it == value } implies ::remove
        }
    }

    override fun addAll(elements: Collection<E>): Boolean {
        verifyLoad(sizeAugment = elements.size)
        return elements.orEach(::hash)
    }

    override fun add(element: E): Boolean {
        verifyLoad(sizeAugment = 1)
        return hash(element)
    }

    private fun verifyLoad(sizeAugment: Int) {
        if ((size + sizeAugment) / table.size <= loadFactor) {
            return
        }
        val lastTable = table
        val lastCounters = counters
        val capacity = table.size * GROWTH_FACTOR
        table = Array(capacity) { Absent }
        counters = IntArray(capacity)
        repeat(capacity) {
            val element = lastTable[it]
            if (element !== Absent) {
                rehash(element, lastCounters[it])
            }
        }
    }

    /**
     * Returns false if the given element is a duplicate.
     */
    private fun hash(element: Any?): Boolean {
        var index = element.hashCode().absoluteValue % table.size
        try {
            while (table[index] !== Absent) {
                if (table[index] == element) {
                    return false
                }
                ++index
            }
            table[index] = element
            return true
        } finally {
            ++size
            ++counters[index]
        }
    }

    private fun hashIndexOf(element: Any?): Int {
        var index = element.hashCode().absoluteValue % table.size
        while (table[index] !== Absent) {
            if (table[index] == element) {
                break
            }
            ++index
        }
        return index
    }

    private fun rehash(element: Any?, count: Int) {
        val index = hashIndexOf(element)
        table[index] = element
        counters[index] = count
    }

    private data object Absent
    
    private companion object {  // Default values according to Java
        const val DEFAULT_SIZE = 16
        const val DEFAULT_LOAD_FACTOR = 0.75
        const val GROWTH_FACTOR = 2
    }
}