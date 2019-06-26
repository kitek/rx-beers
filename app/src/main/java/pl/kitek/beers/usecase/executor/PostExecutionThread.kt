package pl.kitek.beers.usecase.executor

interface PostExecutionThread {
    val rxScheduler: io.reactivex.Scheduler
}
