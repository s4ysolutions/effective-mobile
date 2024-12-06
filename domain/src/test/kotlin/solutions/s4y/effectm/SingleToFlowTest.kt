package solutions.s4y.effectm

import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx3.asFlow
import org.junit.Test

class SingleToFlowTest {
    @Test
    fun single_ShouldComplete() = runBlocking {
        // Arrange
        val single = Single.just(1)
        val flow = single.toObservable().asFlow()
        // Act
        var res: Int = 0
        val job = flow.collect{
            res = it
        }
        // Assert
        assert(res == 1)
    }
    @Test
    fun singleBuffered_ShouldComplete() = runBlocking {
        // Arrange
        val single = Single.just(1)
        val flow = single.toObservable().buffer(1).asFlow()
        // Act
        var res = emptyList<Int>()
        val job = flow.collect{
            res = it
        }
        // Assert
        assert(res.size == 1)
        assert(res[0] == 1)
    }
}