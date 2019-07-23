package pl.kitek.beers.presentation.common.presenter

import java.util.*

class UICommandExecutor<UI> : SafeUI<UI> {

    private var ui: UI? = null
    private val commandQueue: Queue<(UI) -> Unit> = LinkedList()

    fun attach(ui: UI) {
        this.ui = ui
        executePending()
    }

    fun detach() {
        this.ui = null
    }

    override fun perform(command: (UI) -> Unit) {
        val currentUI = this.ui
        if (currentUI != null) command(currentUI) else commandQueue.add(command)
    }

    private fun executePending() {
        val currentUI = this.ui
        if (currentUI != null) {
            var cmd = commandQueue.poll()
            while (cmd != null) {
                cmd(currentUI)
                cmd = commandQueue.poll()
            }
        }
    }
}
