package ian_ellis.flickrtask.observables;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ian_ellis.flickrtask.model.FlickrItem;
import ian_ellis.flickrtask.services.RequestQueue;
import ian_ellis.flickrtask.services.Requests;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Ian on 17/05/2015.
 */
public class Observables {

    public static final String API_PATH = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

    public static Observable<JSONObject> flickrRequestObservable(Context context){
        Requests requests = new Requests(context);

        return Observable.create(sub -> {
            RequestQueue.getInstance(context).addToRequestQueue(
                requests.getRequest(API_PATH,
                    next -> {
                        sub.onNext(next);
                        sub.onCompleted();
                    },
                    err -> {
                        sub.onError(err);
                    }
                )
            );
        });
    }

    public static Observable<ArrayList<FlickrItem>> flickrLoadObservable(Observable<?> trigger,  Context context){
        return trigger.concatMap(trig -> {
            Observable<JSONObject> jsonObs = Observables.flickrRequestObservable(context).subscribeOn(Schedulers.io());
            Observable<ArrayList<FlickrItem>> flickrObs = Observables.flickrDataTransformObservable(jsonObs).subscribeOn(Schedulers.newThread());
            return flickrObs;
        });
    }

    public static Observable<ArrayList<FlickrItem>> flickrDataTransformObservable(Observable<JSONObject> jsonObs) {
        return jsonObs.map(json -> {
            try {
                JSONArray arr = json.getJSONArray("items");
                return arr;
            } catch (JSONException e) {
                return new JSONArray();
            }
        })
        .map(jsonArr -> {
            ArrayList<FlickrItem> items = new ArrayList<FlickrItem>();
            int l = jsonArr.length();
            for (int i = 0; i < l; i++) {
                try {
                    JSONObject obj = jsonArr.getJSONObject(i);
                    if (FlickrItem.hasMedia(obj)) {
                        items.add(new FlickrItem(obj));
                    }
                } catch (JSONException e) {
                }
            }
            return items;
        });
    };

    public static Observable<Boolean> loadingObservable(Observable<?> loadingTrigger, Observable<?> loadedTrigger) {
        return  rx.Observable.merge(
            loadingTrigger.map(obj -> {
                return true;
            }),
            loadedTrigger.map(obj -> {
                return false;
            })
        );
    };
}
