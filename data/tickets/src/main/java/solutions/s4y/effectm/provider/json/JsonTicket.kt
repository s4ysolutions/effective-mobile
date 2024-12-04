package solutions.s4y.effectm.provider.json

import com.google.gson.annotations.SerializedName
import solutions.s4y.effectm.domain.models.Money
import solutions.s4y.effectm.domain.models.Ticket
import solutions.s4y.effectm.provider.RemoteEntityId

class JsonTicket(
    private val id: Int = 0,
    private val badge: String? = null,
    private val price: JsonPrice? = null,
    @SerializedName("provider_name")
    private val providerName: String? = null,
    private val company: String? = null,
    private val departure: JsonDeparture? = null,
    private val arrival: JsonArrival? = null,
    @SerializedName("has_transfer")
    private val hasTransfer: Boolean = false,
    @SerializedName("has_visa_transfer")
    private val hasVisaTransfer: Boolean = false,
    private val luggage: JsonLuggage? = null,
    @SerializedName("hand_luggage")
    private val handLuggage: JsonHandLuggage? = null,
    @SerializedName("is_returnable")
    private val isReturnable: Boolean = false,
    @SerializedName("is_exchangable")
    private val isExchangable: Boolean = false,
) {
    val model: Ticket
        get() = Ticket(
            id = RemoteEntityId(id),
            badge = badge,
            price = price?.money ?: Money(0),
            providerName = providerName,
            company = company,
            departure = departure?.model ?: Ticket.NoDeparture,
            arrival = arrival?.model ?: Ticket.NoArrival,
            hasTransfer = hasTransfer,
            hasVisaTransfer = hasVisaTransfer,
            luggage = luggage?.model ?: Ticket.Luggage(false, Money(0)),
            handLuggage = handLuggage?.model ?: Ticket.HandLuggage(false, ""),
            isReturnable = isReturnable,
            isExchangable = isExchangable,
        )
}

class JsonDeparture(
    val town: String?,
    val date: String?,
    val airport: String?,
) {
    val model
        get() = Ticket.Departure(
            town = town ?: "",
            date = date ?: "",
            airport = airport ?: "UNK"
        )
}

class JsonArrival(
    val town: String?,
    val date: String?,
    val airport: String?,
) {
    val model
        get() = Ticket.Arrival(
            town = town ?: "",
            date = date ?: "",
            airport = airport ?: "UNK"
        )
}

class JsonLuggage(
    val has_luggage: Boolean,
    val price: JsonPrice?
) {
    val model get() = Ticket.Luggage(hasLuggage = has_luggage, price = price?.money ?: Money(0))
}

class JsonHandLuggage(
    val has_hand_luggage: Boolean,
    val size: String?,
) {
    val model get() = Ticket.HandLuggage(hasHandLuggage = has_hand_luggage, size = size ?: "")
}