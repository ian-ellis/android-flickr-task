package ian_ellis.flickrtask;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import org.json.JSONException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ian_ellis.flickrtask.services.FlickrServices;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity {

    private FlickrServices mServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView helloText = (TextView) findViewById(R.id.helloText);

        mServices = new FlickrServices(this);

        runOnUiThread(()->{
            helloText.setText("LAMBDA STYLE");
        });
        mServices.makeRequest(json -> {
            try {
                helloText.setText("JSON LAMBDA STYLE " + json.get("title"));
            }catch (JSONException e){
                helloText.setText("JSON LAMBDA STYLE ");
            }
        },error -> {
            helloText.setText("UN OH LAMBDA STYLE" + error.getMessage());
        });


//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    mTxtDisplay.setText("Response: " + response.toString());
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    // TODO Auto-generated method stub
//
//                }
//            });




    }

//    @Override
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

        return super.onOptionsItemSelected(item);
    }
}
