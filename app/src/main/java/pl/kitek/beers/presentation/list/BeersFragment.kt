package pl.kitek.beers.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.beers_fragment.*
import pl.kitek.beers.R
import pl.kitek.beers.data.model.Beer
import pl.kitek.beers.data.model.Page
import pl.kitek.beers.presentation.common.presenter.BaseFragment
import pl.kitek.beers.presentation.common.presenter.BaseViewModel
import pl.kitek.beers.presentation.list.BeersFragment.PVM
import pl.kitek.beers.presentation.list.BeersPresenter.UI
import pl.kitek.beers.presentation.list.adapter.BeersAdapter
import javax.inject.Inject

class BeersFragment : BaseFragment<BeersPresenter, UI, PVM>(), UI,
    SwipeRefreshLayout.OnRefreshListener, BeersAdapter.ItemClickListener {

    private val beersAdapter = BeersAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.beers_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beersRv.layoutManager = LinearLayoutManager(context)
        beersRv.adapter = beersAdapter

        beersRefresh.setOnRefreshListener(this)
    }

    override fun showBeers(page: Page<Beer>) {
        beersAdapter.setItems(page)
        beersRefresh.isRefreshing = false
    }

    override fun scrollBeersToTop() {
        beersRv.smoothScrollToPosition(0)
    }

    override fun showSnackBar() {
        view?.let { v ->
            Snackbar.make(v, "Something new!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Update") { presenter?.showNewerBeers() }
                .show()
        }
    }

    override fun onRefresh() {
        presenter?.refreshBeers()
    }

    override fun showError(err: Throwable) {
        val message = err.message ?: "Error occurred"
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onBeerClick(beer: Beer) {
    }

    override fun onLoadMoreClick() {
        presenter?.loadMore()
    }

    companion object {
        fun newInstance() = BeersFragment()
    }

    override val vmClass = PVM::class.java

    class PVM @Inject constructor(
        override val presenter: BeersPresenter
    ) : BaseViewModel<BeersPresenter>(presenter)
}
