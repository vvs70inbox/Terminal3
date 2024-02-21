package ru.vvs.terminal1.screens.productListScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.Product

class ProductListAdapter: RecyclerView.Adapter<ProductListAdapter.MainViewHolder>() {

    var listMain = emptyList<Product>()
    var listMainFull = emptyList<Product>()

    class MainViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_layout, parent, false)
        return MainViewHolder(view)
    }

    // method for filtering our recyclerview items.
    fun filterList(filterlist: ArrayList<Product>) {
        // below line is to add our filtered
        // list in our course array list.
        listMain = filterlist
        // below line is to notify our adapter
        notifyDataSetChanged()
        // as change in recycler view data.
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
        //holder.itemView.findViewById<TextView>(R.id.item_name).text = listMain[position].GroupString.substringAfter("/")
        holder.itemView.findViewById<TextView>(R.id.russian_name).text = listMain[position].russianName
        holder.itemView.findViewById<TextView>(R.id.latin_name).text = listMain[position].latinName
        holder.itemView.findViewById<TextView>(R.id.container).text = listMain[position].container
        holder.itemView.findViewById<TextView>(R.id.height).text = listMain[position].height
        holder.itemView.findViewById<TextView>(R.id.remainder).text = listMain[position].quantity.toString()
        holder.itemView.findViewById<TextView>(R.id.price).text = "${listMain[position].price} руб."
        holder.itemView.findViewById<TextView>(R.id.barcode).text = listMain[position].barcode
        holder.itemView.findViewById<TextView>(R.id.history).text = listMain[position].history
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Product>) {
        listMain = list
        listMainFull = list
        notifyDataSetChanged()
    }

    override fun onViewAttachedToWindow(holder: MainViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            ProductListFragment.navigateToCart(listMain[holder.adapterPosition].barcode)
        }
    }

    override fun onViewDetachedFromWindow(holder: MainViewHolder) {
        holder.itemView.setOnClickListener(null)
    }

}