package pl.kitek.beers.usecase.common

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import pl.kitek.beers.usecase.common.executor.UseCaseExecutor

abstract class RxSingleUseCase<P, T>(
    private val executor: UseCaseExecutor,
    private val errorLogger: ErrorReportingService
) {

    var lastResult: T? = null
        private set

    var isRunning: Boolean = false
        private set

    private var requests = HashMap<P, Single<T>>()

    open fun doWork(param: P): Single<T> = throw UnsupportedOperationException()
    open fun doWork(): Single<T> = throw UnsupportedOperationException()

    fun execute(param: P, onSuccess: (t: T) -> Unit, onError: (err: Throwable) -> Unit): Disposable {
        return subscribe(doWork(param), onSuccess, onError)
    }

    fun execute(onSuccess: (t: T) -> Unit, onError: (err: Throwable) -> Unit): Disposable {
        return subscribe(doWork(), onSuccess, onError)
    }

    private fun subscribe(single: Single<T>, onSuccess: (t: T) -> Unit, onError: (err: Throwable) -> Unit): Disposable {
        return single
            .doOnSubscribe {
                lastResult = null
                isRunning = true
            }
            .doOnSuccess {
                lastResult = it
                isRunning = false
            }
            .doOnError {
                errorLogger.logException(it)
                isRunning = false
            }
            .compose(executor.singleTransformer())
            .subscribe(onSuccess, onError)
    }
}
