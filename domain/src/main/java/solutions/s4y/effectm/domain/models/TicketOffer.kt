package solutions.s4y.effectm.domain.models

import java.util.concurrent.TimeUnit

data class TicketOffer (
    val id: ModelId,
    val title: String,
    val timeRange: Array<String>,
    val price: Money)