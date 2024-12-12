package solutions.s4y.effectivem.flight_tickets.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import solutions.s4y.effectm.domain.TicketsService
import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    ticketsProvider: TicketsProvider,
) :
    ViewModel() {
    private val ticketsService = TicketsService.getSingleton(ticketsProvider)

    val offers =
        ticketsService.queryTicketsOffers()
            .onStart { emit(emptyArray()) }
            .map { if (it.size > 3) it.slice(0..2).toTypedArray() else it }
            .asLiveData()
}