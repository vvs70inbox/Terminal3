package ru.vvs.terminal1.screens.ordersFragment.orderFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.ItemsOrder

class OrderAdapter(private val onItemClick: (position: Int) -> Unit, private val onItemDelete: (position: Int) -> Unit): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()
    var listMain = emptyList<ItemsOrder>()

    class OrderViewHolder(view: View, private val onItemClick: (position: Int) -> Unit, private val onItemDelete: (position: Int) -> Unit): RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
           itemView.findViewById<View>(R.id.main_layout).setOnClickListener { onItemClick(adapterPosition) }
           itemView.findViewById<View>(R.id.delete_button).setOnClickListener { onItemDelete(adapterPosition) }
        }

        override fun onClick(v: View?) {

        }
    }

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_layout, parent, false)
        return OrderViewHolder(view, onItemClick, onItemDelete)
    }

    override fun getItemCount(): Int {
        return listMain.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val dataObject = listMain[position];
        val swipeLayout = holder.itemView.findViewById<SwipeRevealLayout>(R.id.swipe_reveal);
        viewBinderHelper.bind(swipeLayout, dataObject.Barcode);

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
