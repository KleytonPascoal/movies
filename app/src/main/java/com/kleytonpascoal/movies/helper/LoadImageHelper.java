package com.kleytonpascoal.movies.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kleytonpascoal.movies.R;

/**
 * Created by kleyton on 09/05/17.
 */

public class LoadImageHelper {

    public static void loadImageFromUrl(final Context context, final String path, final ImageView imageView) {
        Glide.with(context).load(UrlBuilder.buildImageUrl(path)).into(imageView).onLoadFailed(context.getResources().getDrawable(R.drawable.movie_poster_no_picture));
    }
}
