package pl.kitek.beers.data.model

data class Metadata(
    val total: Int = 0,
    val limit: Int = 0,
    val offset: Int = 0
) {

    fun hasMore(): Boolean = limit + offset < total

}
