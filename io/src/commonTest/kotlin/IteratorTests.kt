import io.github.aeckar.collections.*
import io.github.aeckar.collections.test.TEST_STRING
import io.github.aeckar.collections.test.testPivoting
import io.github.aeckar.collections.test.testReverting
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlin.test.Test
import kotlin.test.assertEquals

class IteratorTests {
    @Test
    fun revertible_source_iterator() {
        val source = SystemFileSystem.source(Path("src/commonTest/resources/source.txt"))
        source.revertibleIterator().testReverting(TEST_STRING.toList())
        // play with SECTION_SIZE, too
    }

    @Test
    fun pivoting_source_iterator() {
        fun getSource() = SystemFileSystem.source(Path("src/commonTest/resources/source.txt"))

        assertEquals(
            expected = getSource().buffered().readString(),
            actual = getSource().pivotIterator { mutableListOf(0) }.asSequence().joinToString(separator = "")
        )
        getSource().pivotIterator { mutableListOf(0) }.testReverting(TEST_STRING.toList())
        getSource().pivotIterator { mutableListOf(0) }.testPivoting()
        // play with SECTION_SIZE, too
    }
}