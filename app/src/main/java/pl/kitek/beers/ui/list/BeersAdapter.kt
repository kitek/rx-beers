package pl.kitek.beers.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.beer_item.view.*
import pl.kitek.beers.R
import pl.kitek.beers.data.model.Beer
import pl.kitek.beers.data.model.Page
import pl.kitek.beers.ui.common.load

class BeersAdapter(
    private val onItemClickListener: ItemClickListener
) : RecyclerView.Adapter<ViewHolder>(), View.OnClickListener {

    private var items = emptyList<Any>()

    fun setItems(page: Page<Beer>) {
        val newItems = ArrayList<Any>()
        newItems.addAll(page.items)
        if (page.metadata.hasMore()) newItems.add(LoadMore())

        val diff = DiffUtil.calculateDiff(BeersDiffCallback(items, newItems), false)
        items = newItems

        diff.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Beer -> TYPE_BEER
            is LoadMore -> TYPE_LOAD_MORE
            else -> TYPE_BEER
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.loadMoreBtn -> onItemClickListener.onLoadMoreClick()
            R.id.beerContainer -> {
                val position = v.tag as Int
                val beer = items[position] as Beer
                onItemClickListener.onBeerClick(beer)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_BEER -> createBeerViewHolder(parent)
            TYPE_LOAD_MORE -> createLoadMoreViewHolder(parent)
            else -> createBeerViewHolder(parent)
        }
    }

    private fun createBeerViewHolder(parent: ViewGroup): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.beer_item, parent, false)
        itemView.setOnClickListener(this)

        return BeersViewHolder(
            itemView.beerImage,
            itemView.beerNameTxt,
            itemView.beerTagTxt,
            itemView.beerDescriptionTxt,
            itemView
        )
    }

    private fun createLoadMoreViewHolder(parent: ViewGroup): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.load_more, parent, false)
        itemView.findViewById<Button>(R.id.loadMoreBtn).setOnClickListener(this)

        return LoadMoreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is BeersViewHolder -> {
                val model = items[position] as Beer
                holder.bind(model, position)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    private class BeersViewHolder(
        private val beerImage: ImageView,
        private val beerNameTxt: TextView,
        private val beerTagLineTxt: TextView,
        private val beerDescriptionTxt: TextView,
        itemView: View
    ) : ViewHolder(itemView) {

        fun bind(beer: Beer, position: Int) {
            itemView.tag = position
            beerImage.load(beer.imageUrl)
            beerNameTxt.text = beer.name
            beerTagLineTxt.text = beer.tagline
            beerDescriptionTxt.text = beer.description
        }
    }

    class LoadMoreViewHolder(itemView: View) : ViewHolder(itemView)

    private class LoadMore

    interface ItemClickListener {
        fun onBeerClick(beer: Beer)
        fun onLoadMoreClick()
    }

    companion object {
        private const val TYPE_BEER = 1
        private const val TYPE_LOAD_MORE = 2
    }
}