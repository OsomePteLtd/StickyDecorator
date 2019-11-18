package com.osome.stickydecorator.decor

import android.graphics.*
import android.graphics.drawable.Drawable
import com.osome.stickydecorator.px2dp
import com.osome.stickydecorator.px2dpF
import kotlin.math.roundToInt

class TextDrawable(private val textColor: Int,
                   private val backColor: Int,
                   private val textSize: Float = 12.px2dpF) : Drawable() {

    private val radius = 10.px2dpF
    private var textWidth = 0
    private var textHeight = 0

    private val paintBack = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = backColor
    }
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = this@TextDrawable.textSize
        color = textColor
    }

    private val rectF = RectF()
    private val textBounds = Rect()

    var horizontalPadding = 6.px2dp
    var verticalPadding = 4.px2dp

    var text: CharSequence = ""
        set(value) {
            if (value != field) {
                field = value
                textWidth = paintText.measureText(value, 0, value.length).roundToInt()

                paintText.getTextBounds(value.toString(), 0, value.length, textBounds)
                textHeight = textBounds.height()
            }
        }
    private var cX = 0F
    private var cY = 0F


    override fun draw(canvas: Canvas) {
        if (paintBack.color != Color.TRANSPARENT)
            canvas.drawRoundRect(rectF, radius, radius, paintBack)

        canvas.drawText(
                text,
                0,
                text.length,
                cX,
                cY,
                paintText)
    }

    override fun setAlpha(alpha: Int) {
        paintText.alpha = alpha
        paintBack.alpha = alpha
    }

    fun getHeight(): Int = textHeight + verticalPadding * 2

    fun getTextWidth(): Int = textWidth + horizontalPadding * 2

    /**
     * Set top left position
     */
    fun updatePosition(x: Float, y: Float) {
        setBounds(
                x.roundToInt(),
                y.roundToInt(),
                (x + textWidth + horizontalPadding * 2).roundToInt(),
                (y + textHeight + verticalPadding * 2).roundToInt())
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        rectF.set(bounds)
        cX = bounds.exactCenterX()
        cY = bounds.exactCenterY() - (paintText.descent() + paintText.ascent()) / 2f
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paintBack.colorFilter = colorFilter
        paintText.colorFilter = colorFilter
    }
}