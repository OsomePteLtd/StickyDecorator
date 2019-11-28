package com.osome.stickydecorator;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An ConditionItemDecorator allows the application to add a special
 * drawing and layout offset to specific item views from the adapter's data set.
 * To determine which item should decorate you can use {@link ConditionItemDecorator.Condition}
 * This can be useful for drawing sections or header
 * between items, highlights, visual grouping boundaries and more.
 */
public class ConditionItemDecorator extends RecyclerView.ItemDecoration {

    private final Condition condition;
    private final Decor decor;

    public ConditionItemDecorator(@NonNull Condition condition, @NonNull Decor decor) {
        this.condition = condition;
        this.decor = decor;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (condition.isForDrawOver(position)) {
            decor.getConditionItemOffsets(parent, outRect, view, position);
        }
        decor.getItemOffsets(parent, outRect, view, position, state);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            return;
        }

        decor.prepareDrawOver(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(child);
            if (condition.isForDrawOver(index)) {
                decor.onDrawOver(c, parent, child, index, state);
            }
        }
        decor.onPostDrawOver(c, parent, state);
    }

    public interface Condition {
        /**
         * Determines for which item should apply decoration
         *
         * @param position RecyclerView's adapter position
         * @return true if for position should apply decoration, false otherwise
         */
        boolean isForDrawOver(int position);
    }

    public interface Decor {

        /**
         * Called every time when called {@link RecyclerView.ItemDecoration#onDrawOver(Canvas, RecyclerView, RecyclerView.State)}.
         * Called once before {@link ConditionItemDecorator.Decor#onDrawOver(Canvas, RecyclerView, View, int, RecyclerView.State)}
         *
         * @param c      Canvas to draw into
         * @param parent RecyclerView this Decor is drawing into
         * @param state  The current state for RecyclerView
         */
        void prepareDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state);

        /**
         * Draw any appropriate decorations into the Canvas supplied to the RecyclerView.
         * Any content drawn by this method will be drawn after the item views are drawn
         * and will thus appear over the views. This method call for each view in RecyclerView
         *
         * @param c        Canvas to draw into
         * @param parent   RecyclerView this Decor is drawing into
         * @param child    RecyclerView's child view to to determine place to drawing
         * @param position RecyclerView's adapter position of view
         * @param state    The current state for RecyclerView
         */
        void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull View child, int position, @NonNull RecyclerView.State state);

        /**
         * Called every time when called {@link RecyclerView.ItemDecoration#onDrawOver(Canvas, RecyclerView, RecyclerView.State)}.
         * Called once after {@link ConditionItemDecorator.Decor#onDrawOver(Canvas, RecyclerView, View, int, RecyclerView.State)}
         *
         * @param c      Canvas to draw into
         * @param parent RecyclerView this Decor is drawing into
         * @param state  The current state for RecyclerView
         */
        void onPostDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state);

        /**
         * Retrieve any offsets for the given item which satisfies condition {@link ConditionItemDecorator.Condition#isForDrawOver(int)}.
         * Each field of <code>rect</code> specifies
         * the number of pixels that the item view should be inset by, similar to padding or margin.
         * The default implementation sets the bounds of outRect to 0 and returns.
         *
         * <p>
         * If this Decor does not affect the positioning of item views, it should set
         * all four fields of <code>rect</code> (left, top, right, bottom) to zero
         * before returning.
         *
         * @param parent   RecyclerView this ItemDecoration is decorating
         * @param rect     Rect to receive the output.
         * @param view     The child view to decorate
         * @param position The child view adapter position
         */
        void getConditionItemOffsets(@NonNull RecyclerView parent, @NonNull Rect rect, @NonNull View view, int position);


        /**
         * Retrieve any offsets for the given item. Each field of <code>outRect</code> specifies
         * the number of pixels that the item view should be inset by, similar to padding or margin.
         * The default implementation sets the bounds of outRect to 0 and returns.
         *
         * <p>
         * If this Decor does not affect the positioning of item views, it should set
         * all four fields of <code>rect</code> (left, top, right, bottom) to zero
         * before returning.
         *
         * @param parent   RecyclerView this ItemDecoration is decorating
         * @param rect     Rect to receive the output.
         * @param view     The child view to decorate
         * @param position The child view adapter position
         * @param state    The current state of RecyclerView.
         */
        void getItemOffsets(@NonNull RecyclerView parent, @NonNull Rect rect, @NonNull View view, int position, @NonNull RecyclerView.State state);
    }

    public static abstract class SimpleDecor implements Decor {

        @Override
        public void prepareDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull View child, int position, @NonNull RecyclerView.State state) {

        }

        @Override
        public void onPostDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        }

        @Override
        public void getConditionItemOffsets(@NonNull RecyclerView parent, @NonNull Rect rect, @NonNull View view, int position) {

        }

        @Override
        public void getItemOffsets(@NonNull RecyclerView parent, @NonNull Rect rect, @NonNull View view, int position, @NonNull RecyclerView.State state) {

        }
    }
}
