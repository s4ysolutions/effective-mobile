package solutions.s4y.effectivem.flight_tickets.screens.tickets

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import solutions.s4y.effectivem.flight_tickets.views.PersistedState
import solutions.s4y.effectm.domain.TicketsService
import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.zip
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class AllTicketsViewModel @Inject constructor(
    ticketsProvider: TicketsProvider,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val ticketsService = TicketsService.getSingleton(ticketsProvider)
    val ticketsLiveData = ticketsService.queryTickets().onStart { emit(emptyArray()) }.asLiveData()

    val route =
        PersistedState.CityDeparture.asStateFlow(
            context,
            viewModelScope
        ).zip(
            PersistedState.CityDestination.asStateFlow(
                context,
                viewModelScope
            )
        ) { departure, destination ->
            "$departure - $destination"
        }.asLiveData()

    val filter = PersistedState.DepartureDate.asStateFlow(context, viewModelScope).map {
        "${toDayMonth(it)}, 1 пассажир"
    }.asLiveData()

    companion object {
        private val TAG = AllTicketsViewModel::class.java.simpleName
        private val formatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
        fun toDayMonth(date: Long): String {
            return formatter.format(Date(date))
        }
    }
}