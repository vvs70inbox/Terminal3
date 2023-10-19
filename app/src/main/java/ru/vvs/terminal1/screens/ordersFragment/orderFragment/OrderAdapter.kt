package ru.vvs.terminal1.screens.ordersFragment.orderFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.ItemsOrder

class OrderAdapter(private val onItemClick: (position: Int) -> Unit): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    var listMain = emptyList<ItemsOrder>()
    class OrderViewHolder(view: View, private val onItemClick: (position: Int) -> Unit): RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
           itemView.setOnClickListener { _ -> onItemClick(adapterPosition) }
        }

        override fun onClick(v: View?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_layout, parent, false)
        return OrderViewHolder(view, onItemClick)
    }

    override fun getItemCount(): Int {
        return listMain.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.item_product_order).text = listMain[position].Product
        holder.itemView.findViewById<TextView>(R.id.item_character_order).text = listMain[position].Character
        holder.itemView.findViewById<TextView>(R.id.item_barcode_order).text = listMain[position].Barcode
        holder.itemView.findViewById<TextView>(R.id.item_price_order).text = listMain[position].Price.toString()
        holder.itemView.findViewById<TextView>(R.id.item_count_order).text = listMain[position].counts.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ItemsOrder>) {
        listMain = list
        notifyDataSetChanged()
    }

/*    override fun onViewAttachedToWindow(holder: OrderAdapter.OrderViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            OrderFragment.clickItem(listMain[holder.adapterPosition])
        }
    }

    override fun onViewDetachedFromWindow(holder: OrderAdapter.OrderViewHolder) {
        holder.itemView.setOnClickListener(null)
    }*/

}
