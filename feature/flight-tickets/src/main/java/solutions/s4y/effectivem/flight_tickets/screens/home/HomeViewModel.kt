package solutions.s4y.effectivem.flight_tickets.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.runBlocking
import solutions.s4y.effectivem.flight_tickets.views.PersistedState
import solutions.s4y.effectm.domain.TicketsService
import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class HomeViewModel @Inject constructor(
    ticketsProvider: TicketsProvider,
    singletonClassForCleanup: SingletonClassForCleanup
) : ViewModel() {
    private val ticketsService = TicketsService.getSingleton(ticketsProvider)
    val offersLiveData = ticketsService.queryOffers().onStart { emit(emptyArray()) }.asLiveData()
}

@Singleton
class SingletonClassForCleanup @Inject constructor(@ApplicationContext context: Context) {
    init {
        runBlocking() {
            PersistedState.CityDeparture.set(context, "Минск")
            PersistedState.CityDestination.set(context, "")
            PersistedState.DepartureDate.unset(context)
            PersistedState.ReturnDate.unset(context)
        }
    }
}