package pl.kitek.beers.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.kitek.beers.MainActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
