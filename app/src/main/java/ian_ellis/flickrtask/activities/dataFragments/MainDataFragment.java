package ian_ellis.flickrtask.activities.dataFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.json.JSONObject;

import java.util.ArrayList;

import ian_ellis.flickrtask.model.FlickrItem;
import ian_ellis.flickrtask.observables.BooleanBus;
import ian_ellis.flickrtask.observables.Observables;
import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by Ian on 23/05/2015.
 */
public class MainDataFragment extends Fragment {
    // bus
    private BooleanBus mRefreshClickBus;
    // observables
    private ConnectableObservable<ArrayList<FlickrItem>> mFlickrItemsObs;
    private ConnectableObservable<Boolean> mLoadingObs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);

    }

    public BooleanBus getRefreshClickBus(){
        return mRefreshClickBus;
    }

    public void setRefreshClickBus(BooleanBus bus){
        mRefreshClickBus = bus;
    }

    public ConnectableObservable<ArrayList<FlickrItem>> getFlickrItemsObs(){
        return mFlickrItemsObs;
    };

    public void setFlickrItemsObs(ConnectableObservable<ArrayList<FlickrItem>> flickrItemsObs){
        mFlickrItemsObs = flickrItemsObs;
    };

    public ConnectableObservable<Boolean> getLoadingObs() {
        return mLoadingObs;
    }

    public void setLoadingObs(ConnectableObservable<Boolean> loadingObs) {
        mLoadingObs = loadingObs;
    }

}
