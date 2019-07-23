package pl.kitek.beers.presentation.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pl.kitek.beers.presentation.common.presenter.ViewModelFactory
import pl.kitek.beers.presentation.list.BeersFragment

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds @IntoMap @ViewModelKey(BeersFragment.PVM::class)
    abstract fun bindBeersViewModel(p: BeersFragment.PVM): ViewModel

    @Binds abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
