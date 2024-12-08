package solutions.s4y.effectm.domain.models

data class TicketOffer (
    val id: ModelId,
    val title: String,
    val timeRange: Array<String>,
    val price: Money) {
    // need to override equals and hashCode because of tht timeRange field is an array
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TicketOffer

        if (id != other.id) return false
        if (title != other.title) return false
        if (!timeRange.contentEquals(other.timeRange)) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + timeRange.contentHashCode()
        result = 31 * result + price.hashCode()
        return result
    }
}