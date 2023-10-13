package ru.vvs.terminal1.screens.ordersFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.Order

class OrdersAdapter: RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    var listMain = emptyList<Order>()

    class OrdersViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_orders_layout, parent, false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMain.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.item_number).text = listMain[position].name
        holder.itemView.findViewById<TextView>(R.id.item_date).text = listMain[position].date
        holder.itemView.findViewById<TextView>(R.id.item_positions).text = listMain[position].positions.toString()
        holder.itemView.findViewById<TextView>(R.id.item_products).text = listMain[position].products.toString()
        holder.itemView.findViewById<TextView>(R.id.item_amount).text = listMain[position].amount.toString()
    }
}