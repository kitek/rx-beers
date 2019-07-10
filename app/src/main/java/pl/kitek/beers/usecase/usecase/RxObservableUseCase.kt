package pl.kitek.beers.usecase.usecase

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import pl.kitek.beers.ErrorReportingService
import pl.kitek.beers.usecase.executor.UseCaseExecutor

abstract class RxObservableUseCase<P, T>(
    private val executor: UseCaseExecutor,
    private val errorLogger: ErrorReportingService
) {

    var lastResult: T? = null
        protected set

    private var request: Observable<T>? = null

    open fun doWork(param: P): Observable<T> = throw UnsupportedOperationException()
    open fun doWork(): Observable<T> = throw UnsupportedOperationException()

    fun execute(param: P, onNext: (t: T) -> Unit, onError: (err: Throwable) -> Unit): Disposable {
        return subscribe(doWork(param), onNext, onError)
    }

    fun execute(onNext: (t: T) -> Unit, onError: (err: Throwable) -> Unit): Disposable {
        return subscribe(doWork(), onNext, onError)
    }

    protected fun clearLastResult() {
        this.lastResult = null
    }

    private fun subscribe(
        doWork: Observable<T>,
        onNext: (t: T) -> Unit,
        onError: (err: Throwable) -> Unit
    ): Disposable {
        if (null == request) request = createRequest(doWork)

        return request!!.subscribe(onNext, onError)
    }

    private fun createRequest(observable: Observable<T>): Observable<T> {
        return observable
            .doOnNext { lastResult = it }
            .doFinally { request = null }
            .doOnError { errorLogger.logException(it) }
            .compose(executor.rxTransformer())
            .share()
            .replay()
            .autoConnect(1)
    }
}
