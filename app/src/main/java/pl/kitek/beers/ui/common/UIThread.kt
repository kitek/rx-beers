package pl.kitek.beers.ui.common

import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import pl.kitek.beers.usecase.executor.PostExecutionThread
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class UIThread @Inject constructor() : PostExecutionThread {

    override val rxScheduler: io.reactivex.Scheduler
        get() = mainThread()

}
