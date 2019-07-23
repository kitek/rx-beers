package pl.kitek.beers.presentation

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import pl.kitek.beers.BuildConfig
import pl.kitek.beers.presentation.common.di.AppInjector
import timber.log.Timber
import javax.inject.Inject

class BeersApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun activityInjector() = dispatchingAndroidInjector

    companion object {
        const val API_URL = "http://10.0.2.2:3000/"
    }
}
