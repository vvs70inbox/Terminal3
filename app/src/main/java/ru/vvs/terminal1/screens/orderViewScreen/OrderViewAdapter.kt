package ru.vvs.terminal1.screens.orderViewScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.OrderItem
import ru.vvs.terminal1.model.Product

class OrderViewAdapter(private val onItemClick: (position: Int) -> Unit, private val onItemDelete: (position: Int) -> Unit): RecyclerView.Adapter<OrderViewAdapter.OrderViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()
    private var orderItemList = emptyList<OrderItem>()
    private var productList = emptyList<Product>()

    class OrderViewHolder(view: View, private val onItemClick: (position: Int) -> Unit, private val onItemDelete: (position: Int) -> Unit): RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
           itemView.findViewById<View>(R.id.main_layout)?.setOnClickListener { onItemClick(adapterPosition) }
           itemView.findViewById<View>(R.id.delete_button)?.setOnClickListener { onItemDelete(adapterPosition) }
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
        return orderItemList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val dataObject = orderItemList[position];
        val swipeLayout = holder.itemView.findViewById<SwipeRevealLayout>(R.id.swipe_reveal);
        viewBinderHelper.bind(swipeLayout, dataObject.barcode);

        val orderItem = orderItemList[position];
        val product = productList.find { it.barcode == orderItem.barcode };

        holder.itemView.findViewById<TextView>(R.id.russian_name).text = product?.russianName;
        holder.itemView.findViewById<TextView>(R.id.price).text = "${product?.price} руб."
        holder.itemView.findViewById<TextView>(R.id.barcode).text = orderItem.barcode
        holder.itemView.findViewById<TextView>(R.id.history).text = product?.history

        holder.itemView.findViewById<TextView>(R.id.latin_name).text = product?.latinName
        //holder.itemView.findViewById<TextView>(R.id.container).text = listMain[position].PotSize
        //holder.itemView.findViewById<TextView>(R.id.height).text = listMain[position].Height
        //holder.itemView.findViewById<TextView>(R.id.remainder).text = listMain[position].Quantity.toString()
        holder.itemView.findViewById<TextView>(R.id.amount).text = product?.quantity.toString()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setOrderItemList(list: List<OrderItem>) {
        orderItemList = list
        notifyDataSetChanged()
    }

    // TODO: Notify properly
    @SuppressLint("NotifyDataSetChanged")
    fun setLists(orders: List<OrderItem>, products: List<Product>) {
        orderItemList = orders
        productList = products
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(list: List<Product>) {
        productList = list
        notifyDataSetChanged()
    }
}
