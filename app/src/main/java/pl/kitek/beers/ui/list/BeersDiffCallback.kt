package pl.kitek.beers.ui.list

import androidx.recyclerview.widget.DiffUtil
import pl.kitek.beers.data.model.Beer

class BeersDiffCallback(
    private val oldItems: List<Any>,
    private val newItems: List<Any>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        if (oldItem is Beer && newItem is Beer) {
            return oldItem.id == newItem.id
        }

        return oldItem.javaClass.isAssignableFrom(newItem.javaClass)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem == newItem
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

}
