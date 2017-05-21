package com.kleytonpascoal.movies.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import com.kleytonpascoal.movies.provider.NetworkStatusProvider;

/**
 * Created by kleyton on 12/05/17.
 */

public class NetworkConnectivityBroadcastReceiver extends BroadcastReceiver {

    private static final String PKG = NetworkConnectivityBroadcastReceiver.class.getName();

    public static final String NETWORK_CONN_STATUS_ACTION = PKG + ".extra.NETWORK_CONN_STATUS_ACTION";
    public static final String NETWORK_CONN_STATUS_EXTRA = PKG + ".extra.NETWORK_CONN_STATUS_EXTRA";

    @Override
    public void onReceive(Context context, Intent intent) {
        /*LocalBroadcastManager.getInstance(context).sendBroadcast(
                new Intent(NETWORK_CONN_STATUS_ACTION).
                        putExtra(NETWORK_CONN_STATUS_EXTRA, NetworkStatusProvider.isConnected(context)));*/
    }
}
