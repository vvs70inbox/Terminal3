package ru.vvs.terminal1.screens.mainFragment.cartFragment

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
        holder.itemView.findViewById<TextView>(R.id.item_character_cart).text = listMain[position].Character
        holder.itemView.findViewById<TextView>(R.id.item_barcode_cart).text = listMain[position].Barcode
        holder.itemView.findViewById<TextView>(R.id.item_count_cart).text = listMain[position].Quantity.toString()
        holder.itemView.findViewById<TextView>(R.id.item_price_cart).text = listMain[position].Price.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<CartItem>) {
        listMain = list.sortedBy { it.Price }
        notifyDataSetChanged()
    }

}