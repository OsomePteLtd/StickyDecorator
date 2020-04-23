package com.osome.stickydecorator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemHolder>(), ItemProvider<Item> {

    private val differ: AsyncListDiffer<Item> = AsyncListDiffer(this, DiffItem())

    init {
        setList(items)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): Item {
        return get(position)
    }

    fun setList(list: List<Item>) {
        differ.submitList(list.toList())
    }

    fun getList() = differ.currentList.toList()

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)

        @SuppressLint("SetTextI18n")
        fun bind(item: Item) {
            tvItem.text = "Item with number ${item.value}"
        }
    }

    override fun get(position: Int): Item {
        return differ.currentList[position]
    }

    override fun size(): Int = itemCount
}

class DiffItem : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.value == newItem.value
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.value == newItem.value
    }

}