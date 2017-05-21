package com.kleytonpascoal.movies.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

/**
 * Created by kleyton on 16/05/17.
 */

public class NetworkStatusProvider {

    private static final String TAG = NetworkStatusProvider.class.getSimpleName();

    /* simulate network connection */
    @VisibleForTesting
    public static boolean HAS_NETWORK_CONNECTION = true;

    public static boolean isConnected(Context context) {
        return HAS_NETWORK_CONNECTION;
    }
}
