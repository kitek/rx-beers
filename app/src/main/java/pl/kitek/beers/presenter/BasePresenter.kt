package pl.kitek.beers.presenter

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<UI> : Presenter<UI> {

    protected val disposable = CompositeDisposable()
    private val uiExecutor: UICommandExecutor<UI> = UICommandExecutor()
    private var attachedBefore = false

    protected open fun onFirstAttachment() {
    }

    override fun attach(ui: UI) {
        uiExecutor.attach(ui)
        if (!attachedBefore) {
            onFirstAttachment()
            attachedBefore = true
        }
    }

    override fun detach() {
        uiExecutor.detach()
    }

    override fun destroy() {
        disposable.clear()
    }

    protected fun ui(): SafeUI<UI> = uiExecutor
}
