package com.osome.stickydecorator;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Uses drawable to draw section above item
 */
public abstract class VerticalDrawableSectionDecor extends VerticalSectionDecor {

    @Override
    protected void onDrawSection(@NonNull Canvas c, int position, @NonNull Rect sectionBounds, @NonNull View child) {
        Drawable drawable = getDrawable(position, sectionBounds, child);
        onDrawDrawable(c, position, drawable, sectionBounds);
    }

    protected void onDrawDrawable(@NonNull Canvas c, int position, @NonNull Drawable drawable, Rect bounds) {
        drawable.setBounds(bounds);
        drawable.draw(c);
    }

    /**
     * Return drawable which will be drawn
     *
     * @param position      Adapter item position
     * @param sectionBounds Section bounds
     * @param child         RecyclerView's child view
     */
    @NonNull
    protected abstract Drawable getDrawable(int position, @NonNull Rect sectionBounds, @NonNull View child);
}
