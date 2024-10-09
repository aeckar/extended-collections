package io.github.aeckar.collections

/*
    Containment of values not supported by base implementations/interfaces to allow for
    flexibility of API design (see io.github.aeckar.parsing.SyntaxTreeNode)
*/

/**
 * A node, of some larger data structure, that contains an element.
 */
public interface DataNode<out E> {
    public val element: E
}

/**
 * Returns the element contained by this node.
 */
public operator fun <E> DataNode<E>.component1(): E = element

