package solutions.s4y.effectm.provider.json

import com.google.gson.annotations.SerializedName
import solutions.s4y.effectm.domain.models.TicketOffer
import solutions.s4y.effectm.provider.RemoteEntityId

class JsonTicketOffer(
    private val id: Int,
    private val title: String,
    @SerializedName("time_range")
    private val time_range: Array<String>,
    private val price: JsonPrice
) {
    val model: TicketOffer
        get() = TicketOffer(
            id = RemoteEntityId(id),
            title = title,
            timeRange = time_range,
            price = price.money
        )
}