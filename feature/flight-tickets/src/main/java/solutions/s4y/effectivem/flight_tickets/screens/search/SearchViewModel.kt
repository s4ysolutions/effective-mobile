package solutions.s4y.effectivem.flight_tickets.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import solutions.s4y.effectivem.flight_tickets.state.FilterService
import solutions.s4y.effectm.domain.TicketsService
import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    ticketsProvider: TicketsProvider,
    private val filterService: FilterService
) :
    ViewModel() {
    val destCity = filterService.destCity
    val destCityLiveData = destCity.asLiveData()
    suspend fun setDestCity(value: String) {
        filterService.setDestCity(value)
    }

    val destCountry = filterService.destCountry
    val destCountryLiveData = destCountry.asLiveData()
    suspend fun setDestCountry(value: String) {
        filterService.setDestCountry(value)
    }

    private val ticketsService = TicketsService.getSingleton(ticketsProvider)

    val offers =
        ticketsService.queryTicketsOffers()
            .onStart { emit(emptyArray()) }
            .map { if (it.size > 3) it.slice(0..2).toTypedArray() else it }
            .asLiveData()
}