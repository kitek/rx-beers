package pl.kitek.beers.usecase.common.executor

interface PostExecutionThread {
    val rxScheduler: io.reactivex.Scheduler
}
