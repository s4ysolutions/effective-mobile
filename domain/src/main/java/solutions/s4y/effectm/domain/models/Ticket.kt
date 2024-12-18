package solutions.s4y.effectm.domain.models

import java.util.Date

data class Ticket(
    val id: ModelId,
    val badge: String?,
    val price: Money,
    val providerName: String?,
    val company: String?,
    val departure: Departure,
    val arrival: Arrival,
    val hasTransfer: Boolean,
    val hasVisaTransfer: Boolean,
    val luggage: Luggage,
    val handLuggage: HandLuggage,
    val isReturnable: Boolean,
    val isExchangable: Boolean
) {

    data class Departure(
        val town: String,
        val date: Date,
        val airport: String
    )

    data class Arrival(
        val town: String,
        val date: Date,
        val airport: String
    )

    data class Luggage(
        val hasLuggage: Boolean,
        val price: Money
    )

    data class HandLuggage(
        val hasHandLuggage: Boolean,
        val size: String?
    )

    companion object {
        val NoDeparture = Departure("", Date(0), "")
        val NoArrival = Arrival("", Date(0), "")
    }
}