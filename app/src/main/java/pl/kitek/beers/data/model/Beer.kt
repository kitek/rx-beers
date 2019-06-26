package pl.kitek.beers.data.model

data class Beer(
    val id: String,
    val name: String,
    val tagline: String,
    val description: String,
    val imageUrl: String,
    val abv: Int,
    val createdAt: Long
)
