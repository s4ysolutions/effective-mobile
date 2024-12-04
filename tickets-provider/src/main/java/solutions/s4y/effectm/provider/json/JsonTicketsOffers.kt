package solutions.s4y.effectm.provider.json

import com.google.gson.annotations.SerializedName

class JsonTicketsOffers(
    @SerializedName("tickets_offers")
    val ticketsOffers: Array<JsonTicketOffer>,
)