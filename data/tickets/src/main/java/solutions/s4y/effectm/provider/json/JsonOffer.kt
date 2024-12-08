package solutions.s4y.effectm.provider.json

import solutions.s4y.effectm.domain.models.ImageValue
import solutions.s4y.effectm.domain.models.Offer
import solutions.s4y.effectm.provider.RemoteEntityId

class JsonOffer(
    private val id: Int,
    private val title: String,
    private val town: String,
    private val price: JsonPrice,
    private val image: String?
) {
    val model
        get() = Offer(
            RemoteEntityId(id),
            title,
            town,
            price.money,
            image?.let {
                try {
                    // assume it would be a URL
                    ImageValue.ImageValueById(it.toInt())
                } finally {
                    ImageValue.NoImage
                }
            } ?: ImageValue.ImageValueById(id)
        )
}