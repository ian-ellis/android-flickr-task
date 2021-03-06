package ian_ellis.flickrtask.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ian on 17/05/2015.
 */
public class FlickrItem {
    public static boolean hasMedia(JSONObject json){
        String mediaPath = getMediaPath(json, "m");
        return !(mediaPath.equals(""));
    }

    private String mTitle;
    private String mLink;
    private String mDateTaken;
    private String mDescription;
    private String mMediumImagePath;
    private JSONObject mJson;

    public FlickrItem(JSONObject json) {
        mJson = json;
        mTitle = getString(json, "title");
        mLink= getString(json, "link");
        mDateTaken = getString(json, "date_taken");
        mDescription = getString(json, "description");
        mMediumImagePath = getMediaPath(json, "m");
    }

    public JSONObject getJson()         {return mJson;}
    public String getMediumImagePath()  {return mMediumImagePath;}

    @Override
    public boolean equals(Object other){
        if(other instanceof FlickrItem ) {
            FlickrItem otherFlickr = (FlickrItem)other;
            return mJson.toString().equals(otherFlickr.getJson().toString());
        }
        return false;
    };


    private static String getMediaPath(JSONObject json, String propName) {
        try {
            JSONObject media = json.getJSONObject("media");
            String mediaUrl = media.getString(propName);
            return mediaUrl;
        }catch(JSONException e) {
            return "";
        }
    }

    private static String getString(JSONObject json, String propName) {
        try{
            return json.getString(propName);
        }catch (JSONException e){
            return "";
        }
    }
}
