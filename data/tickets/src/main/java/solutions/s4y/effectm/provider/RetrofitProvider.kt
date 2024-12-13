package solutions.s4y.effectm.provider

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import solutions.s4y.effectm.domain.dependencies.TicketsProvider
import solutions.s4y.effectm.domain.models.Offer
import solutions.s4y.effectm.domain.models.Ticket
import solutions.s4y.effectm.domain.models.TicketOffer
import javax.inject.Inject

class RetrofitProvider @Inject constructor(@ApplicationContext context: Context) : TicketsProvider {
    private val retrofitClient = RetrofitClient.getInstance(context)

    override fun queryOffers(): Single<Array<Offer>> =
        retrofitClient.getOffers().subscribeOn(Schedulers.io()).map { jsonOffers ->
            Array(jsonOffers.offers.size) { index ->
                jsonOffers.offers[index].model
            }
        }.delay(DELAY, java.util.concurrent.TimeUnit.MILLISECONDS)

    override fun queryTickets(): Single<Array<Ticket>> =
        retrofitClient.getTickets().subscribeOn(Schedulers.io()).map { jsonTickets ->
            Array(jsonTickets.tickets.size) { index ->
                jsonTickets.tickets[index].model
            }
        }.delay(DELAY, java.util.concurrent.TimeUnit.MILLISECONDS)

    override fun queryTicketsOffers(): Single<Array<TicketOffer>> =
        retrofitClient.getTicketsOffers().subscribeOn(Schedulers.io()).map { jsonTicketsOffers ->
            Array(jsonTicketsOffers.ticketsOffers.size) { index ->
                jsonTicketsOffers.ticketsOffers[index].model
            }
        }.delay(DELAY, java.util.concurrent.TimeUnit.MILLISECONDS)

    companion object {
        private val DELAY get() = 500L + (System.currentTimeMillis() % 10) * 50L
    }
}