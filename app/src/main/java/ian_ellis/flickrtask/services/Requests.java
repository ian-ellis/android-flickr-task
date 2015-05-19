package ian_ellis.flickrtask.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;


/**
 * Created by Ian on 17/05/2015.
 */
public class Requests {

    private Context mContext;

    public Requests(Context context) {
        mContext =  context;
    }

    public void makeRequest(Request request) {
        RequestQue.getInstance(mContext).addToRequestQueue(request);
    }

    public JsonObjectRequest getRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        return new JsonObjectRequest(Request.Method.GET, url, null,listener, errorListener);
    }

    /**
     * Returns a Synchronouse JSONObject request
     * @return
     */
    public JsonObjectRequest getFutureRequest(String url, RequestFuture<JSONObject> future) {
        return new JsonObjectRequest(Request.Method.GET, url, null,future, future);
    }
}
