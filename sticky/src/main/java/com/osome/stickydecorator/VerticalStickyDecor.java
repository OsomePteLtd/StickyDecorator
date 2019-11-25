package com.osome.stickydecorator;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Draws section decoration and sticky header on the top of RecyclerView.
 * Now tested only with {@link androidx.recyclerview.widget.LinearLayoutManager} and vertical orientation.
 * Support reversed layout.
 */
public abstract class VerticalStickyDecor extends VerticalSectionDecor {
    public static int HEADER_HEIGHT_UNDEFINED = -1;

    private int contactPosition = RecyclerView.NO_POSITION;
    private Section contactSection = new Section(-1, RecyclerView.NO_POSITION);
    private int lastHeaderHeight = 0;
    private Rect headerBounds = new Rect();
    private boolean reverseLayout;

    public VerticalStickyDecor() {
        this(false);
    }

    public VerticalStickyDecor(boolean reverseLayout) {
        this.reverseLayout = reverseLayout;
    }

    @Override
    public void getItemOffsets(@NonNull RecyclerView parent, @NonNull Rect rect, @NonNull View view, int position, @NonNull RecyclerView.State state) {
        super.getItemOffsets(parent, rect, view, position, state);
        if (reverseLayout && position == state.getItemCount() - 1) {
            rect.top += getHeaderHeight() + getHeaderMarginTop() + getHeaderMarginTop();
        }
    }

    @Override
    public void prepareDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.prepareDrawOver(c, parent, state);

        // clear last header state
        contactPosition = RecyclerView.NO_POSITION;
        contactSection.isValid = false;
    }

    @Override
    void onDrawSectionInternal(@NonNull Canvas c, int position, @NonNull Rect sectionBounds, @NonNull View child) {
        if (sectionBounds.top > getHeaderMarginTop()) {
            // don't draw section above header
            super.onDrawSectionInternal(c, position, sectionBounds, child);
        }

        lastHeaderHeight = sectionBounds.height();
        lastHeaderHeight = sectionBounds.height();
        int contactPoint = getHeaderHeightInternal() + getHeaderMarginTop() + getHeaderMarginBottom();
        if ((contactPoint >= sectionBounds.top && contactPoint < sectionBounds.bottom + getHeaderMarginBottom())) {
            contactSection.isValid = true;
            contactSection.position = position;
            contactSection.top = sectionBounds.top;
        }
    }

    @Override
    public void onPostDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onPostDrawOver(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            parent.getDecoratedBoundsWithMargins(child, decoratedBounds);
            getViewBounds(parent, child);
            int viewTop = decoratedBounds.top;
            int contactPoint = getHeaderHeightInternal() + getHeaderMarginTop();
            if (viewTop <= contactPoint) {
                contactPosition = position;
            }
        }

        drawHeaderInternal(c, parent, state);
    }

    private void drawHeaderInternal(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (contactSection.isValid) {
            int itemPosition = getItemPosition(state);
            int headerBottom = contactSection.top - getHeaderMarginBottom();
            Rect headerBounds = getHeaderBounds(parent, headerBottom, itemPosition, viewBounds, decoratedBounds);
            if (!reverseLayout && contactSection.position == 0 && headerBounds.top - getHeaderMarginTop() < 0) {
                return;
            }

            onDrawHeader(c, itemPosition, headerBounds);
            return;
        }

        if (contactPosition != RecyclerView.NO_POSITION) {
            int itemPosition = contactPosition;
            int headerBottom = getHeaderMarginTop() + getHeaderHeightInternal();
            Rect headerBounds = getHeaderBounds(parent, headerBottom, itemPosition, viewBounds, decoratedBounds);
            onDrawHeader(c, itemPosition, headerBounds);
        }
    }

    private int getItemPosition(@NonNull RecyclerView.State state) {
        return reverseLayout ?
                Math.min(contactSection.position + 1, state.getItemCount() - 1) :
                Math.max(contactSection.position - 1, 0);
    }

    /**
     * Draw header drawable on canvas
     *
     * @param c            RecyclerView canvas
     * @param position     item position from 0 to {@link RecyclerView.Adapter#getItemCount()}
     * @param headerBounds header bounds
     */
    protected abstract void onDrawHeader(@NonNull Canvas c, int position, @NonNull Rect headerBounds);

    @NonNull
    protected Rect getHeaderBounds(@NonNull RecyclerView parent, int headerBottom, int itemPosition, @NonNull Rect viewBounds, @NonNull Rect decoratedBounds) {
        int left = decoratedBounds.left;
        int top = headerBottom - getHeaderHeightInternal();
        int right = decoratedBounds.right;
        headerBounds.set(left, top, right, headerBottom);
        return headerBounds;
    }

    protected int getHeaderMarginTop() {
        return getSectionMarginTop();
    }

    protected int getHeaderMarginBottom() {
        return getSectionMarginBottom();
    }

    /**
     * Return header height. As default return -1. It means that header height takes from section size
     *
     * @return header height
     */
    protected int getHeaderHeight() {
        return HEADER_HEIGHT_UNDEFINED;
    }

    private int getHeaderHeightInternal() {
        int userHeight = getHeaderHeight();
        if (userHeight != HEADER_HEIGHT_UNDEFINED) {
            return userHeight;
        }
        return lastHeaderHeight;
    }

    private static class Section {
        int top;
        int position;
        boolean isValid = false;

        Section(int top, int position) {
            this.top = top;
            this.position = position;
        }
    }
}