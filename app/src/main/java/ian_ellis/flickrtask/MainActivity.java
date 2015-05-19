package ian_ellis.flickrtask;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import org.json.JSONObject;

import java.util.ArrayList;

import ian_ellis.flickrtask.activities.RxActionBarActivity;
import ian_ellis.flickrtask.model.FlickrItem;
import ian_ellis.flickrtask.observables.BooleanBus;
import ian_ellis.flickrtask.observables.Observables;
import ian_ellis.flickrtask.services.Requests;
import rx.Observable;
import rx.Subscription;
import rx.android.lifecycle.LifecycleObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MainActivity extends RxActionBarActivity {
    // views
    private TextView mHelloText;
    private TextView mLoadingText;
    // bus
    private BooleanBus mRefreshClickBus;
    // observables
    private Observable<ArrayList<FlickrItem>> mFlickrItemsObs;
    private Observable<Boolean> mLoadingObs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelloText = (TextView) findViewById(R.id.helloText);
        mLoadingText = (TextView) findViewById(R.id.loadingText);
        mRefreshClickBus = new BooleanBus();

        mFlickrItemsObs = mRefreshClickBus.toObserverable().flatMap(click -> {
            Observable<JSONObject> jsonObs = Observables.flickrRequestObservable(this).subscribeOn(Schedulers.io());
            return Observables.flickrItemsObservable(jsonObs).subscribeOn(Schedulers.newThread());
        });

        mLoadingObs = Observables.loadingObservable(mRefreshClickBus.toObserverable(), mFlickrItemsObs);

        // binding on lifecycle ensures automaticlly unsubscribed when activity is destroyed
        // ala https://github.com/ReactiveX/RxAndroid/blob/0.x/sample-app/src/main/java/rx/android/samples/LifecycleObservableActivity.java
        LifecycleObservable.bindActivityLifecycle(lifecycle(),mLoadingObs)
                .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::loadingStateChanged);


        LifecycleObservable.bindActivityLifecycle(lifecycle(),mFlickrItemsObs)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::loaded);


    }

    protected void loaded(ArrayList<FlickrItem> items) {
        mHelloText.setText("GOT FLICKR ITEMS");
    }

    protected void loadingStateChanged(boolean loading) {
        String msg = (loading) ? "Loading" : "Loaded";
        mLoadingText.setText(msg);
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.menu_refresh) {
            mRefreshClickBus.push(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
