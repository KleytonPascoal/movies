package com.kleytonpascoal.movies.provider;

import android.content.Context;

import com.android.volley.toolbox.HttpStack;

/**
 * Created by kleyton on 12/05/17.
 */

public class HttpStackProvider {

    public static HttpStack createHttpStack(Context context) {
        // let volley create the default HttpStack
        return null;
    }

}
