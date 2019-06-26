package pl.kitek.beers

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class ErrorLogger @Inject constructor() : ErrorReportingService {

    override fun logException(e: Throwable) {
        if (BuildConfig.DEBUG) Timber.e(e, "Error occurred")
    }
}
