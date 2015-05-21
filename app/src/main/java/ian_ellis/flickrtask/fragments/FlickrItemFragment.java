package ian_ellis.flickrtask.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ian_ellis.flickrtask.R;
import ian_ellis.flickrtask.model.FlickrItem;

/**
 * Created by Ian on 20/05/2015.
 */
public class FlickrItemFragment extends Fragment{
    private static String IMAGE_PATH = "image";

    public static FlickrItemFragment newInstance(FlickrItem item) {
        FlickrItemFragment fragment = new FlickrItemFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_PATH, item.getMediumImagePath());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.image_fragment_view, container, false);
    }
}
