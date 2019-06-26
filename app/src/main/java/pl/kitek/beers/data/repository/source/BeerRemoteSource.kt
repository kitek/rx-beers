package pl.kitek.beers.data.repository.source

import io.reactivex.Single
import pl.kitek.beers.data.model.Beer
import pl.kitek.beers.data.model.Page
import pl.kitek.beers.data.model.Snapshot
import pl.kitek.beers.data.network.BeersService
import javax.inject.Inject

class BeerRemoteSource @Inject constructor(
    private val remoteService: BeersService
) {

    fun getBeers(offset: Int, limit: Int): Single<Snapshot<Page<Beer>>> {
        return remoteService.getBeers(offset, limit)
            .map { page -> Snapshot(page) }
    }
}
