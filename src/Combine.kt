import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
@FlowPreview
private fun main() = runTest {
    fun delayedFlow(i: Int) = flow {
        val d = i * 1000L
        repeat(i) {
            delay(d)
            emit("$i: $it")
        }
    }

    val before = currentTime
    combine(delayedFlow(1), delayedFlow(2), delayedFlow(3)) { i, j, k ->
        println("combined $i, $j, $k")
        kotlinx.coroutines.flow.flowOf(i, j, k)
    }.flatMapConcat { combined ->
        flow {
            combined.collect {
                emit(it)
            }
        }
    }.collect {
        println(it)
    }
    println("total ${currentTime - before} ms")
}
