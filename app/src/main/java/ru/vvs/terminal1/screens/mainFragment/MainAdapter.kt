package ru.vvs.terminal1.screens.mainFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.CartItem

class MainAdapter: RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var listMain = emptyList<CartItem>()
    private var listMainFull = emptyList<CartItem>()

    class MainViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_layout, parent, false)
        return MainViewHolder(view)
    }

    // method for filtering our recyclerview items.
    fun filterList(filterlist: ArrayList<CartItem>) {
        // below line is to add our filtered
        // list in our course array list.
        listMain = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }
    fun filterRemove() {
        // below line is to add our filtered
        // list in our course array list.
        listMain = listMainFull
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listMain.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.item_name).text = listMain[position].GroupString.substringAfter("/")
        holder.itemView.findViewById<TextView>(R.id.item_buy).text = listMain[position].Product.substringBefore(",")
        holder.itemView.findViewById<TextView>(R.id.item_english).text = listMain[position].Product.substringAfter(",").trim()
        holder.itemView.findViewById<TextView>(R.id.item_character).text = listMain[position].Character
        holder.itemView.findViewById<TextView>(R.id.item_count).text = listMain[position].Quantity.toString()
        holder.itemView.findViewById<TextView>(R.id.item_price).text = listMain[position].Price.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<CartItem>) {
        listMain = list
        listMainFull = list
        notifyDataSetChanged()
    }

    override fun onViewAttachedToWindow(holder: MainViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            MainFragment.navigateToCart(listMain[holder.adapterPosition].Barcode)
        }
    }

    override fun onViewDetachedFromWindow(holder: MainViewHolder) {
        holder.itemView.setOnClickListener(null)
    }

}