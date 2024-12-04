package solutions.s4y.effectm.provider.json

import solutions.s4y.effectm.domain.models.Money

class JsonPrice(
    private val value: Int,
){
    val money: Money get() = Money(value)
}