package pl.kitek.beers.usecase.common.executor

import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class InteractorExecutor @Inject constructor(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) : UseCaseExecutor {

    override fun <R> rxTransformer(): ObservableTransformer<R, R> {
        return ObservableTransformer { t ->
            t.subscribeOn(from(threadExecutor)).observeOn(postExecutionThread.rxScheduler)
        }
    }

    override fun <R> singleTransformer(): SingleTransformer<R, R> {
        return SingleTransformer { t ->
            t.subscribeOn(from(threadExecutor)).observeOn(postExecutionThread.rxScheduler)
        }
    }
}
