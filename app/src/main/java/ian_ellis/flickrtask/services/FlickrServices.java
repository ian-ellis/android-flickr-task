package ian_ellis.flickrtask.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by Ian on 17/05/2015.
 */
public class FlickrServices {

    private String API_PATH = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";
    private Context mContext;

    public FlickrServices(Context context) {
        mContext =  context;
    }

    public void makeRequest(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        RequestQue.getInstance(mContext).addToRequestQueue(getRequest(listener, errorListener));
    }

    public JsonObjectRequest getRequest(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        return new JsonObjectRequest(Request.Method.GET, API_PATH, null,listener, errorListener);
    }
}
