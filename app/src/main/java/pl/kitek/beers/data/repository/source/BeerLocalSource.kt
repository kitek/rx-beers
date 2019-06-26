package pl.kitek.beers.data.repository.source

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import pl.kitek.beers.data.model.Beer
import pl.kitek.beers.data.model.Metadata
import pl.kitek.beers.data.model.Page
import pl.kitek.beers.data.model.Snapshot
import pl.kitek.beers.data.repository.BeerRepository
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.ceil

@SuppressLint("BinaryOperationInTimber")
class BeerLocalSource @Inject constructor() {

    @SuppressLint("UseSparseArrays")
    private var cache = HashMap<Int, Snapshot<Page<Beer>>>()

    fun getBeersSize(): Single<Int> = Single.just(
        (0 until cache.size)
            .fold(0, { acc, index -> acc + (cache[index]?.data?.items?.size ?: 0) })
    )

    fun getBeers(endAt: Int): Maybe<Snapshot<Page<Beer>>> {
//        val endAtPage = (endAt - BeerRepository.LIMIT) / BeerRepository.LIMIT
        var endAtPage = ceil(endAt / BeerRepository.LIMIT.toFloat()).toInt()
        if (endAtPage > 0) endAtPage -= 1 else 0
        Timber.tag("kitek").d("endAtPage: $endAtPage ")


        val maxEndAtPage = if (cache.size > 0) cache.size - 1 else 0
        if (endAtPage > maxEndAtPage) {
            Timber.tag("kitek").d(
                "BeerLocalSource.getBeers(endAt: $endAt, endAtPage: $endAtPage) " +
                        "| skipped not enough items (current: ${cache.size}) required: $endAtPage"
            )
            return Maybe.empty()
        }

        val items = ArrayList<Beer>()
        var lastCreatedAt: Date? = null
        var lastMetadata: Metadata? = null

        (0..endAtPage).forEach { index ->
            Timber.tag("kitek").d("cache.index: $index -> size:${cache[index]?.data?.items?.size}")
            cache[index]?.let { snapshot ->
                lastCreatedAt = snapshot.createdAt
                lastMetadata = snapshot.data.metadata
                items.addAll(snapshot.data.items)
            }
        }

        Timber.tag("kitek")
            .d(
                "BeerLocalSource.getBeers(endAt: $endAt, endAtPage: $endAtPage) " +
                        "| pages: ${cache.size} | items: ${items.size} | lastCreatedAt: $lastCreatedAt " +
                        "| lastMetadata: $lastMetadata"
            )


        if (null == lastMetadata) return Maybe.empty()
        if (null == lastCreatedAt) return Maybe.empty()

        val page = Page(lastMetadata!!, items)
        val snapshot = Snapshot(page, createdAt = lastCreatedAt!!)

        return Maybe.just(snapshot)
    }

    fun set(snapshot: Snapshot<Page<Beer>>) {
        val page = snapshot.data.metadata.offset / BeerRepository.LIMIT
        cache[page] = snapshot

        Timber.tag("kitek").d("BeerLocalSource.set(page: $page) | cache.size: ${cache.size}")
    }

    fun invalidate(): Completable = Completable.fromAction {
        cache.clear()
    }
}
