package com.osome.stickydecorator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemHolder>(), ItemProvider<Item> {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): Item {
        return items[position]
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)

        @SuppressLint("SetTextI18n")
        fun bind(item: Item) {
            tvItem.text = "Item with number ${item.value}"
        }
    }

    override fun get(position: Int): Item {
        return items[position]
    }

    override fun size(): Int = items.size
}