package pl.kitek.beers.presentation.common.presenter

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<P : Presenter<*>> constructor(
    open val presenter: P
) : ViewModel() {

    override fun onCleared() {
        presenter.destroy()
        super.onCleared()
    }
}
