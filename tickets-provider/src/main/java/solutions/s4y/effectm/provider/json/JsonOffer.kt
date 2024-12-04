package solutions.s4y.effectm.provider.json

import solutions.s4y.effectm.domain.models.ImageProvider
import solutions.s4y.effectm.domain.models.Offer
import solutions.s4y.effectm.provider.RemoteEntityId

class JsonOffer(
    private val id: Int,
    private val title: String,
    private val town: String,
    private val price: JsonPrice,
    private val image: String?
) {
    val model get() = Offer(
        RemoteEntityId(id),
        title,
        town,
        price.money,
        image?.let {
            try {
                ImageProvider.ImageProviderById(id)
            } finally {
                ImageProvider.NoImage
            }
        } ?: ImageProvider.NoImage
    )
}