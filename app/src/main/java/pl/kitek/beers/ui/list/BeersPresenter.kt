package pl.kitek.beers.ui.list

import pl.kitek.beers.data.model.Beer
import pl.kitek.beers.data.model.Page
import pl.kitek.beers.data.model.Snapshot
import pl.kitek.beers.presenter.BasePresenter
import pl.kitek.beers.ui.common.addTo
import pl.kitek.beers.ui.list.BeersPresenter.UI
import pl.kitek.beers.usecase.GetBeersUseCase
import timber.log.Timber
import javax.inject.Inject

class BeersPresenter @Inject constructor(
    private val getBeersUseCase: GetBeersUseCase
) : BasePresenter<UI>() {

    override fun attach(ui: UI) {
        super.attach(ui)
        loadBeers()
    }

    private fun loadBeers(refresh: Boolean = false) {
        Timber.tag("kitek").d("Load beers (refresh: $refresh)...")

        val param = GetBeersUseCase.Param(refresh, getBeersUseCase.currentEndAt)
        getBeersUseCase.execute(param, ::showBeers, ::showError).addTo(disposable)
    }

    fun refreshBeers() {
        loadBeers(true)
    }

    fun loadMore() {
        Timber.tag("kitek").d("Load more beers...")

        val param = GetBeersUseCase.Param(false, getBeersUseCase.nextEndAt)
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
        Timber.tag("kitek").d("** BeersPresenter.showBeers (size: ${snapshot.data.items.size}) ")
        ui().perform { ui ->
            if (snapshot.isNewer) ui.showSnackBar() else ui.showBeers(snapshot.data)
        }
    }

    private fun showError(err: Throwable) {
        Timber.tag("kitek").e("ERROR: $err ")
    }

    interface UI {
        fun showBeers(page: Page<Beer>)
        fun showSnackBar()
        fun scrollBeersToTop()
    }
}
