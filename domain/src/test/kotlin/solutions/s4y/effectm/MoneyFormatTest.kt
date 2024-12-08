package solutions.s4y.effectm

import org.junit.Assert.assertEquals
import org.junit.Test
import solutions.s4y.effectm.domain.models.Money
import solutions.s4y.effectm.domain.models.formatted
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class MoneyFormatTest(private val input: Int, private val expected: String) {

    @Test
    fun testFormat() {
        val money = Money(input)
        assertEquals(expected, money.formatted)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{index}: Money({0}) = {1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(0, "0 ₽"),
                arrayOf(1, "1 ₽"),
                arrayOf(999, "999 ₽"),
                arrayOf(1001, "1 001 ₽"),
                arrayOf(100001, "100 001 ₽"),
                arrayOf(100000001, "100 000 001 ₽")
            )
        }
    }
}