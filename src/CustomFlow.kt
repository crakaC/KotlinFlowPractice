import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    launch {
        flowOf("First", "Second")
            .map { "$it modified 1st" }
            .map { "$it modified 2nd" }
            .collect(object : Collector<String> {
                override fun emit(value: String) {
                    println(value)
                }
            })
    }
}

private interface Collector<in T> {
    fun emit(value: T)
}

private interface Flow<out T> {
    fun collect(collector: Collector<T>)
}

private fun <T> flowOf(vararg value: T) = object : Flow<T> {
    override fun collect(collector: Collector<T>) {
        for (v in value) {
            collector.emit(v)
        }
    }
}

private inline fun <T, R> Flow<T>.map(crossinline transform: (value: T) -> R): Flow<R> {
    val upstream = this
    return object : Flow<R> {
        override fun collect(collector: Collector<R>) {
            upstream.collect(object : Collector<T> {
                override fun emit(value: T) {
                    val newValue = transform(value)
                    collector.emit(newValue)
                }
            })
        }
    }
}
