package com.osome.stickydecorator;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

public abstract class VerticalStickyDrawableDecor extends VerticalStickyDecor {

    public VerticalStickyDrawableDecor() {
    }

    public VerticalStickyDrawableDecor(boolean reverseLayout) {
        super(reverseLayout);
    }

    @Override
    protected void onDrawHeader(@NonNull Canvas c, int position, @NonNull Rect headerBounds) {
        Drawable drawable = getHeaderDrawable(position, headerBounds);
        onDrawHeaderDrawable(c, position, headerBounds, drawable);
    }

    protected void onDrawHeaderDrawable(@NonNull Canvas c, int position, @NonNull Rect headerBounds, @NonNull Drawable drawable) {
        drawable.setBounds(headerBounds);
        drawable.draw(c);
    }

    @Override
    protected void onDrawSection(@NonNull Canvas c, int position, @NonNull Rect sectionBounds, @NonNull View child) {
        Drawable drawable = getSectionDrawable(position, sectionBounds, child);
        onDrawSectionDrawable(c, position, drawable, sectionBounds);
    }

    protected void onDrawSectionDrawable(@NonNull Canvas c, int position, @NonNull Drawable drawable, Rect bounds) {
        drawable.setBounds(bounds);
        drawable.draw(c);
    }

    /**
     * Return drawable which will be drawn for section
     *
     * @param position      Adapter item position
     * @param sectionBounds Section bounds
     * @param child         RecyclerView's child view
     */
    @NonNull
    protected abstract Drawable getSectionDrawable(int position, @NonNull Rect sectionBounds, @NonNull View child);

    /**
     * Return drawable which will be drawn for header
     *
     * @param position     Adapter item position near by header
     * @param headerBounds Header bounds
     */
    @NonNull
    protected abstract Drawable getHeaderDrawable(int position, @NonNull Rect headerBounds);
}
