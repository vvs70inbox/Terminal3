package ru.vvs.terminal1.screens.ordersFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.CartItem
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.screens.mainFragment.MainAdapter
import ru.vvs.terminal1.screens.mainFragment.MainFragment

class OrdersAdapter(private val onItemClick: (position: Int) -> Unit, private val onItemDelete: (position: Int) -> Unit): RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()
    private var listMain = emptyList<Order>()

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    class OrdersViewHolder(view: View, private val onItemClick: (position: Int) -> Unit, private val onItemDelete: (position: Int) -> Unit): RecyclerView.ViewHolder(view) {
        init {
            itemView.findViewById<View>(R.id.main_layout).setOnClickListener { onItemClick(adapterPosition) }
            itemView.findViewById<View>(R.id.delete_button).setOnClickListener { onItemDelete(adapterPosition) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_orders_layout, parent, false)
        return OrdersViewHolder(view, onItemClick, onItemDelete)
    }

    override fun getItemCount(): Int {
        return listMain.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val dataObject = listMain[position];
        val swipeLayout = holder.itemView.findViewById<SwipeRevealLayout>(R.id.swipe_reveal);
        viewBinderHelper.bind(swipeLayout, dataObject.id.toString());

        holder.itemView.findViewById<TextView>(R.id.item_number).text = listMain[position].id.toString()//.name
        holder.itemView.findViewById<TextView>(R.id.item_date).text = "от ${listMain[position].date}"
        holder.itemView.findViewById<TextView>(R.id.item_positions).text ="${listMain[position].positions} тов."
        holder.itemView.findViewById<TextView>(R.id.item_products).text = "${listMain[position].products} шт."
        holder.itemView.findViewById<TextView>(R.id.item_amount).text = listMain[position].amount.toString()
    }

    fun setList(list: List<Order>) {
        listMain = list
        notifyDataSetChanged()
    }

    override fun onViewAttachedToWindow(holder: OrdersAdapter.OrdersViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            OrdersFragment.clickOrder(listMain[holder.adapterPosition])
        }
    }

    override fun onViewDetachedFromWindow(holder: OrdersAdapter.OrdersViewHolder) {
        holder.itemView.setOnClickListener(null)
    }

}