package ian_ellis.flickrtask;

import org.json.JSONObject;
import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ian_ellis.flickrtask.activities.RxActionBarActivity;
import ian_ellis.flickrtask.model.FlickrItem;
import ian_ellis.flickrtask.observables.BooleanBus;
import ian_ellis.flickrtask.observables.Observables;
import ian_ellis.flickrtask.view.adapters.FlickrItemImageAdapter;
import rx.Observable;
import rx.android.lifecycle.LifecycleObservable;
import rx.schedulers.Schedulers;

public class MainActivity extends RxActionBarActivity {
    // views
    private View mActionView;
    private MenuItem mRefreshMenuItem;
    // animation
    private Animation mRotation;
    // bus
    private BooleanBus mRefreshClickBus;
    // observables
    private Observable<ArrayList<FlickrItem>> mFlickrItemsObs;
    private Observable<Boolean> mLoadingObs;
    // main view pager
    private ViewPager mPager;
    // View Model
    private ArrayList<FlickrItem> mItems;
    // pager
    private FlickrItemImageAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mItems = new ArrayList<FlickrItem>();
        mPagerAdapter = new FlickrItemImageAdapter(getSupportFragmentManager(), mItems);
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        // prepare views
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActionView = inflater.inflate(R.layout.ic_menu_refresh_image, null);
        // prepare animations
        mRotation = AnimationUtils.loadAnimation(this, R.anim.loading_rotate_anim);
        mRotation.setRepeatCount(Animation.INFINITE);
        // simple bus to handle clicks to the refresh button
        mRefreshClickBus = new BooleanBus();
        // map the click of the refrsh button to a flickrRequest, which is transformed to flickr items
        mFlickrItemsObs = mRefreshClickBus.toObserverable().concatMap(click -> {
            Observable<JSONObject> jsonObs = Observables.flickrRequestObservable(this).subscribeOn(Schedulers.io());
            Observable<ArrayList<FlickrItem>> flickrObj = Observables.flickrItemsObservable(jsonObs).subscribeOn(Schedulers.newThread());
            return flickrObj;
        }).cache();
        // create a loading state observable with the refresh click triggering the loading
        // and the mFlickrItemsObjs triggering the loaded
        mLoadingObs = Observables.loadingObservable(mRefreshClickBus.toObserverable(), mFlickrItemsObs);



    }

    protected void subscribeAll() {

        // binding on lifecycle ensures automaticlly unsubscribed when activity is destroyed and auto subscribed on UI thread
        // ala https://github.com/ReactiveX/RxAndroid/blob/0.x/sample-app/src/main/java/rx/android/samples/LifecycleObservableActivity.java

        LifecycleObservable.bindActivityLifecycle(lifecycle(), mLoadingObs)
                .subscribe(this::loadingStateChanged);

        LifecycleObservable.bindActivityLifecycle(lifecycle(), mFlickrItemsObs)
                .subscribe(this::loaded);
    }

    protected void loaded(ArrayList<FlickrItem> items) {
        mItems.clear();
        mItems.addAll(items);
        mPagerAdapter.notifyDataSetChanged();
    }

    protected void loadingStateChanged(boolean loading) {

        if (loading) {
            mActionView .startAnimation(mRotation);
            mRefreshMenuItem.setActionView(mActionView );
        } else {
            mRefreshMenuItem.getActionView().clearAnimation();
            mRefreshMenuItem.setActionView(null);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // store reference to the menu item so we dont have to keep looking it up
        mRefreshMenuItem = menu.findItem(R.id.menu_refresh_item);
        // now we have the menu item we can subscribe
        subscribeAll();
        // and push to start load
        mRefreshClickBus.push(true);
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
