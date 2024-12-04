package solutions.s4y.effectm.domain.models

sealed class ImageProvider {
    object NoImage : ImageProvider()
    // data class ImageProviderUrl(val url: URL) : ImageProvider()
    data class ImageProviderById(val id: Int) : ImageProvider()
}

