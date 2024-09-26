@file:Suppress("unused")
package io.github.aeckar.collections.samples

import io.github.aeckar.collections.*

private fun revertibleIterator() {
    val chars = "Hello, world!".revertibleIterator()
    chars.save()
    chars.advance(7)
    println(Iterable { chars }.joinToString(""))    // world!
    chars.revert()
    println(Iterable { chars }.joinToString(""))    // Hello, world!
}

private fun pivotIterator() {
    val chars = "Hello, world!".pivotIterator { arrayOf(0) }
    while (chars.hasNext()) {
        chars.here()[0] = chars.nextChar().code
    }
    println(chars.pivots().map { it.value })    // [72, 101, 108, ... 33]
}

private fun linkedList() {
    val nodes = link(values(), 1, 2, 3)
    println(nodes.toList())   // [1, 2, 3]
}

private fun tree() {
    val tree = buildTree("5") { // Values indicate iteration order
        this += "0"
        this += "3".."2".."1"
        this += "4"
    }
    println(tree)               // 4 (root node)
    println(tree.treeString())  // 5
                                // ├── 0
                                // ├── 3
                                // │   └── 2
                                // │       └── 1
                                // └── 4
}

private fun multiSet() {
    val elements = multiSetOf("Hello, world!", 'K', 16, 3.14, 16)
    println(elements.size)          // 5
    println(elements.toList().size) // 4 (number of unique elements)
    println(elements.count(16))     // 2
}