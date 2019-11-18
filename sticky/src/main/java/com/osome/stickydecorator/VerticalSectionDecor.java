package com.osome.stickydecorator;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Draws sections decoration
 * By default decor draw full width and height allocated in {@link ConditionItemDecorator.Decor#getConditionItemOffsets(RecyclerView, Rect, View, int)}
 */
public abstract class VerticalSectionDecor implements ConditionItemDecorator.Decor {

    protected final Rect decoratedBounds = new Rect();
    protected final Rect viewBounds = new Rect();
    private Rect sectionBounds = new Rect();

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull View child, int position, @NonNull RecyclerView.State state) {
        parent.getDecoratedBoundsWithMargins(child, decoratedBounds);
        Rect bounds = getViewBounds(parent, child);
        onDrawSectionInternal(c, position, getSectionBounds(position, bounds, decoratedBounds), child);
    }

    void onDrawSectionInternal(@NonNull Canvas c, int position, @NonNull Rect sectionBounds, @NonNull View child) {
        onDrawSection(c, position, sectionBounds, child);
    }

    protected abstract void onDrawSection(@NonNull Canvas c, int position, @NonNull Rect sectionBounds, @NonNull View child);

    protected final Rect getViewBounds(ViewGroup parent, View child) {
        child.getDrawingRect(viewBounds);
        parent.offsetDescendantRectToMyCoords(child, viewBounds);
        return viewBounds;
    }

    @NonNull
    protected Rect getSectionBounds(int position, @NonNull Rect viewBounds, @NonNull Rect decoratedBounds) {
        int left = viewBounds.left;
        int top = decoratedBounds.top + getSectionMarginTop();
        int right = viewBounds.right;
        int bottom = viewBounds.top - getSectionMarginBottom();
        sectionBounds.set(left, top, right, bottom);
        return sectionBounds;
    }

    @Override
    public void prepareDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

    }

    @Override
    public void onPostDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

    }

    @Override
    public void getItemOffsets(@NonNull RecyclerView parent, @NonNull Rect rect, @NonNull View view, int position, @NonNull RecyclerView.State state) {

    }


    @Override
    public void getConditionItemOffsets(@NonNull RecyclerView parent, @NonNull Rect rect, @NonNull View view, int position) {
        rect.top += getSectionHeight(position) + getSectionMarginTop() + getSectionMarginBottom();
    }

    /**
     * Get section height
     *
     * @param position item adapter position
     * @return section height
     */
    protected abstract int getSectionHeight(int position);

    /**
     * Get top margin for section
     *
     * @return vertical margin
     */
    protected int getSectionMarginTop() {
        return 0;
    }

    /**
     * Get bottom margin for section
     *
     * @return vertical margin
     */
    protected int getSectionMarginBottom() {
        return 0;
    }
}
