package ru.vvs.terminal1.screens.productScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.CartItem

class CartAdapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var listMain = emptyList<CartItem>()
    class CartViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMain.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.container).text = listMain[position].PotSize
        holder.itemView.findViewById<TextView>(R.id.height).text = listMain[position].Height
        holder.itemView.findViewById<TextView>(R.id.remainder).text = listMain[position].Quantity.toString()
        holder.itemView.findViewById<TextView>(R.id.price).text = "${listMain[position].Price} руб."
        holder.itemView.findViewById<TextView>(R.id.barcode).text = listMain[position].Barcode
        holder.itemView.findViewById<TextView>(R.id.history).text = listMain[position].History
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<CartItem>) {
        listMain = list.sortedBy { it.Price }
        notifyDataSetChanged()
    }

}