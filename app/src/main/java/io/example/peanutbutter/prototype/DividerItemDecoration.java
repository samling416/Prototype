package io.example.peanutbutter.prototype;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Samuel on 31/05/2017.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int vertical = 0, horizontal = 1;

    private int mode;
    private int verticalSpaceHeight;
    private int horizontalSpaceWidth;

    public DividerItemDecoration(int spacing, int mode) {
        this.mode = mode;
        if (mode == vertical) {
            this.verticalSpaceHeight = spacing;
        } else if (mode == horizontal) {
            this.horizontalSpaceWidth = spacing;
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Adds offset between items.

        if (mode == vertical) {
            //outRect.bottom = verticalSpaceHeight/4;
            outRect.top = verticalSpaceHeight / 4;

            // Remove space in the last item.
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = verticalSpaceHeight / 4;
            } else {
                outRect.bottom = verticalSpaceHeight / 2;
            }
        } else if (mode == horizontal) {
            outRect.right = horizontalSpaceWidth / 4;
            outRect.left = horizontalSpaceWidth / 4;
        }
    }
}
