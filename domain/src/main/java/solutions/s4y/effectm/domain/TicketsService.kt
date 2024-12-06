package solutions.s4y.effectm.domain

import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import kotlinx.coroutines.rx3.asFlow

// possible caching and exposing as Flow
class TicketsService(private val ticketsProvider: TicketsProvider) {
    fun queryOffers() = ticketsProvider.queryOffers().toObservable().asFlow()
    fun queryTickets() = ticketsProvider.queryTickets().toObservable().asFlow()
    fun queryTicketsOffers() = ticketsProvider.queryTicketsOffers().toObservable().asFlow()

    companion object {
        // java module can't use Hilt, nust be integrated with Dagger,
        // currently manually
        private var instance: TicketsService? = null
        fun getSingleton(ticketsProvider: TicketsProvider):TicketsService = instance ?: run {
            instance = TicketsService(ticketsProvider)
            getSingleton(ticketsProvider)
        }
    }
}