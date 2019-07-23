package pl.kitek.beers.presentation.common.presenter

interface SafeUI<out UI> {
    fun perform(command: (UI) -> Unit)
}
