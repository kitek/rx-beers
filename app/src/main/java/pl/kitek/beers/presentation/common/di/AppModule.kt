package pl.kitek.beers.presentation.common.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.kitek.beers.data.network.BeersService
import pl.kitek.beers.data.network.interceptor.ThrottleInterceptor
import pl.kitek.beers.presentation.BeersApp
import pl.kitek.beers.presentation.common.UIThread
import pl.kitek.beers.usecase.common.ErrorLogger
import pl.kitek.beers.usecase.common.ErrorReportingService
import pl.kitek.beers.usecase.common.executor.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton @Provides
    fun provideBeersService(): BeersService {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Timber.tag("beers").d(it) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ThrottleInterceptor())
            .build()

        val gson = GsonBuilder().create()

        return Retrofit.Builder()
            .baseUrl(BeersApp.API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(BeersService::class.java)
    }

    @Provides @Singleton
    fun provideUseCaseExecutor(executor: InteractorExecutor): UseCaseExecutor = executor

    @Provides @Singleton
    fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor = jobExecutor

    @Provides @Singleton
    fun providePostExecutionThread(uiThread: UIThread): PostExecutionThread = uiThread

    @Provides @Singleton fun provideErrorReportingService(errorLogger: ErrorLogger): ErrorReportingService = errorLogger

}
