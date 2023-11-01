package ru.vvs.terminal1.screens.salesFragment.saleFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vvs.terminal1.R
import ru.vvs.terminal1.model.ItemsSale

class SaleAdapter(private val onItemClick: (position: Int) -> Unit): RecyclerView.Adapter<SaleAdapter.SaleViewHolder>() {

    var listMain = emptyList<ItemsSale>()
    class SaleViewHolder(view: View, private val onItemClick: (position: Int) -> Unit): RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            itemView.setOnClickListener { _ -> onItemClick(adapterPosition) }
        }
        override fun onClick(v: View?) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sale_layout, parent, false)
        return SaleViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.item_product_sale).text = listMain[position].Product
        holder.itemView.findViewById<TextView>(R.id.item_character_sale).text = listMain[position].Character
        holder.itemView.findViewById<TextView>(R.id.item_barcode_sale).text = listMain[position].Barcode
        holder.itemView.findViewById<TextView>(R.id.item_price_sale).text = listMain[position].Price.toString()
        holder.itemView.findViewById<TextView>(R.id.item_count_sale).text = listMain[position].counts.toString()
        //holder.itemView.findViewById<TextView>(R.id.item_count_sale).text = listMain[position].checks.toString()
    }

    override fun getItemCount(): Int {
        return listMain.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ItemsSale>) {
        listMain = list.sortedBy { it.Product }
        notifyDataSetChanged()
    }

}