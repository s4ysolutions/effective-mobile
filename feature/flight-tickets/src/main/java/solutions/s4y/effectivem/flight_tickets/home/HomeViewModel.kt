package solutions.s4y.effectivem.flight_tickets.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import solutions.s4y.effectm.domain.TicketsService
import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val ticketsProvider: TicketsProvider) :
    ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment(Flights)"
    }
    val text: LiveData<String> = _text
    fun search() {
        Log.d("HomeViewModel", "search")
    }

    private val ticketsService = TicketsService.getSingleton(ticketsProvider)

    val offers = ticketsService.queryOffers().onStart { emit(emptyArray()) }.asLiveData()
}