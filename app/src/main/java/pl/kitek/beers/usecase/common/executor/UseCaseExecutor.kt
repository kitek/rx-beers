package pl.kitek.beers.usecase.common.executor

import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer

interface UseCaseExecutor {

    fun <R> rxTransformer(): ObservableTransformer<R, R>
    fun <R> singleTransformer(): SingleTransformer<R, R>

}