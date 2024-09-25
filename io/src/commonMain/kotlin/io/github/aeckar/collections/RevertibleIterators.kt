package io.github.aeckar.collections

import kotlinx.io.Buffer
import kotlinx.io.RawSource
import kotlinx.io.readString

private const val SECTION_SIZE = 8192L

internal data class SourcePosition(
    val bufferSection: Int,
    val position: Int
) : Comparable<SourcePosition> {
    override fun compareTo(other: SourcePosition): Int {
        val bufferDiff = bufferSection - other.bufferSection
        if (bufferDiff != 0) {
            return bufferDiff
        }
        return position - other.position
    }
}

/**
 * Returns a revertible iterator over the characters loaded from this source.
 *
 * Making this source buffered provides no performance benefit to the returned iterator.
 * If this is [closed][RawSource.close],
 * any function called from the returned instance throws an [IllegalStateException].
 */
public fun RawSource.revertibleIterator(): CharRevertibleIterator<*> = SourceRevertibleIterator(this)

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
        init
    ), CharPivotIterator<SourcePosition, V>, CharRevertibleIterator<SourcePosition> by revertible {}
}

internal class SourceRevertibleIterator(private val source: RawSource) : CharRevertibleIterator<SourcePosition> {
    private val buffer = mutableListOf("")  // Allows initial hasNext()
    private val savedPositions = mutableListOf<SourcePosition>()
    private var section = 0
    private var sectionPosition = 0

    override fun next() = nextChar()
    override fun position() = SourcePosition(section, sectionPosition)
    override fun hasNext() = sectionPosition < buffer[section].length || loadSection()
    override fun nextChar() = peekChar().also { ++sectionPosition }

    override fun advance(places: Int) {
        sectionPosition += places
    }

    override fun save() {
        savedPositions += position()
    }

    override fun revert() {
        removeLastSave().let { (section, position) ->
            this.section = section
            this.sectionPosition = position
        }
    }

    override fun removeSave() {
        removeLastSave()
    }

    override fun peekChar(): Char {
        verifyPosition()
        return buffer[section][sectionPosition]
    }

    override fun toString(): String {
        val message = if (isExhausted()) "<iterator exhausted>" else "'${peekChar()}'"
        return message + " (position = ${absolutePosition()})"
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        if (other !is SourceRevertibleIterator) {
            return false
        }
        return source === other.source && section == other.section && sectionPosition == other.sectionPosition
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + section
        result = 31 * result + sectionPosition
        return result
    }

    private fun removeLastSave(): SourcePosition {
        return try {
            savedPositions.removeLast()
        } catch (_: NoSuchElementException) {
            error("No positions saved")
        }
    }

    /**
     * Returns true if the next section exists, or false if this input stream has been exhausted.
     */
    private fun loadSection(): Boolean {
        if (section < buffer.lastIndex) {
            return true
        }
        val nextSection = Buffer().apply { source.readAtMostTo(this, SECTION_SIZE) }.readString()
        // Throws IllegalStateException when source is closed
        if (nextSection.isEmpty()) {
            return false
        }
        buffer += nextSection
        return true
    }

    private fun verifyPosition() {
        while (sectionPosition >= buffer[section].length) {
            sectionPosition -= buffer[section].length
            if (!loadSection()) {
                throw NoSuchElementException("Source is exhausted at position ${absolutePosition()}")
            }
            ++section
        }
    }

    private fun absolutePosition(): Int {
        if (section == 0) {
            return sectionPosition
        }
        return buffer.asSequence().take(section - 1).sumOf { it.length } + sectionPosition
    }
}