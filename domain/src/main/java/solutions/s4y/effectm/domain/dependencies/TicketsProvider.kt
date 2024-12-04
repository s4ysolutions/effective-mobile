package solutions.s4y.effectm.domain.dependencies

import io.reactivex.rxjava3.core.Single
import solutions.s4y.effectm.domain.models.Offer
import solutions.s4y.effectm.domain.models.Ticket
import solutions.s4y.effectm.domain.models.TicketOffer

interface TicketsProvider {
    fun queryOffers(): Single<Array<Offer>>
    fun queryTickets(): Single<Array<Ticket>>
    fun queryTicketsOffers(): Single<Array<TicketOffer>>
}