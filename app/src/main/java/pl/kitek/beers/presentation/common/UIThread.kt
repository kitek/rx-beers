package pl.kitek.beers.presentation.common

import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import pl.kitek.beers.usecase.common.executor.PostExecutionThread
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class UIThread @Inject constructor() : PostExecutionThread {

    override val rxScheduler: io.reactivex.Scheduler
        get() = mainThread()

}
