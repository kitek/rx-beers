package pl.kitek.beers

interface ErrorReportingService {
    fun logException(e: Throwable)
}
