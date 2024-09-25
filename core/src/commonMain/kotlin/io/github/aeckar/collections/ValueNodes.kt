package io.github.aeckar.collections

// Because of limitations on generics, it would be impossible to implement a MutableValueTreeNode

/**
 * A node, of some larger data structure, containing a value.
 */
public interface ValueNode<out V> {
    public val value: V
}

/**
 * A doubly-linked list node with an assigned value.
 */
public data class ValueListNode<out V>(
    override val value: V
) : ListNode<ValueListNode<@UnsafeVariance V>>(), ValueNode<V> {
    override fun toString(): String = value.toString()
}

/**
 * A tree node with an assigned value.
 */
public interface ValueTreeNode<out V> : TreeNode<ValueTreeNode<@UnsafeVariance V>>, ValueNode<V>