package com.osome.stickydecorator.decor

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import com.osome.stickydecorator.Item
import com.osome.stickydecorator.ItemProvider
import com.osome.stickydecorator.VerticalDrawableSectionDecor

class SectionDecorReverse(private val itemProvider: ItemProvider<Item>) : VerticalDrawableSectionDecor() {

    private val section = buildTextDrawable()

    private fun buildTextDrawable(): TextDrawable {
        val d = TextDrawable(
                textColor = Color.BLACK,
                backColor = Color.LTGRAY
        )
        d.text = "0" // init drawable height
        return d
    }

    override fun getSectionHeight(position: Int): Int {
        return section.getHeight()
    }

    override fun getSectionMarginTop(): Int {
        return section.getHeight()
    }

    override fun getDrawable(position: Int, sectionBounds: Rect, child: View): Drawable {
        val item = itemProvider.get(position)
        val value: Int = (((item.value) / 10) * 10)
        section.text = "${value}th"
        return section
    }
}