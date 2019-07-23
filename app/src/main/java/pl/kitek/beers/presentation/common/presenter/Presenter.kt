package pl.kitek.beers.presentation.common.presenter

interface Presenter<in UI> {
    fun attach(ui: UI)
    fun detach()
    fun destroy()
}
