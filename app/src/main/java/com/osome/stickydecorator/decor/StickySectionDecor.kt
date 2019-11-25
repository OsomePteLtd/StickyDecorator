package com.osome.stickydecorator.decor

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.osome.stickydecorator.Item
import com.osome.stickydecorator.ItemProvider
import com.osome.stickydecorator.SimpleTextDrawable
import com.osome.stickydecorator.VerticalStickyDrawableDecor

class StickySectionDecor(private val itemProvider: ItemProvider<Item>) : VerticalStickyDrawableDecor() {

    private val section = buildTextDrawable()
    private val header = buildTextDrawable()

    private fun buildTextDrawable(): SimpleTextDrawable {
        return SimpleTextDrawable.Builder()
                .setPaddingSymmetricDp(12, 4)
                .setBackgroundCornerRadiusDp(14)
                .setBackgroundColor(Color.LTGRAY)
                .build()
    }

    override fun getSectionDrawable(position: Int, sectionBounds: Rect, child: View): Drawable {
        section.text = getHeaderValue(position)
        return section
    }

    override fun getHeaderDrawable(position: Int, headerBounds: Rect): Drawable {
        header.text = getHeaderValue(position)
        return header
    }

    override fun getSectionBounds(parent: RecyclerView, position: Int, viewBounds: Rect, decoratedBounds: Rect): Rect {
        section.text = getHeaderValue(position)
        val temp = super.getSectionBounds(parent, position, viewBounds, decoratedBounds)
        section.setTopCenter(temp.exactCenterX(), temp.top.toFloat())
        return section.bounds
    }

    override fun getHeaderBounds(parent: RecyclerView, headerBottom: Int, itemPosition: Int, viewBounds: Rect, decoratedBounds: Rect): Rect {
        header.text = getHeaderValue(itemPosition)
        val temp = super.getHeaderBounds(parent, headerBottom, itemPosition, viewBounds, decoratedBounds)
        header.setTopCenter(temp.exactCenterX(), temp.top.toFloat())
        return header.bounds
    }

    private fun getHeaderValue(position: Int): String {
        val item = itemProvider.get(position)
        return "${(((item.value + 1) / 10) * 10)}th"
    }

    override fun getSectionHeight(position: Int): Int {
        return section.height
    }
}