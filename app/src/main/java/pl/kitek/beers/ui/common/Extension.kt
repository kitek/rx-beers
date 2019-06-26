package pl.kitek.beers.ui.common

import android.widget.ImageView
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun ImageView.load(url: String) {
    Picasso.with(context).load(url).into(this)
}

fun Disposable.addTo(disposable: CompositeDisposable) {
    disposable.add(this)
}
