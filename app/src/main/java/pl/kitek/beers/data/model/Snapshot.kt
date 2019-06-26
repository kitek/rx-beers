package pl.kitek.beers.data.model

import java.util.*

data class Snapshot<T>(
    val data: T,
    val createdAt: Date = Date(),
    val isNewer: Boolean = false
)
