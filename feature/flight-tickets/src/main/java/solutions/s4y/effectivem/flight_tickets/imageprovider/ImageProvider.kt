package solutions.s4y.effectivem.flight_tickets.imageprovider

object ImageProvider {
    fun getImageUrl(imageId: Int): String {
        return "https://picsum.photos/id/$imageId/200/300"
    }
}