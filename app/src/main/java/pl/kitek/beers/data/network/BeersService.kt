package pl.kitek.beers.data.network

import io.reactivex.Single
import pl.kitek.beers.data.model.Beer
import pl.kitek.beers.data.model.Page
import retrofit2.http.GET
import retrofit2.http.Path


interface BeersService {

    @GET("beers/{offset}/{limit}")
    fun getBeers(
        @Path("offset") offset: Int, @Path("limit") limit: Int
    ): Single<Page<Beer>>

}
