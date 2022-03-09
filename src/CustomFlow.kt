import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    launch {
        myFlowOf("First", "Second")
            .map { "$it modified 1st" }
            .map { "$it modified 2nd" }
            .collect(object : MyCollector<String> {
                override fun emit(value: String) {
                    println(value)
                }
            })
    }
}

private interface MyCollector<in T> {
    fun emit(value: T)
}

private interface MyFlow<out T> {
    fun collect(collector: MyCollector<T>)
}

private fun <T> myFlowOf(vararg value: T) = object : MyFlow<T> {
    override fun collect(collector: MyCollector<T>) {
        for (v in value) {
            collector.emit(v)
        }
    }
}

private inline fun <T, R> MyFlow<T>.map(crossinline transform: (value: T) -> R): MyFlow<R> {
    val upstream = this
    return object : MyFlow<R> {
        override fun collect(collector: MyCollector<R>) {
            upstream.collect(object : MyCollector<T> {
                override fun emit(value: T) {
                    val newValue = transform(value)
                    collector.emit(newValue)
                }
            })
        }
    }
}
