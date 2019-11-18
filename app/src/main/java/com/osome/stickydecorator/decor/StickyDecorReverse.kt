package com.osome.stickydecorator.decor

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import com.osome.stickydecorator.Item
import com.osome.stickydecorator.ItemProvider
import com.osome.stickydecorator.VerticalStickyDrawableDecor


class StickyDecorReverse(private val itemProvider: ItemProvider<Item>) : VerticalStickyDrawableDecor(true) {

    private val section = buildTextDrawable()
    private val header = buildTextDrawable()

    private fun buildTextDrawable(): TextDrawable {
        val d = TextDrawable(
                textColor = Color.BLACK,
                backColor = Color.LTGRAY
        )
        d.text = "10th" // init drawable height
        return d
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
        temp.left = viewBounds.left + viewBounds.width() / 2 - section.getTextWidth() / 2
        temp.right = temp.left + section.getTextWidth()
        return temp
    }


    override fun getHeaderBounds(headerBottom: Int, itemPosition: Int, viewBounds: Rect, decoratedBounds: Rect): Rect {
        val temp = super.getHeaderBounds(headerBottom, itemPosition, viewBounds, decoratedBounds)
        header.text = getHeaderValue(itemPosition)
        temp.left = viewBounds.left + viewBounds.width() / 2 - header.getTextWidth() / 2
        temp.right = temp.left + header.getTextWidth()
        return temp
    }

    private fun getHeaderValue(position: Int): String {
        val item = itemProvider.get(position)
        return "${(((item.value) / 10) * 10)}th"
    }

    override fun getHeaderHeight(): Int {
        return header.getHeight()
    }

    override fun getSectionMarginTop(): Int {
        return section.getHeight()
    }

    override fun getSectionMarginBottom(): Int {
        return section.getHeight()
    }

    override fun getHeaderMarginTop(): Int {
        return header.getHeight() / 3
    }

    override fun getHeaderMarginBottom(): Int {
        return header.getHeight() / 3
    }

    override fun getSectionHeight(position: Int): Int {
        return section.getHeight()
    }
}