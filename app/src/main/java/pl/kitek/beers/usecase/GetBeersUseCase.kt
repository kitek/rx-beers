package pl.kitek.beers.usecase

import io.reactivex.Observable
import pl.kitek.beers.ErrorReportingService
import pl.kitek.beers.data.model.Beer
import pl.kitek.beers.data.model.Page
import pl.kitek.beers.data.model.Snapshot
import pl.kitek.beers.data.repository.BeerRepository
import pl.kitek.beers.usecase.GetBeersUseCase.Param
import pl.kitek.beers.usecase.executor.UseCaseExecutor
import pl.kitek.beers.usecase.usecase.RxObservableUseCase
import timber.log.Timber
import javax.inject.Inject

class GetBeersUseCase @Inject constructor(
    private val repo: BeerRepository,
    executor: UseCaseExecutor,
    errorLogger: ErrorReportingService
) : RxObservableUseCase<Param, Snapshot<Page<Beer>>>(executor, errorLogger) {

    val currentEndAt: Int
        get() {
            val currentSize = lastResult?.data?.items?.size ?: 0
            if (currentSize == 0) return BeerRepository.LIMIT
            return currentSize
        }

    val nextEndAt: Int
        get() {
            val currentSize = lastResult?.data?.items?.size ?: 0
            return currentSize + BeerRepository.LIMIT
        }

    override fun doWork(param: Param): Observable<Snapshot<Page<Beer>>> {
        Timber.tag("kitek").d("GetBeersUseCase.doWork: $param ")

        if (param.refresh) {
            this.lastResult = null
            return repo.invalidate()
                .andThen(Observable.defer { repo.getBeers(BeerRepository.LIMIT) })
        }

        return repo.getBeers(param.endAt)
    }


    data class Param(val refresh: Boolean, val endAt: Int = 0)

}
