@file:Suppress("UNCHECKED_CAST")
package io.github.aeckar.collections

import kotlin.js.JsName

// ------------------------------ factories ------------------------------

/**
 * Returns a [linker][link] for a [ValueListNode].
 */
public fun <V> values(): (V) -> ValueListNode<V> = { ValueListNode(it) }

/**
 * Returns the head of the doubly-linked list that is created when the nodes
 * with the given arguments are joined together in the same order.
 * @param linker initializes each list node using the provided arguments
 */
public inline fun <T, Self : ListNode<Self>> link(linker: (T) -> Self, first: T, vararg others: T): Self {
    val head = linker(first)
    var curNode = head
    for (argument in others) {
        val next = linker(argument)
        curNode.insertAfter(next)
        curNode = next
    }
    return head
}

/**
 * Returns an iterator over every element in this linked list, up to and including this node.
 */
public fun <Self : ListNode<Self>> Self?.reversed(): Iterable<Self> = Iterable {
    object : Iterator<Self> {
        var cursor: Self? = this@reversed as Self

        override fun hasNext() = cursor != null

        override fun next(): Self {
            val cursor = cursor ?: throw NoSuchElementException("Node is head")
            this.cursor = cursor.last
            return cursor
        }
    }
}

// ------------------------------ implementations ------------------------------

/**
 * Returns a list containing all nodes in this linked list.
 *
 * The mutability of the returned list cannot be guaranteed.
 * If the receiver is null, an empty list is returned.
 */
public fun <Self: ListNode<Self>> Self?.toList(): List<Self> {
    this ?: return listOf()
    val elements = this
    return mutableListOf<Self>().apply {
        addAll(elements.reversed())
        reverse()
        next?.let { addAll(it) }
    }
}

/**
 * An element in a doubly-linked list.
 *
 * Unlike in Java, this library does not provide a dedicated linked list class.
 * Instead, list nodes are operated on directly.
 *
 * A nullable property of this type whose value is null is considered an empty linked list.
 * Iterates over the elements in the linked list, starting from and including this one.
 *
 * ```kotlin
 *     val nodes = link(values(), 1, 2, 3)
 *     println(nodes.toList())   // [1, 2, 3]
 * ```
 */
public abstract class ListNode<Self : ListNode<Self>> : Iterable<Self> {
    @PublishedApi @JsName("nextNode") internal var next: Self? = null
    internal var last: Self? = null

    /**
     * Returns the next node in the linked list.
     * @throws NoSuchElementException this is the tail of the linked list
     */
    public fun next(): Self {
        return next ?: throw NoSuchElementException("Node is tail")
    }

    /**
     * Returns the previous node in the linked list.
     * @throws NoSuchElementException this is the head of the linked list
     */
    public fun last(): Self {
        return last ?: throw NoSuchElementException("Node is head")
    }

    /**
     * Inserts the given node directly after this one.
     * @throws IllegalArgumentException [node] is this same instance
     */
    public fun insertAfter(node: Self) {
        require(this !== node) { "Cannot append node to itself" }
        next?.apply { last = node }
        node.next = next
        node.last = this as Self
        next = node
    }

    /**
     * Inserts the given node directly before this one.
     * @throws IllegalArgumentException [node] is this same instance
     */
    public fun insertBefore(node: Self) {
        require(this !== node) { "Cannot append node to itself" }
        last?.apply { next = node }
        node.last = last
        node.next = this as Self
        last = node
    }

    /**
     * Returns the head of this linked list.
     */
    public fun head(): Self {
        return if (last == null) this as Self else reversed().last()
    }

    /**
     * Returns the tail of this linked list.
     */
    public fun tail(): Self {
        return if (next == null) this as Self else this.asIterable().last()
    }

    /**
     * Returns the node in this linked list that satisfies the given predicate,
     * or the tail of the list of one is not found.
     */
    public inline fun seek(predicate: (Self) -> Boolean): Self {
        var tail = this as Self
        for (element in this) {
            if (predicate(element)) {
                return element
            }
            tail = element
        }
        return tail
    }

    /**
     * Returns the node in this linked list that satisfies the given predicate,
     * or the head of the list if one is not found.
     */
    public inline fun backtrace(predicate: (Self) -> Boolean): Self {
        var head = this as Self
        for (element in reversed()) {
            if (predicate(element)) {
                return element
            }
            head = element
        }
        return head
    }

    /**
     * Returns an iterator over every element in this linked list, up to and including this node.
     */
    override fun iterator(): Iterator<Self> = object : Iterator<Self> {
        var cursor: Self? = this@ListNode as Self

        override fun hasNext() = cursor != null

        override fun next(): Self {
            val cursor = cursor ?: throw NoSuchElementException("Node is tail")
            this.cursor = cursor.next
            return cursor
        }
    }
}