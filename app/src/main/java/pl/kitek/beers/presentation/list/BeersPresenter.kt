package pl.kitek.beers.presentation.list

import pl.kitek.beers.data.model.Beer
import pl.kitek.beers.data.model.Page
import pl.kitek.beers.data.model.Snapshot
import pl.kitek.beers.presentation.common.addTo
import pl.kitek.beers.presentation.common.presenter.BasePresenter
import pl.kitek.beers.presentation.list.BeersPresenter.UI
import pl.kitek.beers.usecase.GetBeersUseCase
import pl.kitek.beers.usecase.GetBeersUseCase.Param
import javax.inject.Inject

class BeersPresenter @Inject constructor(
    private val getBeersUseCase: GetBeersUseCase
) : BasePresenter<UI>() {

    override fun attach(ui: UI) {
        super.attach(ui)
        loadBeers()
    }

    private fun loadBeers(refresh: Boolean = false) {
        val param = Param(refresh, getBeersUseCase.currentEndAt)
        getBeersUseCase.execute(param, ::showBeers, ::showError).addTo(disposable)
    }

    fun refreshBeers() {
        loadBeers(true)
    }

    fun loadMore() {
        val param = Param(false, getBeersUseCase.nextEndAt)
        getBeersUseCase.execute(param, ::showBeers, ::showError).addTo(disposable)
    }

    fun showNewerBeers() {
        getBeersUseCase.lastResult?.let { pageResult ->
            ui().perform { ui ->
                ui.showBeers(pageResult.data)
                ui.scrollBeersToTop()
            }
        }
    }

    private fun showBeers(snapshot: Snapshot<Page<Beer>>) {
        ui().perform { ui ->
            if (snapshot.isNewer) ui.showSnackBar() else ui.showBeers(snapshot.data)
        }
    }

    private fun showError(err: Throwable) {
        ui().perform { ui -> ui.showError(err) }
    }

    interface UI {
        fun showBeers(page: Page<Beer>)
        fun showError(err: Throwable)
        fun showSnackBar()
        fun scrollBeersToTop()
    }
}
