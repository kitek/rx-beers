package pl.kitek.beers.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.kitek.beers.ui.list.BeersFragment


@Module @Suppress("unused")
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector abstract fun contributeBeersFragment(): BeersFragment


}
