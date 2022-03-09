import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest

fun main() = runTest {
    val nums = (1..3).asFlow().onEach {
        delay(10)
    }
    val strs = flowOf("one", "two", "three").onEach {
        delay(15)
    }

    val start = currentTime
    nums.zip(strs){a, b -> "$a -> $b"}
        .collect{println(it)}
    println("done : ${currentTime - start} ms")
}