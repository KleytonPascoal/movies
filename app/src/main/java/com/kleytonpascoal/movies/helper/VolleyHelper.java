package com.kleytonpascoal.movies.helper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kleytonpascoal.movies.provider.HttpStackProvider;

/**
 * Created by kleyton on 12/05/17.
 */

public class VolleyHelper {

    public static RequestQueue newRequestQueue(@NonNull Context context) {
        return Volley.newRequestQueue(context, HttpStackProvider.createHttpStack(context));
    }

}
