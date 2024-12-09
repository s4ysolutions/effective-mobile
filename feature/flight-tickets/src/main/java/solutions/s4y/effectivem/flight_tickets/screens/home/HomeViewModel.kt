package solutions.s4y.effectivem.flight_tickets.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import solutions.s4y.effectivem.flight_tickets.state.FilterService
import solutions.s4y.effectm.domain.TicketsService
import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ticketsProvider: TicketsProvider,
    private val filterService: FilterService
) :
    ViewModel() {
    val destCityLiveData = filterService.destCity.asLiveData()
    suspend fun setDestCity(value: String) {
        filterService.setDestCity(value)
    }

    val destCountryLiveData = filterService.destCountry.asLiveData()
    suspend fun setDestCountry(value: String) {
        filterService.setDestCountry(value)
    }

    private val ticketsService = TicketsService.getSingleton(ticketsProvider)

    val offersLiveData = ticketsService.queryOffers().onStart { emit(emptyArray()) }.asLiveData()
}