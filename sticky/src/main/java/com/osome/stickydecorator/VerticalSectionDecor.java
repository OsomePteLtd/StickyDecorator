package com.osome.stickydecorator;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Draws sections decoration
 * By default decor draw full width and height allocated in {@link ConditionItemDecorator.Decor#getConditionItemOffsets(RecyclerView, Rect, View, int)}
 */
public abstract class VerticalSectionDecor implements ConditionItemDecorator.Decor {
    protected GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.DefaultSpanSizeLookup();

    protected final Rect decoratedBounds = new Rect();
    protected final Rect viewBounds = new Rect();
    private Rect sectionBounds = new Rect();

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull View child, int position, @NonNull RecyclerView.State state) {
        parent.getDecoratedBoundsWithMargins(child, decoratedBounds);
        Rect bounds = getViewBounds(parent, child);
        onDrawSectionInternal(c, position, getSectionBounds(parent, position, bounds, decoratedBounds), child);
    }

    void onDrawSectionInternal(@NonNull Canvas c, int position, @NonNull Rect sectionBounds, @NonNull View child) {
        onDrawSection(c, position, sectionBounds, child);
    }

    protected abstract void onDrawSection(@NonNull Canvas c, int position, @NonNull Rect sectionBounds, @NonNull View child);

    /**
     * Get view bounds without decoration offsets
     *
     * @param parent - current recycler view
     * @param child  - itemView from recycler view
     * @return real view bounds without decoration offsets
     */
    protected Rect getViewBounds(@NonNull RecyclerView parent, @NonNull View child) {
        child.getDrawingRect(viewBounds);
        parent.offsetDescendantRectToMyCoords(child, viewBounds);
        addTranslationOffsetToViewBounds(child, viewBounds);
        return viewBounds;
    }

    /**
     * Add view translation to bound to support {@link androidx.recyclerview.widget.DefaultItemAnimator}
     *
     * @param child      - itemView from recycler
     * @param viewBounds - current itemView bounds without decoration offsets
     */
    protected void addTranslationOffsetToViewBounds(@NonNull View child, @NonNull Rect viewBounds) {
        viewBounds.offset((int) child.getTranslationX(), (int) child.getTranslationY());
    }

    @NonNull
    protected Rect getSectionBounds(@NonNull RecyclerView parent, int position, @NonNull Rect viewBounds, @NonNull Rect decoratedBounds) {
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

    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        this.spanSizeLookup = spanSizeLookup;
    }
}
