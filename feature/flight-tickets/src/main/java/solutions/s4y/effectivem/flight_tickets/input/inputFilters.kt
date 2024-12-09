package solutions.s4y.effectivem.flight_tickets.input

import android.text.InputFilter

private val cyrillicRegex = Regex("[0-9а-яА-Я\\s]*")
val cyrillicFilter = InputFilter { source, start, end, dest, dstart, dend ->
    if (source.toString().matches(cyrillicRegex)) source else ""
}
