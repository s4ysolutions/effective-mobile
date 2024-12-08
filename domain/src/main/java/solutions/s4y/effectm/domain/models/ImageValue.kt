package solutions.s4y.effectm.domain.models

sealed class ImageValue {
    data object NoImage : ImageValue()
    data class ImageValueById(val id: Int) : ImageValue()
}