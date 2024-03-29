package pl.kitek.beers.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import pl.kitek.beers.data.model.Beer
import pl.kitek.beers.data.model.Page
import pl.kitek.beers.data.model.Snapshot
import pl.kitek.beers.data.repository.source.BeerLocalSource
import pl.kitek.beers.data.repository.source.BeerRemoteSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton class BeerRepository @Inject constructor(
    private val local: BeerLocalSource,
    private val remote: BeerRemoteSource
) {

    fun getBeers(endAt: Int): Observable<Snapshot<Page<Beer>>> {
        return local.getBeersSize().toObservable()
            .flatMap { currentSize ->
                val isResume = currentSize == endAt
                if (isResume) resume(endAt) else load(endAt)
            }
    }

    private fun resume(endAt: Int): Observable<Snapshot<Page<Beer>>> {
        return Observable.concat<Snapshot<Page<Beer>>>(
            getLocalBeers(endAt),

            Observable.zip(
                getLocalBeers(endAt),
                getRemoteBeers(offset = 0),
                PagesComparator()
            )
        )
            .distinct { snapshot -> snapshot.data }
            .scan { a: Snapshot<Page<Beer>>, b: Snapshot<Page<Beer>> ->
                b.copy(isNewer = a.createdAt < b.createdAt)
            }
    }


    private fun load(endAt: Int): Observable<Snapshot<Page<Beer>>> {
        val offset = endAt - LIMIT
        return Observable.concat(
            getLocalBeers(endAt),

            remote.getBeers(offset, LIMIT)
                .doOnSuccess { local.set(it) }
                .flatMapObservable { local.getBeers(endAt).toObservable() }

        )
            .distinct { snapshot -> snapshot.data }
            .scan { a: Snapshot<Page<Beer>>, b: Snapshot<Page<Beer>> ->
                b.copy(isNewer = a.createdAt < b.createdAt)
            }
    }

    fun invalidate(): Completable = local.invalidate()

    private fun getLocalBeers(endAt: Int) = local.getBeers(endAt).toObservable()

    private fun getRemoteBeers(offset: Int): Observable<Snapshot<Page<Beer>>>? {
        return remote.getBeers(offset, LIMIT)
            .doAfterSuccess { snapshot -> local.set(snapshot) }
            .toObservable()
    }

    private class PagesComparator : BiFunction<Snapshot<Page<Beer>>, Snapshot<Page<Beer>>, Snapshot<Page<Beer>>> {

        override fun apply(localPage: Snapshot<Page<Beer>>, remotePage: Snapshot<Page<Beer>>): Snapshot<Page<Beer>> {
            val toIndex = min(LIMIT, localPage.data.items.size)
            val firstLocalPageItems = localPage.data.items.subList(0, toIndex)

            val areItemsTheSame = firstLocalPageItems != remotePage.data.items
            return if (areItemsTheSame) remotePage else localPage
        }
    }

    companion object {
        const val LIMIT = 10
    }

}
