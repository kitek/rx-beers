package pl.kitek.beers.presentation.common.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.kitek.beers.presentation.list.BeersFragment


@Module @Suppress("unused")
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector abstract fun contributeBeersFragment(): BeersFragment


}
