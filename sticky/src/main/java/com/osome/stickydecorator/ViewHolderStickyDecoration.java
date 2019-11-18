package com.osome.stickydecorator;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderStickyDecoration extends RecyclerView.ItemDecoration {

    private Rect bounds = new Rect();
    private Pair<Integer, RecyclerView.ViewHolder> currentHeader;
    private final Condition condition;
    private final boolean reverseLayout;

    public ViewHolderStickyDecoration(@NonNull RecyclerView parent, @NonNull Condition condition) {
        this(parent, condition, false);
    }

    public ViewHolderStickyDecoration(@NonNull RecyclerView parent, @NonNull Condition condition, boolean reverseLayout) {
        this.condition = condition;
        this.reverseLayout = reverseLayout;
        init(parent);
    }

    private void init(@NonNull RecyclerView parent) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("Firstly set adapter");
        }

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                clearHeader();
            }
        });

        parent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                clearHeader();
            }
        });
    }

    private void clearHeader() {
        currentHeader = null;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            return;
        }

        View topChild = getTopChild(parent);
        if (topChild == null) {
            return;
        }

        int topPosition = parent.getChildAdapterPosition(topChild);
        View header = getHeaderViewForItem(topPosition, parent);
        if (header == null) {
            return;
        }

        int contactPoint = header.getBottom();
        View contactChild = getChildInContact(parent, contactPoint);
        if (contactChild == null) {
            return;
        }

        if (condition.isHeader(parent.getChildAdapterPosition(contactChild))) {
            moveHeader(c, header, contactChild);
            return;
        }

        drawHeader(c, header);
    }

    @Nullable
    private View getTopChild(@NonNull RecyclerView parent) {
        if (reverseLayout) {
            return parent.findChildViewUnder(0, 0);
        }
        return parent.getChildAt(0);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private View getHeaderViewForItem(int position, RecyclerView parent) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return null;
        }
        int headerPosition = reverseLayout ?
                getHeaderPositionForItemRevense(adapter.getItemCount(), position) :
                getHeaderPositionForItem(position);

        if (headerPosition == RecyclerView.NO_POSITION) {
            return null;
        }

        int viewType = adapter.getItemViewType(headerPosition);
        if (currentHeader != null && headerPosition == currentHeader.first && currentHeader.second.getItemViewType() == viewType) {
            return currentHeader.second.itemView;
        }

        RecyclerView.ViewHolder holder = adapter.createViewHolder(parent, viewType);
        adapter.onBindViewHolder(holder, headerPosition);
        fixViewSize(parent, holder.itemView);
        currentHeader = new Pair<>(headerPosition, holder);
        return holder.itemView;
    }

    private int getHeaderPositionForItem(int position) {
        int headerPosition = RecyclerView.NO_POSITION;
        int currentPosition = position;
        do {
            if (condition.isHeader(currentPosition)) {
                headerPosition = currentPosition;
                break;
            }
            currentPosition -= 1;
        } while (currentPosition >= 0);

        return headerPosition;
    }

    private int getHeaderPositionForItemRevense(int totalCount, int position) {
        int headerPosition = RecyclerView.NO_POSITION;
        int currentPosition = position;
        do {
            if (condition.isHeader(currentPosition)) {
                headerPosition = currentPosition;
                break;
            }
            currentPosition += 1;
        } while (currentPosition < totalCount);

        return headerPosition;
    }

    private void fixViewSize(ViewGroup parent, View view) {
        // Specs for parent (RecyclerView)
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        // Specs for children (headers)
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    @Nullable
    private View getChildInContact(RecyclerView parent, int contactPoint) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, bounds);
            if (bounds.bottom > contactPoint && bounds.top <= contactPoint) {
                return child;
            }
        }

        return null;
    }

    private void moveHeader(Canvas c, View header, View nextHeader) {
        c.save();
        c.translate(0, (nextHeader.getTop() - header.getHeight()));
        header.draw(c);
        c.restore();
    }

    private void drawHeader(Canvas c, View header) {
        c.save();
        c.translate(0, 0);
        header.draw(c);
        c.restore();
    }

    public interface Condition {
        boolean isHeader(int position);
    }
}
