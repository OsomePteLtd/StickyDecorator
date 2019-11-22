package com.osome.stickydecorator.decor

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import com.osome.stickydecorator.Item
import com.osome.stickydecorator.ItemProvider
import com.osome.stickydecorator.SimpleTextDrawable
import com.osome.stickydecorator.VerticalDrawableSectionDecor

class SectionDecor(private val itemProvider: ItemProvider<Item>) : VerticalDrawableSectionDecor() {

    private val section = buildTextDrawable()

    private fun buildTextDrawable(): SimpleTextDrawable {
        return SimpleTextDrawable.Builder()
                .setPaddingSymmetricDp(12, 4)
                .setBackgroundColor(Color.LTGRAY)
                .setTextGravity(SimpleTextDrawable.SimpleGravity.LEFT)
                .setTextColor(Color.BLACK)
                .setTextSizeDp(14)
                .build()
    }

    override fun getSectionHeight(position: Int): Int {
        return section.height
    }

    override fun getSectionMarginTop(): Int {
        return section.height
    }

    override fun getDrawable(position: Int, sectionBounds: Rect, child: View): Drawable {
        val item = itemProvider.get(position)
        val value: Int = (((item.value + 1) / 10) * 10)
        section.text = "${value}th"
        return section
    }
}