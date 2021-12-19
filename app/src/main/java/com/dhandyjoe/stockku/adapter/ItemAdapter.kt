package com.dhandyjoe.stockku.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dhandyjoe.stockku.databinding.ItemListStockBinding
import com.dhandyjoe.stockku.model.Item

class ItemAdapter(private val data: ArrayList<Item>, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class MyViewHolder(val binding: ItemListStockBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemListStockBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = data[position]

        if (holder is MyViewHolder) {
            holder.binding.tvNameItem.text = model.name
            holder.binding.tvSizeItem.text = model.size
            holder.binding.tvStockItem.text = model.stock.toString()
            holder.binding.tvPriceItem.text = model.price
            Glide.with(context)
                .load(model.imageUrl)
                .into(holder.binding.ivItem)

            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(data[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = data.size

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Item)
    }
}