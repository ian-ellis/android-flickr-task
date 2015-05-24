package ian_ellis.flickrtask.view.decorators;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ian on 24/05/2015.
 */
public class ImageSelectedDecorator extends RecyclerView.ItemDecoration {
    private Drawable mSelectedDrawable;
    private int mSelectedIndex = 0;

    public ImageSelectedDecorator(Drawable selectedDrawable){
        mSelectedDrawable = selectedDrawable;
    }

    public void setSelectedIndex(int index) {
        mSelectedIndex = index;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        int index = parent.getChildAdapterPosition(view);

        if(index == mSelectedIndex) {
            view.setBackgroundDrawable(mSelectedDrawable);
        } else {
            view.setBackgroundResource(0);
        }
    }
}
