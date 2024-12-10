package solutions.s4y.effectivem.flight_tickets.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.last
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
    val destCity = filterService.destCity
    val destCityLiveData = destCity.distinctUntilChanged().conflate().asLiveData()
    suspend fun setDestCity(value: String) {
        filterService.setDestCity(value)
    }

    val destCountry = filterService.destCountry
    val destCountryLiveData = destCountry.distinctUntilChanged().conflate().asLiveData()
    suspend fun setDestCountry(value: String) {
        filterService.setDestCountry(value)
    }

    private val ticketsService = TicketsService.getSingleton(ticketsProvider)

    val offersLiveData = ticketsService.queryOffers().onStart { emit(emptyArray()) }.asLiveData()
}