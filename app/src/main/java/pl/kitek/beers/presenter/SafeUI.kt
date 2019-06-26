package pl.kitek.beers.presenter

interface SafeUI<out UI> {
    fun perform(command: (UI) -> Unit)
}
