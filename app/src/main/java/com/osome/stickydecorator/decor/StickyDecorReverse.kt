package com.osome.stickydecorator.decor

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import com.osome.stickydecorator.Item
import com.osome.stickydecorator.ItemProvider
import com.osome.stickydecorator.SimpleTextDrawable
import com.osome.stickydecorator.VerticalStickyDrawableDecor


class StickyDecorReverse(private val itemProvider: ItemProvider<Item>) : VerticalStickyDrawableDecor(true) {

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
        val item = itemProvider.get(position)
        val value: Int = (((item.value) / 10) * 10)
        section.text = "${value}th"
        return section
    }

    override fun getHeaderDrawable(position: Int, headerBounds: Rect): Drawable {
        val value = getHeaderValue(position)
        header.text = value
        return header
    }

    override fun getSectionBounds(position: Int, viewBounds: Rect, decoratedBounds: Rect): Rect {
        section.text = getHeaderValue(position)
        val temp = super.getSectionBounds(position, viewBounds, decoratedBounds)
        section.setTopCenter(temp.exactCenterX(), temp.top.toFloat())
        return section.bounds
    }


    override fun getHeaderBounds(headerBottom: Int, itemPosition: Int, viewBounds: Rect, decoratedBounds: Rect): Rect {
        val temp = super.getHeaderBounds(headerBottom, itemPosition, viewBounds, decoratedBounds)
        header.text = getHeaderValue(itemPosition)
        header.setTopCenter(temp.exactCenterX(), temp.top.toFloat())
        return header.bounds
    }

    private fun getHeaderValue(position: Int): String {
        val item = itemProvider.get(position)
        return "${(((item.value) / 10) * 10)}th"
    }

    override fun getHeaderHeight(): Int {
        return header.height
    }

    override fun getSectionMarginTop(): Int {
        return section.height
    }

    override fun getSectionMarginBottom(): Int {
        return section.height
    }

    override fun getHeaderMarginTop(): Int {
        return header.height / 3
    }

    override fun getHeaderMarginBottom(): Int {
        return header.height / 3
    }

    override fun getSectionHeight(position: Int): Int {
        return section.height
    }
}