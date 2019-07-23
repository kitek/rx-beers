package pl.kitek.beers.usecase.common

interface ErrorReportingService {
    fun logException(e: Throwable)
}
