package pl.kitek.beers.presentation.common.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import pl.kitek.beers.presentation.common.di.Injectable
import javax.inject.Inject

abstract class BaseFragment<P : Presenter<V>, in V, VM : BaseViewModel<P>> : Fragment(), Injectable {

    @Inject
    open lateinit var vmFactory: ViewModelProvider.Factory

    abstract val vmClass: Class<VM>

    protected var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = ViewModelProviders.of(this, vmFactory).get(vmClass)
        presenter = vm.presenter
    }

    override fun onResume() {
        super.onResume()
        
        @Suppress("UNCHECKED_CAST")
        presenter?.attach(this as V)
    }

    override fun onPause() {
        presenter?.detach()
        super.onPause()
    }
}
