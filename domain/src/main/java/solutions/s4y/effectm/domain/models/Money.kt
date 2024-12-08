package solutions.s4y.effectm.domain.models

class Money(val i: Int, val f: Int = 0, val c: String = "RUB")

private fun format(i: Int, prev: String): String {
    val r1 = i % 1000
    val r2 = i / 1000
    return if (r2 == 0) {
        "$r1$prev"
    } else {
        format(r2, String.format(" %03d$prev", r1))
    }
}

val Money.formatted: String
    @Suppress("DefaultLocale")
    get() =
        "${format(this.i, "")} ₽"