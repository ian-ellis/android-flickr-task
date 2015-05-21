package ian_ellis.flickrtask.view.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import ian_ellis.flickrtask.fragments.FlickrItemFragment;
import ian_ellis.flickrtask.model.FlickrItem;

/**
 * Created by Ian on 21/05/2015.
 */
public class FlickrItemImageAdapter extends FragmentStatePagerAdapter {

    ArrayList<FlickrItem> mItems;

    public FlickrItemImageAdapter(FragmentManager fm, ArrayList<FlickrItem> items) {
        super(fm);
        mItems = items;
    }
    @Override
    public Fragment getItem(int position) {
        return FlickrItemFragment.newInstance(mItems.get(position));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }
}
