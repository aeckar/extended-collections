import io.github.aeckar.collections.buildMultiSet
import io.github.aeckar.collections.mutableMultiSetOf
import kotlin.random.Random
import kotlin.test.Test

class MultiSetTests {
    @Test
    fun set_behavior() {
        val elements = listOf(0, 1L, 2.0, "three")
        val set = buildMultiSet { addAll(elements) }
        check(set.containsAll(elements))
    }

    @Test
    fun instance_counting_behavior() {
        val rand = Random(0)
        val elements = mutableListOf<Int>()
        val set = mutableMultiSetOf<Int>()
        repeat(rand.nextInt(until = 10)) {
            val element = rand.nextInt()
            repeat(rand.nextInt(until = 10)) {
                elements += element
                set += element
            }
        }
        for (element in elements.distinct()) {
            check(set.count(element) == elements.count { it == element })
        }
    }
}