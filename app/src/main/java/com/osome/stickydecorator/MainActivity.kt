package com.osome.stickydecorator

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.osome.stickydecorator.decor.SectionDecor
import com.osome.stickydecorator.decor.SectionDecorReverse
import com.osome.stickydecorator.decor.StickyDecorReverse
import com.osome.stickydecorator.decor.StickySectionDecor
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val adapter = ItemAdapter(generateItem(32))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Select options here  =======>"

        setUpStickyRecycler()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        title = item.title
        when (item.itemId) {
            R.id.sticky -> setUpStickyRecycler()
            R.id.stickyReverse -> setUpStickyReverseRecycler()
            R.id.section -> setUpSectionRecycler()
            R.id.sectionReverse -> setUpSectionReverseRecycler()
            R.id.viewHolder -> setUpViewHolderSection()
            R.id.viewHolderReverse -> setUpViewHolderSectionReverse()
            R.id.viewHolderGrid -> setUpViewHolderSectionGrid()
            R.id.viewHolderGridReverse -> setUpViewHolderSectionGridReverse()
        }
        return true
    }

    private fun setUpStickyRecycler() {
        clearDecoration()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context)
        val itemProvider: ItemProvider<Item> = adapter
        recycler.addItemDecoration(ConditionItemDecorator(
                { position -> (itemProvider.get(position).value + 1) % 10 == 0 || position == 0 },
                StickySectionDecor(adapter)))
    }

    private fun setUpStickyReverseRecycler() {
        clearDecoration()
        recycler.adapter = adapter
        val itemProvider: ItemProvider<Item> = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, true)
        recycler.addItemDecoration(
                ConditionItemDecorator({ position ->
                    position != 0 && ((itemProvider.get(position)).value + 1) % 10 == 0
                }, StickyDecorReverse(adapter)))
    }

    private fun setUpSectionRecycler() {
        clearDecoration()
        recycler.adapter = adapter
        val itemProvider: ItemProvider<Item> = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context)
        recycler.addItemDecoration(
                ConditionItemDecorator(
                        { position -> (itemProvider.get(position).value + 1) % 10 == 0 || position == 0 },
                        SectionDecor(adapter)))
    }

    private fun setUpSectionReverseRecycler() {
        clearDecoration()
        recycler.adapter = adapter
        val itemProvider: ItemProvider<Item> = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, true)
        recycler.addItemDecoration(
                ConditionItemDecorator({ position ->
                    position != 0 && ((itemProvider.get(position)).value + 1) % 10 == 0 || position == itemProvider.size() - 1
                }, SectionDecorReverse(adapter)))
    }

    private fun setUpViewHolderSection() {
        clearDecoration()
        val adapter = SectionItemAdapter(generateItemWithSection(34))
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context)
        recycler.addItemDecoration(ViewHolderStickyDecoration(recycler, adapter))
    }

    private fun setUpViewHolderSectionReverse() {
        clearDecoration()
        val adapter = SectionItemAdapter(generateItemWithSectionReverse(34))
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, true)
        recycler.addItemDecoration(ViewHolderStickyDecoration(recycler, adapter, true))
    }

    private fun setUpViewHolderSectionGrid() {
        clearDecoration()
        val adapter = SectionItemAdapter(generateItemWithSection(34))
        recycler.adapter = adapter

        val spanCount = 3
        val lm = GridLayoutManager(recycler.context, spanCount)

        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter.getItemViewType(position) == SectionItemAdapter.TYPE_HEADER)
                    return spanCount
                return 1
            }
        }
        recycler.layoutManager = lm
        recycler.addItemDecoration(ViewHolderStickyDecoration(recycler, adapter))
    }

    private fun setUpViewHolderSectionGridReverse() {
        clearDecoration()
        val adapter = SectionItemAdapter(generateItemWithSectionReverse(34))
        recycler.adapter = adapter

        val spanCount = 3
        val lm = GridLayoutManager(recycler.context, spanCount, GridLayoutManager.VERTICAL, true)

        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter.getItemViewType(position) == SectionItemAdapter.TYPE_HEADER)
                    return spanCount
                return 1
            }
        }
        recycler.layoutManager = lm
        recycler.addItemDecoration(ViewHolderStickyDecoration(recycler, adapter, true))
    }

    private fun clearDecoration() {
        for (i in 0 until recycler.itemDecorationCount) {
            recycler.removeItemDecorationAt(i)
        }
    }
}