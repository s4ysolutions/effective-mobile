package solutions.s4y.effectm.provider

import org.junit.Test

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
fun toDatex(date: String?): Date {
    return if (date == null) {
        Date(0)
    } else {
        val localDateTime = LocalDateTime.parse(date, formatter)
        Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }
}
fun toDate(date: String?): Date {
    return if (date == null) {
        Date(0)
    } else {
        val localDateTime = LocalDateTime.parse(date, formatter)
        Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }
}

class DateFormatTest {
    @Test
    fun format() {
        val s = "2024-02-23T03:15:00"
        val d = toDate(s)
    }
}