package com.osome.stickydecorator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SimpleTextDrawable extends Drawable {

    private final int paddingLeft;
    private final int paddingTop;
    private final int paddingRight;
    private final int paddingBottom;
    private final int bgRadius;
    private final Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintBack = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Rect textBounds = new Rect();
    private final RectF bgBounds = new RectF();
    private String textForDraw;
    private int textHeight;
    private int textWidth;
    private float textX = 0;
    private float textY = 0;
    private SimpleGravity textGravity;

    protected SimpleTextDrawable(float textSize,
                                 int colorText,
                                 SimpleGravity gravity,
                                 int colorBackground,
                                 int bgRadius,
                                 int paddingLeft,
                                 int paddingTop,
                                 int paddingRight,
                                 int paddingBottom,
                                 Typeface typeface) {
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
        this.bgRadius = bgRadius;

        textGravity = gravity;
        paintText.setColor(colorText);
        if (typeface != null) {
            paintText.setTypeface(typeface);
        }
        paintText.setTextSize(textSize);

        paintBack.setColor(colorBackground);
    }

    @Override
    public void setAlpha(int alpha) {
        paintText.setAlpha(alpha);
        paintBack.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paintText.setColorFilter(colorFilter);
        paintBack.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        if (paintBack.getColor() != Color.TRANSPARENT || paintBack.getAlpha() > 0) {
            canvas.drawRoundRect(bgBounds, bgRadius, bgRadius, paintBack);
        }

        canvas.drawText(textForDraw, 0, textForDraw.length(), textX, textY, paintText);
    }

    /**
     * Update text for this drawable and recalculate size
     *
     * @param text - New text value
     */
    public void setText(@NonNull String text) {
        if (!text.equals(this.textForDraw)) {
            this.textForDraw = text;
            paintText.getTextBounds(text, 0, text.length(), textBounds);
            textHeight = textBounds.height();
            textWidth = textBounds.width();
        }
    }

    /**
     * Return current text
     *
     * @return current text
     */
    public String getText() {
        return textForDraw;
    }

    /**
     * Set top left position for drawable.
     * <p>
     * This method calculate bounds based on text size and text padding
     * <p>
     * DON'T use this method if your section or header has full width
     *
     * @param x X coordinate of top left corner
     * @param y Y coordinate of top left corner
     */
    public void setTopLeft(float x, float y) {
        setBounds(
                (int) x,
                (int) y,
                (int) (x + textWidth + paddingLeft + paddingRight),
                (int) (y + textHeight + paddingTop + paddingBottom)
        );
    }

    /**
     * Set top left position for drawable.
     * <p>
     * This method calculate bounds based on text size and text padding
     * <p>
     * DON'T use this method if your section or header has full width
     *
     * @param x X coordinate of top left corner
     * @param y Y coordinate of top left corner
     */
    public void setTopLeft(int x, int y) {
        setTopLeft((float) x, (float) y);
    }

    /**
     * Set top center position for drawable
     * <p>
     * This method calculate bounds based on text size and text padding
     * <p>
     * DON'T use this method if your section or header has full width
     *
     * @param x X coordinate of top center
     * @param y Y coordinate of top center
     */
    public void setTopCenter(float x, float y) {
        setBounds(
                (int) (x - (textWidth / 2) - paddingLeft),
                (int) y,
                (int) (x + (textWidth / 2) + paddingRight),
                (int) (y + textHeight + paddingTop + paddingBottom)
        );
    }

    /**
     * Set top center position for drawable
     * <p>
     * This method calculate bounds based on text size and text padding
     * <p>
     * DON'T use this method if your section or header has full width
     *
     * @param x X coordinate of top center
     * @param y Y coordinate of top center
     */
    public void setTopCenter(int x, int y) {
        setTopCenter((float) x, (float) y);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        Rect bounds = getBounds();
        bgBounds.set(bounds);
        textX = calculateTextX(bounds);
        textY = bounds.exactCenterY() - (paintText.descent() + paintText.ascent()) / 2f;
    }

    private float calculateTextX(Rect bounds) {
        switch (textGravity) {
            case LEFT:
                return bounds.left + paddingLeft;
            case RIGHT:
                return bounds.right - paddingRight - textWidth;
            default:
                return bounds.exactCenterX() - textWidth / 2f;
        }
    }

    /**
     * Text width with padding
     *
     * @return text width with padding
     */
    public int getTextWidth() {
        return textWidth + paddingLeft + paddingRight;
    }

    /**
     * This is drawable bounds width
     * <p>
     * {@link #getWidth()} >= {@link #getTextWidth()}
     *
     * @return drawable bounds width
     */
    public int getWidth() {
        return getBounds().width();
    }

    /**
     * Text height with padding
     *
     * @return text height with padding
     */
    public int getHeight() {
        return textHeight + paddingTop + paddingBottom;
    }

    public enum SimpleGravity {
        LEFT, RIGHT, CENTER
    }

    public static class Builder {
        private float textSize = Util.dpToPxF(12);
        private int colorText = Color.BLACK;
        private int colorBackground = Color.TRANSPARENT;
        private int bgRadius = 0;
        private int paddingLeft = 0;
        private int paddingTop = 0;
        private int paddingRight = 0;
        private int paddingBottom = 0;
        private SimpleGravity gravity = SimpleGravity.CENTER;
        private Typeface typeface;

        /**
         * Set up text size in pixels
         *
         * @param textSize test size in pixels
         * @return updated builder instance
         */
        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * Set up text size in dip and convert in to pixels
         *
         * @param textSize text size in dix
         * @return updated builder instance
         */
        public Builder setTextSizeDp(int textSize) {
            this.textSize = Util.dpToPxF(textSize);
            return this;
        }

        /**
         * Set up text color
         *
         * @param colorText text color
         * @return updated builder instance
         */
        public Builder setTextColor(int colorText) {
            this.colorText = colorText;
            return this;
        }

        /**
         * Set up background color
         *
         * @param colorBackground background color
         * @return updated builder instance
         */
        public Builder setBackgroundColor(int colorBackground) {
            this.colorBackground = colorBackground;
            return this;
        }

        /**
         * Set up background corners radius in pixels
         *
         * @param bgRadius background corners radius in pixels
         * @return updated builder instance
         */
        public Builder setBackgroundCornerRadius(int bgRadius) {
            this.bgRadius = bgRadius;
            return this;
        }

        /**
         * Set up background corners radius in dip and convert in to pixels
         *
         * @param bgRadius background corners radius in dip
         * @return updated builder instance
         */
        public Builder setBackgroundCornerRadiusDp(int bgRadius) {
            return setBackgroundCornerRadius(Util.dp2px(bgRadius));
        }

        /**
         * Set up text left padding in pixels
         *
         * @param paddingLeft text left padding in pixels
         * @return updated builder instance
         */
        public Builder setPaddingLeft(int paddingLeft) {
            this.paddingLeft = paddingLeft;
            return this;
        }

        /**
         * Set up text left padding in dip and covert in to pixels
         *
         * @param paddingLeft left padding in dip
         * @return updated builder instance
         */
        public Builder setPaddingLeftDp(int paddingLeft) {
            this.paddingLeft = Util.dp2px(paddingLeft);
            return this;
        }

        /**
         * Set up text top padding in in pixels
         *
         * @param paddingTop top padding in pixels
         * @return updated builder instance
         */
        public Builder setPaddingTop(int paddingTop) {
            this.paddingTop = paddingTop;
            return this;
        }

        /**
         * Set up text top padding in dip and covert in to pixels
         *
         * @param paddingTop top padding in dip
         * @return updated builder instance
         */
        public Builder setPaddingTopDp(int paddingTop) {
            this.paddingTop = Util.dp2px(paddingTop);
            return this;
        }

        /**
         * Set up text right padding in in pixels
         *
         * @param paddingRight right padding in pixels
         * @return updated builder instance
         */
        public Builder setPaddingRight(int paddingRight) {
            this.paddingRight = paddingRight;
            return this;
        }

        /**
         * Set up text right padding in dip and covert in to pixels
         *
         * @param paddingRight right padding in dip
         * @return updated builder instance
         */
        public Builder setPaddingRightDp(int paddingRight) {
            this.paddingRight = Util.dp2px(paddingRight);
            return this;
        }

        /**
         * Set up text bottom padding in in pixels
         *
         * @param paddingBottom right padding in pixels
         * @return updated builder instance
         */
        public Builder setPaddingBottom(int paddingBottom) {
            this.paddingBottom = paddingBottom;
            return this;
        }

        /**
         * Set up text bottom padding in dip and covert in to pixels
         *
         * @param paddingBottom bottom padding in dip
         * @return updated builder instance
         */
        public Builder setPaddingBottomDp(int paddingBottom) {
            this.paddingBottom = Util.dp2px(paddingBottom);
            return this;
        }


        /**
         * Set up symmetric padding in pixels
         *
         * @param horizontal horizontal padding in pixels
         * @param vertical   vertical padding in pixels
         * @return updated builder instance
         */
        public Builder setPaddingSymmetric(int horizontal, int vertical) {
            paddingLeft = horizontal;
            paddingTop = vertical;
            paddingRight = horizontal;
            paddingBottom = vertical;
            return this;
        }


        /**
         * Set up symmetric padding in dip and convert in to pixels
         *
         * @param horizontal horizontal padding in dip
         * @param vertical   vertical padding in dip
         * @return updated builder instance
         */
        public Builder setPaddingSymmetricDp(int horizontal, int vertical) {
            return setPaddingSymmetric(Util.dp2px(horizontal), Util.dp2px(vertical));
        }

        /**
         * Set up text typeface
         *
         * @param typeface text typeface
         * @return updated builder instance
         */
        public Builder setTypeface(@Nullable Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        /**
         * Set up text gravity.
         * <p>
         * Support only horizontal gravity: left,right and center
         * <p>
         * Center by default
         *
         * @param gravity value of {@link SimpleGravity}
         * @return updated builder instance
         */
        public Builder setTextGravity(@NonNull SimpleGravity gravity) {
            this.gravity = gravity;
            return this;
        }

        @NonNull
        public SimpleTextDrawable build() {
            SimpleTextDrawable drawable = new SimpleTextDrawable(textSize,
                    colorText,
                    gravity,
                    colorBackground,
                    bgRadius,
                    paddingLeft,
                    paddingTop,
                    paddingRight,
                    paddingBottom,
                    typeface);

            drawable.setText("0"); // init drawable height
            return drawable;
        }
    }
}
