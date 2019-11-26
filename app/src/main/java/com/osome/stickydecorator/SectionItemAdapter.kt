package com.osome.stickydecorator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SectionItemAdapter(private val items: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ViewHolderStickyDecoration.Condition {
    companion object {
        const val TYPE_HEADER = R.layout.section
        const val TYPE_ITEM = R.layout.list_item
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.list_item) {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return ItemHolder(view)
        }

        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return SectionHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeader(position))
            return TYPE_HEADER
        return TYPE_ITEM
    }

    override fun isHeader(position: Int): Boolean {
        return getItem(position) is SectionItem
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_ITEM) {
            (holder as ItemHolder).bind(getItem(position))
        } else if (holder.itemViewType == TYPE_HEADER) {
            (holder as SectionHolder).bind(getItem(position) as SectionItem)
        }
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

    class SectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv = itemView as TextView

        fun bind(item: SectionItem) {
            tv.text = "${item.value}th"
        }
    }
}