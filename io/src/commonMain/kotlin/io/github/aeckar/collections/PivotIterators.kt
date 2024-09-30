package io.github.aeckar.collections

import kotlinx.io.RawSource

/**
 * Returns an iterator pivoting over the characters loaded from this source.
 *
 * Making this source buffered provides no performance benefit to the returned iterator.
 * If this is [closed][RawSource.close],
 * any function called from the returned instance throws an [IllegalStateException].
 */
public fun <V> RawSource.pivotIterator(init: () -> V): CharPivotIterator<*, V> {
    val revertible = SourceRevertibleIterator(this)
    return object : AbstractPivotIterator<Char, SourcePosition, V>(
        revertible,
        { init() }
    ), CharPivotIterator<SourcePosition, V>, CharRevertibleIterator<SourcePosition> by revertible {
        override fun peek() = revertible.peek() // Silences compiler warning
    }
}