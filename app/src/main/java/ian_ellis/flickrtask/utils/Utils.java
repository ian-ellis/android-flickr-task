package ian_ellis.flickrtask.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 * Created by Ian on 24/05/2015.
 */
public class Utils {

    public static int getPixelsFromDp(int dp, DisplayMetrics metrics) {
        return Math.round((float) dp * metrics.density);
    }

    public static NetworkInfo getActiveNetworkInfo(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo;
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = Utils.getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
