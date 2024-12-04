package solutions.s4y.effectm.domain.models

data class Offer(
    val id: ModelId,
    val title: String,
    val town: String,
    val price: Money,
    val image: ImageProvider
)
