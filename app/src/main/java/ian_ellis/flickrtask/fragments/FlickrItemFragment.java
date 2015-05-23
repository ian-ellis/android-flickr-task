package ian_ellis.flickrtask.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

import ian_ellis.flickrtask.R;
import ian_ellis.flickrtask.model.FlickrItem;
import ian_ellis.flickrtask.services.RequestQueue;

/**
 * Created by Ian on 20/05/2015.
 */
public class FlickrItemFragment extends Fragment {

    private static String IMAGE_PATH = "image";

    NetworkImageView mNetworkImageView;
    RequestQueue mRequestQue;
    String mImagePath;

    public static FlickrItemFragment newInstance(FlickrItem item) {
        FlickrItemFragment fragment = new FlickrItemFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_PATH, item.getMediumImagePath());
        fragment.setArguments(args);
        return fragment;
    }

    public String getImagePath(){return mImagePath;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQue = RequestQueue.getInstance(getActivity());

        Bundle args = getArguments();
        mImagePath = args.getString(IMAGE_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.image_fragment_view, container, false);
        mNetworkImageView = (NetworkImageView) v.findViewById(R.id.flickr_network_image);
        mNetworkImageView.setImageUrl(mImagePath, mRequestQue.getImageLoader());
        return v;
    }
}
