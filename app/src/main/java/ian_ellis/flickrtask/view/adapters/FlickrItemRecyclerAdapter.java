package ian_ellis.flickrtask.view.adapters;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import ian_ellis.flickrtask.R;
import ian_ellis.flickrtask.model.FlickrItem;

/**
 * Created by Ian on 24/05/2015.
 */
public class FlickrItemRecyclerAdapter extends RecyclerView.Adapter<FlickrItemRecyclerAdapter .ViewHolder> {

    private ArrayList<FlickrItem> mItems;
    private ImageLoader mImageLoader;



    public FlickrItemRecyclerAdapter(ArrayList<FlickrItem> items, ImageLoader imageLoader) {
        mItems = items;
        mImageLoader = imageLoader;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.flickr_list_item, parent, false);
        return new ViewHolder(sView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FlickrItem item = mItems.get(position);
        holder.getImageView().setDefaultImageResId(R.drawable.ic_flickr);
        holder.getImageView().setImageUrl(item.getMediumImagePath(), mImageLoader);


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private NetworkImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (NetworkImageView) view.findViewById(R.id.flickr_network_image);
        }

        public NetworkImageView getImageView() {
            return mImageView;
        }
    }



}
