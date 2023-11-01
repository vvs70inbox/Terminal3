package ru.vvs.terminal1.screens.salesFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.model.Sale
import ru.vvs.terminal1.screens.ordersFragment.OrdersAdapter
import ru.vvs.terminal1.screens.ordersFragment.OrdersFragment

class SalesAdapter: RecyclerView.Adapter<SalesAdapter.SalesViewHolder>() {

    private var listMain = emptyList<Sale>()
    class SalesViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sales_layout, parent, false)
        return SalesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMain.size
    }

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.sale_number).text = listMain[position].number
        holder.itemView.findViewById<TextView>(R.id.sale_date).text = "от ${listMain[position].date.substring(0..9)}"
        holder.itemView.findViewById<TextView>(R.id.sale_amount).text = listMain[position].amount.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Sale>) {
        listMain = list
        notifyDataSetChanged()
    }

    override fun onViewAttachedToWindow(holder: SalesAdapter.SalesViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            SalesFragment.clickSale(listMain[holder.adapterPosition])
        }
    }

    override fun onViewDetachedFromWindow(holder: SalesAdapter.SalesViewHolder) {
        holder.itemView.setOnClickListener(null)
    }
}