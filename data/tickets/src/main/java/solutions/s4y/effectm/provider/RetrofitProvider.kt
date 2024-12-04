package solutions.s4y.effectm.provider

import android.content.Context
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import solutions.s4y.effectm.domain.models.Offer
import solutions.s4y.effectm.domain.models.Ticket
import solutions.s4y.effectm.domain.models.TicketOffer


class RetrofitProvider(context: Context) : TicketsProvider {
    private val remoteService = RemoteService.getInstance(context)

    override fun queryOffers(): Single<Array<Offer>> =
        remoteService.getOffers().subscribeOn(Schedulers.io()).map { jsonOffers ->
            Array(jsonOffers.offers.size) { index ->
                jsonOffers.offers[index].model
            }
        }

    override fun queryTickets(): Single<Array<Ticket>> =
        remoteService.getTickets().subscribeOn(Schedulers.io()).map { jsonTickets ->
            Array(jsonTickets.tickets.size) { index ->
                jsonTickets.tickets[index].model
            }
        }

    override fun queryTicketsOffers(): Single<Array<TicketOffer>> =
        remoteService.getTicketsOffers().subscribeOn(Schedulers.io()).map { jsonTicketsOffers ->
            Array(jsonTicketsOffers.ticketsOffers.size) { index ->
                jsonTicketsOffers.ticketsOffers[index].model
            }
        }
}