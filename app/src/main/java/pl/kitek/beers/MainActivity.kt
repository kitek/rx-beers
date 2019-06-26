package pl.kitek.beers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import pl.kitek.beers.ui.list.BeersFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (null == savedInstanceState) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, BeersFragment.newInstance())
                .commit()
        }



    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
