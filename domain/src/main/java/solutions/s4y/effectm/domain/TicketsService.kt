package solutions.s4y.effectm.domain

import solutions.s4y.effectm.domain.dependencies.TicketsProvider

class TicketsService(
    private val ticketsProvider: TicketsProvider
) {
    fun queryOffers() = ticketsProvider.queryOffers()
    fun queryTickets() = ticketsProvider.queryTickets()
    fun queryTicketsOffers() = ticketsProvider.queryTicketsOffers()
}