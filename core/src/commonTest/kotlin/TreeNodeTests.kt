import io.github.aeckar.collections.linkedTreeOf
import kotlin.test.Test
import kotlin.test.assertEquals

class TreeNodeTests {
    private val tree = linkedTreeOf(4)

    init {
        tree += 0
        tree += linkedTreeOf(2)..1
        tree += 3
    }

    @Test
    fun correct_tree_string() {
        assertEquals("""
            4
            ├── 0
            ├── 2
            │   └── 1
            └── 3
        """.trimIndent(), tree.treeString())
    }

    @Test
    fun correct_iteration_order() {
        for ((index, node) in tree.withIndex()) {
            assertEquals(index, node.value)
        }
    }
}