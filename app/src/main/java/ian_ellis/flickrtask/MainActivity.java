package ian_ellis.flickrtask;

import org.json.JSONObject;
import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import ian_ellis.flickrtask.activities.RxActionBarActivity;
import ian_ellis.flickrtask.activities.dataFragments.MainDataFragment;
import ian_ellis.flickrtask.model.FlickrItem;
import ian_ellis.flickrtask.observables.BooleanBus;
import ian_ellis.flickrtask.observables.Observables;
import ian_ellis.flickrtask.view.adapters.FlickrItemImageAdapter;
import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.android.lifecycle.LifecycleObservable;
import rx.schedulers.Schedulers;

public class MainActivity extends RxActionBarActivity {

    private static String DATA_TAG = "MainActivityData";

    // views
    private View mActionView;
    private MenuItem mRefreshMenuItem;
    // animation
    private Animation mRotation;
    // bus
    private BooleanBus mRefreshClickBus;
    // observables
    private ConnectableObservable<ArrayList<FlickrItem>> mFlickrItemsObs;
    private ConnectableObservable<Boolean> mLoadingObs;
    // main view pager
    private ViewPager mPager;
    // View Model
    private ArrayList<FlickrItem> mItems;
    // pager
    private FlickrItemImageAdapter mPagerAdapter;

    private MainDataFragment mDataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create data item
        mItems = new ArrayList<FlickrItem>();
        //create view items
        mPagerAdapter = new FlickrItemImageAdapter(getSupportFragmentManager(), mItems);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActionView = inflater.inflate(R.layout.ic_menu_refresh_image, null);
        // prepare animations
        mRotation = AnimationUtils.loadAnimation(this, R.anim.loading_rotate_anim);
        mRotation.setRepeatCount(Animation.INFINITE);
        //see if we a re rebulding due to a config change
        FragmentManager fm = getSupportFragmentManager();
        mDataFragment= (MainDataFragment) fm.findFragmentByTag(DATA_TAG);
        boolean rebuilt = (mDataFragment != null);
        // if we aare not, create data fragment to store observables
        // across config changes
        if (rebuilt) {
            // retrieve observables from data fragment
            mFlickrItemsObs = mDataFragment.getFlickrItemsObs();
            mLoadingObs = mDataFragment.getLoadingObs();
            mRefreshClickBus = mDataFragment.getRefreshClickBus();
        } else {
            mDataFragment = new MainDataFragment();
            fm.beginTransaction().add(mDataFragment,DATA_TAG).commit();
            // simple bus to handle clicks to the refresh button
            mRefreshClickBus = new BooleanBus();
            // get a flickr observable and transform it
            Observable<ArrayList<FlickrItem>> flickrItemsObs = getFlickrObs();
            mFlickrItemsObs = flickrItemsObs.cache().replay(1);
            // publish and connect so we can begin
            mFlickrItemsObs.publish();
            mFlickrItemsObs.connect();
            // create a loading state observable with the refresh click triggering the loading
            // and the mFlickrItemsObjs triggering the loaded
            mLoadingObs = Observables.loadingObservable(mRefreshClickBus.toObserverable(), mFlickrItemsObs).replay(1);
            mLoadingObs.publish();
            mLoadingObs.connect();
            // push observables into data fragment for storage
            mDataFragment.setFlickrItemsObs(mFlickrItemsObs);
            mDataFragment.setLoadingObs(mLoadingObs);
            mDataFragment.setRefreshClickBus(mRefreshClickBus);
        }
        // begin subscribing to flickr items
        LifecycleObservable.bindActivityLifecycle(lifecycle(), mFlickrItemsObs)
        .subscribe(this::loaded);//, this::flickrObsError);
        // if we didnt rebuild- ie first load, then request a refresh
        if(!rebuilt) {
            mRefreshClickBus.push(true);
        }
    }

    private Observable<ArrayList<FlickrItem>> getFlickrObs(){
        return Observables.flickrLoadObservable(
                mRefreshClickBus.toObserverable(),
                getApplicationContext()
        ).onErrorResumeNext(err -> {
            handleFlickrError(err);
            loadingStateChanged(false);
            return getFlickrObs();
        });
    }

    private void handleFlickrError(Throwable err){
        String msg = "Oops some thing went wrong when loading images";
        if(!isNetworkAvailable()){
            msg = "No Internet Connection";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private NetworkInfo getActiveNetworkInfo(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo;
    }
    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loaded(ArrayList<FlickrItem> items) {
        mItems.clear();
        mItems.addAll(items);
        mPagerAdapter.notifyDataSetChanged();
        mPager.setCurrentItem(0);
    }




    protected void loadingStateChanged(boolean loading) {
        if (loading) {
            mActionView.startAnimation(mRotation);
            mRefreshMenuItem.setActionView(mActionView);
        } else {
            mActionView.clearAnimation();
            mRefreshMenuItem.setActionView(null);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // store reference to the menu item so we dont have to keep looking it up
        mRefreshMenuItem = menu.findItem(R.id.menu_refresh_item);
        // now we have the menu item we can subscribe and start loading
        // startUp();
        LifecycleObservable.bindActivityLifecycle(lifecycle(), mLoadingObs)
                .subscribe(this::loadingStateChanged);//, this::loadingObsError);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh_item) {
            //refresh clicked so push value to the bus
            mRefreshClickBus.push(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
