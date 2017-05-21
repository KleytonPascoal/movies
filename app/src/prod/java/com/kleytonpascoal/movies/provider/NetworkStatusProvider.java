package com.kleytonpascoal.movies.provider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.VisibleForTesting;

/**
 * Created by kleyton on 16/05/17.
 */

public class NetworkStatusProvider {

    @VisibleForTesting
    public static boolean HAS_NETWORK_CONNECTION = true;

    public static boolean isConnected(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
