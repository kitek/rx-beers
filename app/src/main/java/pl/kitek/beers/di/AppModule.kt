package pl.kitek.beers.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.kitek.beers.ErrorLogger
import pl.kitek.beers.ErrorReportingService
import pl.kitek.beers.data.network.BeersService
import pl.kitek.beers.data.network.interceptor.ThrottleInterceptor
import pl.kitek.beers.ui.common.UIThread
import pl.kitek.beers.usecase.executor.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton @Provides
    fun provideBeersService(): BeersService {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Timber.tag("kitek").d(it) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ThrottleInterceptor())
            .build()

        val gson = GsonBuilder().create()

        return Retrofit.Builder()
            .baseUrl("http://10.168.20.122:3000/")
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
