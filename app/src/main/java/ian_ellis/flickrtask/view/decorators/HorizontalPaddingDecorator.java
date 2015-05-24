package ian_ellis.flickrtask.view.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Ian on 24/05/2015.
 */
public class HorizontalPaddingDecorator extends RecyclerView.ItemDecoration{

    private ArrayList<?> mItems;
    private int mPadding;

    public HorizontalPaddingDecorator(int padding, ArrayList<?> items) {
        mPadding = padding;
        mItems = items;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        int index = parent.getChildAdapterPosition(view);

        outRect.left= (index == 0) ? 0 : mPadding;
        outRect.right = (index == mItems.size() -1)? 0 : mPadding;

    }
}
