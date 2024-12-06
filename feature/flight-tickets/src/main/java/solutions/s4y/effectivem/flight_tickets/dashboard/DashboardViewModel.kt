package solutions.s4y.effectivem.flight_tickets.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.onStart
import solutions.s4y.effectm.domain.TicketsService
import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import javax.inject.Inject

class DashboardViewModel @Inject constructor(private val ticketsProvider: TicketsProvider) :
    ViewModel() {

    private val ticketsService = TicketsService.getSingleton(ticketsProvider)

    val offers = ticketsService.queryOffers().onStart{ emit(emptyArray()) }.asLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}