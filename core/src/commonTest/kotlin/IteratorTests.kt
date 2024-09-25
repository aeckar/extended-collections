import io.github.aeckar.collections.*
import io.github.aeckar.collections.test.TEST_STRING
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private val TEST_LIST = TEST_STRING.toList()

fun RevertibleIterator<*, *>.testReverting(elements: List<*>) {
    assertFailsWith<IllegalStateException> { revert() }
    save()
    save()
    next()
    removeSave()
    assertEquals(elements[1], next())
    revert()
    assertEquals(elements[0], next())
    advance(100)
    assertFailsWith<NoSuchElementException> { next() }
}

fun PivotIterator<*, *, MutableList<Int>>.testPivoting() {
    save()                      // [0] <-
    save()                      // [0, 0] <-
    save()                      // [0, 0, 0] <-
    next()                      // get 0, move to 1
    removeSave()                // [0, 0] -> (do nothing)
    next()                      // get 1, move to 2
    here()[0] = 12              // value at 2
    revert()                    // [0] -> move to 0
    advance(2)                  // move to 2
    assertEquals(12, here()[0]) // value at 2
    revert()                    // [] -> move to 0
    save()                      // [0] <-
    here()[0] = 10              // value at 0
    advance(7)                  // move to 7
    here()[0] = 17              // value at 7
    next()                      // get 7, move to 8
    here()                      // initialize 8
    revert()                    // [] -> move to 0
    advance(1)                  // move to 1
    here()[0] = 11              // value at 1
    assertContentEquals(
        expected = listOf(listOf(10), listOf(11), listOf(12), listOf(17), listOf(0)),
        actual = pivots().map { it.value }
    )
}

class IteratorTests {
    @Test
    fun `revertible list iterator`() {
        assertContentEquals(TEST_LIST, TEST_LIST.revertibleIterator().asSequence().toList())
        TEST_LIST.revertibleIterator().testReverting(TEST_LIST)
    }

    @Test
    fun `revertible string iterator`() {
        TEST_STRING.revertibleIterator().testReverting(TEST_STRING.toList())
    }



    @Test
    fun `pivoting list iterator`() {
        assertContentEquals(TEST_LIST, TEST_LIST.pivotIterator { mutableListOf(0) }.asSequence().toList())
        TEST_LIST.pivotIterator { mutableListOf(0) }.testReverting(TEST_LIST)
        TEST_LIST.pivotIterator { mutableListOf(0) }.testPivoting()
    }

    @Test
    fun `pivoting string iterator`() {
        TEST_STRING.pivotIterator { mutableListOf(0) }.testReverting(TEST_STRING.toList())
        TEST_STRING.pivotIterator { mutableListOf(0) }.testPivoting()
    }
}