package pl.kitek.beers.presenter

interface Presenter<in UI> {
    fun attach(ui: UI)
    fun detach()
    fun destroy()
}
