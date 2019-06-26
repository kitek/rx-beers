package pl.kitek.beers.data.model

data class Page<out T>(val metadata: Metadata, val items: List<T>)
