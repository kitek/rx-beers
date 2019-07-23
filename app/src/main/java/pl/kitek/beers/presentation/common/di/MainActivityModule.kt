package pl.kitek.beers.presentation.common.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.kitek.beers.presentation.MainActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
